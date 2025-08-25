package wallswq.dianping.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.dto.UserDTO;
import wallswq.dianping.domian.po.Blog;
import wallswq.dianping.service.IBlogService;
import wallswq.dianping.service.IUserService;
import wallswq.dianping.utils.UserHolrder;

import java.util.List;


@Data
@EnableKnife4j
@RestController
@RequestMapping(value = "blog")
public class BlogController {

    @Resource
    private IBlogService blogService;

    @Resource
    private IUserService userService;
    @GetMapping("/hot")
    public Result queryByCurrent(@RequestParam(value = "current",defaultValue = "1") Integer current ){
        return blogService.blogqueryByCurrent(current);
    }

    @GetMapping("/{id}")
    public Result queryById(@PathVariable(name = "id") Long id ){
        return blogService.queryBlogById(id);
    }


    @GetMapping("/of/me")
    public Result queryMe(@RequestParam(value = "current",defaultValue = "1") Integer current ){
        UserDTO user = UserHolrder.getUser();
        Page<Blog> blog = blogService.query().eq("user_id", user.getId())
                .page(new Page<>(current, 5));
        List<Blog> records = blog.getRecords();
        return Result.ok(records);
    }
}
