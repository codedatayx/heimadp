package wallswq.dianping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.dto.UserDTO;
import wallswq.dianping.domian.po.Follow;
import wallswq.dianping.mapper.FollowMapper;
import wallswq.dianping.service.IFollowService;
import wallswq.dianping.service.IUserService;
import wallswq.dianping.utils.UserHolrder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static wallswq.dianping.constant.RedisConstant.FOLLOW_USER_KEY;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    private final StringRedisTemplate stringRedisTemplate;

    public FollowServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Resource
    private IUserService userService;

    @Override
    public Result follow(Long followUserId) {
        //1,查询用户id
        Long userId = UserHolrder.getUser().getId();
        //查询是否关注 select count(*) from tb_follow where user_id = ? and follow_user_id = ?
        Long count = query().eq("follow_user_id", followUserId)
                .eq("user_id", userId).count();
        return Result.ok(count > 0);
    }

    @Override
    public Result isFollow(Long followUserId, boolean isFollow) {
        //1,查询用户id
        Long userId = UserHolrder.getUser().getId();
        String key = FOLLOW_USER_KEY + userId;
        //2.查询是否关注
        if (isFollow){
            Follow follow = new Follow();
            follow.setFollowUserId(followUserId);
            follow.setUserId(userId);
            boolean save = save(follow);
            if (save){
                stringRedisTemplate.opsForSet().add(key, followUserId.toString());
            }
        }else {
            boolean remove = remove(new QueryWrapper<Follow>()
                    .eq("follow_user_id", followUserId)
                    .eq("user_id", userId));
            if (remove){
                stringRedisTemplate.opsForSet().remove(key, followUserId.toString());
            }

        }
        return Result.ok();
    }

    @Override
    public Result commonFollow(Long id) {
        //查询用户
        Long userId = UserHolrder.getUser().getId();
        String key1 = FOLLOW_USER_KEY + id;
        String key2 = FOLLOW_USER_KEY + userId;
        //求交集
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key1, key2);
        if (intersect.size() == 0 || intersect.isEmpty()){
            return Result.ok(Collections.emptyList());
        }
        //解析数据
        List<Long> list = intersect.stream().map(Long::valueOf).toList();
        //返回用户
        List<UserDTO> userDTOList = userService.listByIds(list).stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());
        return Result.ok(userDTOList);
    }
}
