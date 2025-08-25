package wallswq.dianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.SeckillVoucher;
import wallswq.dianping.domian.po.Voucher;

public interface IVoucherService extends IService<Voucher>{
    Result queryVoucherOfShopId(Long shopId);

    void addKillVoucher(Voucher voucher);
}
