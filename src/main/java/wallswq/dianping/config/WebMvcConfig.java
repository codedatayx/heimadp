package wallswq.dianping.config;


import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wallswq.dianping.utils.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(stringRedisTemplate))
                .excludePathPatterns("/code")
                .excludePathPatterns("/login")
                .excludePathPatterns("/shop-type/**")
                .excludePathPatterns("/shop/**")
                .excludePathPatterns("/blog/hot")
                .excludePathPatterns("/voucher/**");

    }

}
