package wallswq.dianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wallswq.dianping.domian.po.SeckillVoucher;
import wallswq.dianping.domian.po.Voucher;

public interface ISeckillVoucherService extends IService<SeckillVoucher> {
    void addKillVoucher(Voucher voucher);
}
