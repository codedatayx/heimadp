package wallswq.dianping.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import wallswq.dianping.domian.po.ShopType;
import wallswq.dianping.mapper.ShopTypeMapper;
import wallswq.dianping.service.IShopTypeService;

import java.util.ArrayList;
import java.util.List;

import static wallswq.dianping.constant.RedisConstant.LOCK_SHOP_TYPE;

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private  StringRedisTemplate stringRedisTemplate;

    @Override
    public List<ShopType>  getShopType() {
        //1.在redis中查询是否有数据
        String KEY = LOCK_SHOP_TYPE;
        List<String> json = stringRedisTemplate.opsForList().range(KEY, 0, -1);
        List<ShopType> shop_type = new ArrayList<>();
        //2.redis有，查找redis
        if(!json.isEmpty()){
            for (String s : json) {
                ShopType shopType = JSONUtil.toBean(s, ShopType.class);
                shop_type.add(shopType);
            }
            return shop_type;
        }
        //3.没有，查询数据库
        shop_type=query().orderByAsc("sort").list();
        if(shop_type.isEmpty()){
            return new ArrayList<>();
        }
        for (ShopType shopType : shop_type) {
            stringRedisTemplate.opsForList().rightPush(KEY, JSONUtil.toJsonStr(shopType));
        }
        return shop_type;
    }
}
