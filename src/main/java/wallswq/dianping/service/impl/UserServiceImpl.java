package wallswq.dianping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import net.bytebuddy.asm.Advice;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.dto.LoginFormDTO;
import wallswq.dianping.domian.dto.UserDTO;
import wallswq.dianping.domian.po.User;
import wallswq.dianping.mapper.UserMapper;
import wallswq.dianping.service.IUserService;
import wallswq.dianping.utils.UserHolrder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static wallswq.dianping.constant.MsgConstant.CODE_ERR;
import static wallswq.dianping.constant.MsgConstant.PHONE_ERR;
import static wallswq.dianping.constant.RedisConstant.LOGIN_CODE;
import static wallswq.dianping.constant.RedisConstant.LOGIN_TOKEN;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService{
    @Resource
    private  StringRedisTemplate stringRedisTemplate;

    @Override
    public Result login(LoginFormDTO loginFormDTO, HttpSession session) {
        String phone = loginFormDTO.getPhone();
        if (phone.isEmpty()){
            return Result.fail(PHONE_ERR);
        }
        String code = loginFormDTO.getCode();
        String cachecode = stringRedisTemplate.opsForValue().get(LOGIN_CODE + phone);
        if(!code.equals(cachecode)){
            return Result.fail(CODE_ERR);
        }
        String token = UUID.randomUUID().toString();
        User user = query().eq("phone", phone).one();
        if(user == null){
            user = creatUser(phone);
        }

        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String,Object> map = BeanUtil.beanToMap(userDTO,new HashMap<>()
                , CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor(
                        (fieldName,fieldValue)->(fieldValue.toString())));
        stringRedisTemplate.opsForHash().putAll(LOGIN_TOKEN + token,map);
        String tokenky = LOGIN_TOKEN + token;
        stringRedisTemplate.expire(tokenky,30, TimeUnit.MINUTES);
        return Result.ok(token);
    }

    @Override
    public Result sendCode(String phone, HttpSession session) {
        if (phone==null||phone.isEmpty()){
            return Result.fail(PHONE_ERR);
        }
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(LOGIN_CODE+phone, code, 5, TimeUnit.MINUTES);
        log.debug("发送验证码成功: "+code);
        return Result.ok();
    }

    public User creatUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName("溪宝最可爱");
        save(user);
        return user;
    }
}
