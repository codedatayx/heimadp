package wallswq.dianping.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.Blog;
import wallswq.dianping.domian.po.User;
import wallswq.dianping.mapper.BlogMapper;
import wallswq.dianping.service.IBlogService;
import wallswq.dianping.service.IUserService;

import java.util.List;
import java.util.Set;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Resource
    private IUserService userService;

    @Override
    public Result blogqueryByCurrent(Integer current) {
        //分页查询
        //根据点赞量来查询
        Page<Blog> page = query().orderByDesc("liked").page(new Page<>(current, 10));
        //查询当前页
        List<Blog> records = page.getRecords();
        //查询用户
        records.forEach(blog -> {
                    this.queryUser(blog);
                }
        );
        return Result.ok(records);
    }
    
    @Override
    public Result queryBlogById(Long id) {
        //查询blog
        Blog blog = getById(id);
        if (blog == null) {
            return Result.fail("博客不存在");
        }
        //
        queryUser(blog);
        return Result.ok(blog);
    }

    public void queryUser(Blog blog) {
        Long id = blog.getUserId();
        User user = userService.getById(id);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }
}
