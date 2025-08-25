package wallswq.dianping.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.SeckillVoucher;
import wallswq.dianping.domian.po.Shop;
import wallswq.dianping.domian.po.Voucher;
import wallswq.dianping.service.IVoucherService;

import java.util.List;

@Tag(name = "优惠券管理")
@Data
@Resource
@RestController
@RequestMapping(value = "/voucher")
public class VoucherController {
    @Resource
    private IVoucherService voucherService;

    @GetMapping("/list/{shopId}")
    public Result list(@PathVariable Long shopId) {
        return voucherService.queryVoucherOfShopId(shopId);
    }

    @PostMapping
    public Result addVoucher(@RequestBody Voucher voucher) {
        voucherService.save(voucher);
        return Result.ok();
    }

    @PostMapping("/seckill")
    public Result seckillVoucher(@RequestBody Voucher voucher) {
        voucherService.addKillVoucher(voucher);
        return Result.ok(voucher.getId());
    }
}
