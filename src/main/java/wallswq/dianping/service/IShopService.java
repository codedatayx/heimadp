package wallswq.dianping.service;


import com.baomidou.mybatisplus.extension.service.IService;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.Shop;

import java.security.PublicKey;

public interface IShopService extends IService<Shop> {

   Result queryShopById(Long id);
}
