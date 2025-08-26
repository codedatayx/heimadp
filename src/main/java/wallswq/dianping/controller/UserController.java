package wallswq.dianping.controller;

import cn.hutool.core.bean.BeanUtil;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.dto.LoginFormDTO;
import wallswq.dianping.domian.dto.UserDTO;
import wallswq.dianping.domian.po.User;
import wallswq.dianping.domian.po.UserInfo;
import wallswq.dianping.service.IUserInfoService;
import wallswq.dianping.service.IUserService;
import wallswq.dianping.utils.UserHolrder;

@RestController
@RequestMapping("/user")
@Data
@EnableKnife4j
@Tag(name = "用户")
public class UserController {
    @Resource
    public IUserService userService;
    @Resource
    public IUserInfoService userInfoService;

    @Operation(summary = "发送验证码")
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        return userService.sendCode(phone,session);
    }
    @Operation(summary = "登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginFormDTO loginFormDTO, HttpSession session) {
        return userService.login(loginFormDTO,session);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result logout(HttpSession session) {
        UserHolrder.removeUser();
        System.out.println("当前线程"+UserHolrder.getUser());
        return Result.ok();
    }

    @Operation(summary = "个人主页")
    @GetMapping("info/{id}")
    public Result Info(@PathVariable("id") Integer id){
        UserInfo info = userInfoService.getById(id);
        if(info == null){
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        return Result.ok(info);
    }

    @Operation(summary = "主页")
    @GetMapping("/me")
    public Result me() {
        UserDTO user = UserHolrder.getUser();
        return Result.ok(user);
    }

    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable("id") Long userId){
        // 查询详情
        User user = userService.getById(userId);
        if (user == null) {
            return Result.ok();
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 返回
        return Result.ok(userDTO);
    }

}
