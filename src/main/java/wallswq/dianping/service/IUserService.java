package wallswq.dianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.dto.LoginFormDTO;
import wallswq.dianping.domian.po.User;

public interface IUserService extends IService<User> {
    Result login(LoginFormDTO loginFormDTO, HttpSession session);

    Result sendCode(String phone, HttpSession session);
}
