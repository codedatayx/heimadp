package wallswq.dianping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.Shop;
import wallswq.dianping.domian.po.User;
import wallswq.dianping.mapper.ShopMapper;
import wallswq.dianping.service.IShopService;
import wallswq.dianping.utils.RedisData;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static wallswq.dianping.constant.RedisConstant.*;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    private final StringRedisTemplate stringRedisTemplate;

    public ShopServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Result queryShopById(Long id) {
        //Shop shop = queryShopWithMutex(id);
        Shop shop = queryShopWithLogicalExpire(id);
        if (shop == null) {
            return Result.fail("店铺不存在");
        }
        return Result.ok(shop);
    }
    public Shop queryShopWithMutex(Long id) {
        //1.从缓存中取信息
        String key=CACHE_SHOP_KEY+id;
        String json = stringRedisTemplate.opsForValue().get(key);
        //2。存在，返回
        if(StrUtil.isNotBlank(json)){
            Shop shop = JSONUtil.toBean(json, Shop.class);
            return shop;
        }
        if (json != null){
            return null;
        }
        //3.不存在，从数据库中读取
        String lockey = LOCK_SHOP_KEY + id;
        //3.1获取互斥锁
        try {
            Boolean ISlock = isLock(lockey);
            if (!ISlock){
                //休眠一会重试
                Thread.sleep(30);
                return queryShopWithMutex(id);
            }

            Shop shop = getById(id);

            if(shop==null){
                stringRedisTemplate.opsForValue().set(key,"",30, TimeUnit.MINUTES);
                return null;
            }
            //4.清除缓存，加入新的缓存
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop),30, TimeUnit.MINUTES);
            //5.返回
            return shop;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            unLock(lockey);
        }
    }


    private static final ExecutorService CACHE_REBUILD_EXECUTOR= Executors.newFixedThreadPool(10);

    public Shop queryShopWithLogicalExpire(Long id) {
        //1.从缓存中取信息
        String key=CACHE_SHOP_KEY+id;
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.不存在，返回null
        if(StrUtil.isBlank(json)){
            return null;
        }
        //存在，查询逻辑过期时间
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        //String shop = (String) redisData.getData();
        Shop shop = JSONUtil.toBean((JSONObject)redisData.getData(), Shop.class);
        LocalDateTime expireTime = redisData.getExpireTime();

        if(expireTime.isAfter(LocalDateTime.now())){
            return shop;
        }
        //3.逻辑已过期，进行缓存重建
        //3.1获取互斥锁
        String lockey = LOCK_SHOP_KEY + id;
        boolean ISlock = isLock(lockey);
        if (ISlock){
            //缓存重建
            CACHE_REBUILD_EXECUTOR.submit(()->{
                try {
                    this.Cache_Rebuild(30L,id);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }finally {
                    unLock(lockey);
                }
            });
        }
        /*String data = stringRedisTemplate.opsForValue().get(CACHE_SHOP_KEY + id);
        redisData = JSONUtil.toBean(data, RedisData.class);
        shop = (Shop)redisData.getData();*/
        return shop;
    }

    public boolean isLock(String key){
        Boolean islock = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(islock);
    }
    public void unLock(String key){
        stringRedisTemplate.delete(key);
    }
    public void Cache_Rebuild(Long time,Long id) throws InterruptedException {
        //查询商铺信息
        Shop shop = getById(id);
        Thread.sleep(200);
        //封装redisdata
        RedisData redisData = new RedisData();
        redisData.setExpireTime(LocalDateTime.now().plusDays(time));
        redisData.setData(shop);
        //写入redis
        stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY+id,JSONUtil.toJsonStr(redisData));
    }

}
