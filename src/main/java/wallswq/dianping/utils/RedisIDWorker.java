package wallswq.dianping.utils;


import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Data
@Component
public class RedisIDWorker {
    //1
    private static final long BEGIN_TIMESTAMP=1640995200L;
    @Resource
    private  StringRedisTemplate stringRedisTemplate;

    public long nexID(String keyprefix){
        //计算时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        //
        String format = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long id = stringRedisTemplate.opsForValue().increment("icr:" + keyprefix + ":" + "20220910");
        return timestamp <<32|id;
    }
    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.of(2022,1,1,0,0,0);
        long Second = time.toEpochSecond(ZoneOffset.UTC);
        System.out.println("Second:"+Second);
    }
}
