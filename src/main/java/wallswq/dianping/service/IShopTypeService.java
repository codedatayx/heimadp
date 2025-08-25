package wallswq.dianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.ShopType;

import java.util.List;

public interface IShopTypeService extends IService<ShopType> {
    List<ShopType>  getShopType();
}
