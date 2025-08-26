package wallswq.dianping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.dto.UserDTO;
import wallswq.dianping.domian.po.Blog;
import wallswq.dianping.domian.po.User;
import wallswq.dianping.mapper.BlogMapper;
import wallswq.dianping.service.IBlogService;
import wallswq.dianping.service.IUserService;
import wallswq.dianping.utils.UserHolrder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static wallswq.dianping.constant.RedisConstant.BLOG_LIKE_KEY;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Resource
    private IUserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

    @Override
    public Result IsLike(Long id) {
        //1.获取登录用户
        Long user = UserHolrder.getUser().getId();
        //2
        String key = BLOG_LIKE_KEY+id;
        Double islike = stringRedisTemplate.opsForZSet().score(key, user.toString());
        //3判断是否点赞
        if (islike == null) {
            boolean Success = update().setSql("liked=liked+1").eq("id", id).update();
            if (Success){
                stringRedisTemplate.opsForZSet().add(key,user.toString(),System.currentTimeMillis() );
            }
        }else {
            boolean success = update().setSql("liked=liked-1").eq("id", id).update();
            if (success){
                stringRedisTemplate.opsForZSet().remove(key,user.toString());
            }
        }
        return Result.ok();
    }

    @Override
    public Result queryLikes(Long id) {
        //查询top5用户
        String key = BLOG_LIKE_KEY + id;
        Set<String> top = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        //解析用户id
        List<Long> ids = top.stream().map(Long::valueOf).collect(Collectors.toList());
        String idStr = StrUtil.join(",", ids);

        if (idStr == null){
            return Result.ok(Collections.emptyList());
        }
        //返回用户id
        List<UserDTO> idss = userService.query()
                .in("id", ids).last("order by field(id, " + idStr + ")").list().stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .toList();
        return Result.ok(ids);
    }

    public void queryUser(Blog blog) {
        Long id = blog.getUserId();
        User user = userService.getById(id);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }
}
