package wallswq.dianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.Follow;

public interface IFollowService extends IService<Follow> {
    Result follow(Long followUserId);

    Result isFollow(Long followUserId, boolean isFollow);

    Result commonFollow(Long id);
}
