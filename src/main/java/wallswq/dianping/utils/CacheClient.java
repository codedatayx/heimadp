package wallswq.dianping.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jdk.jshell.execution.Util;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


@Data
@Component
public class CacheClient {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    //json->string
    public void set(String key, Object value,Long time) {
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(value),time, TimeUnit.MINUTES);
    }
    public void setWithLogicExpire(String key, Object value,Long time) {
        //设置逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusDays(time));
        //
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(redisData));
    }
   /* public <T> T queryByIdWithPen(String key,Class<T> type,Long time) {
        *//*String s = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(s)) {
            return null;
        }

        stringRedisTemplate.opsForValue().set(key," ",time,TimeUnit.MINUTES);
        return BeanUtil.toBean(s,Object.class);*//*
        return  <T> T;
    }*/

}
