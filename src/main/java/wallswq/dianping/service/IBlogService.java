package wallswq.dianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.Blog;

public interface IBlogService extends IService<Blog> {
    Result blogqueryByCurrent(Integer current);

    Result queryBlogById(Long id);
}
