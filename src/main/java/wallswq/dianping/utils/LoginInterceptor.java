package wallswq.dianping.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.modeler.Util;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import wallswq.dianping.domian.dto.UserDTO;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static wallswq.dianping.constant.RedisConstant.LOGIN_CODE;
import static wallswq.dianping.constant.RedisConstant.LOGIN_TOKEN;

public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        if(StrUtil.isBlank(token)){
            return true;
        }
        Map<Object, Object> Entires = stringRedisTemplate.opsForHash().entries(LOGIN_TOKEN + token);
        if (Entires.isEmpty()) {
            return true;
        }
        UserDTO userDTO = BeanUtil.fillBeanWithMap(Entires, new UserDTO(),false);
        stringRedisTemplate.expire(LOGIN_TOKEN+token,36000L,TimeUnit.MINUTES);
        UserHolrder.saveUser(userDTO);
        return true;
    }

}
