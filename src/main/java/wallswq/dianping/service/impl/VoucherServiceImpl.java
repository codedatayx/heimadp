package wallswq.dianping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.SeckillVoucher;
import wallswq.dianping.domian.po.Voucher;
import wallswq.dianping.mapper.VoucherMapper;
import wallswq.dianping.service.IVoucherService;

import java.util.List;

@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements IVoucherService {

    @Resource
    private SeckillVoucherServiceImpl seckillVoucherService;

    @Override
    public Result queryVoucherOfShopId(Long shopId) {
        List<Voucher> vouchers = getBaseMapper().queryVoucherOfShopId(shopId);
        return Result.ok(vouchers);
    }

    @Override
    public void addKillVoucher(Voucher voucher) {
        save(voucher);
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);
    }
}
