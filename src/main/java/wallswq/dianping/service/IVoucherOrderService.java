package wallswq.dianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.VoucherOrder;

public interface IVoucherOrderService extends IService<VoucherOrder> {
    Result newSeckill(Long id);

    Result creatDingdan(Long id);
}
