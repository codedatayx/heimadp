package wallswq.dianping.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import wallswq.dianping.domian.Result;
import wallswq.dianping.service.IVoucherOrderService;


@Data
@RestController
@RequestMapping(value = "/voucher-order")
@Tag(name = "秒杀券")
public class VoucherOrderController {

    @Resource
    private IVoucherOrderService voucherOrderService;

    @PostMapping("/seckill/{id}")
    public Result newSeckill(@PathVariable(name = "id") Long id){
        return voucherOrderService.newSeckill(id);
    }
}
