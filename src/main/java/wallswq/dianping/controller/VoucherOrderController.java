package wallswq.dianping.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Data
@RestController
@RequestMapping(value = "voucher-order/seckill/15")
@Tag(name = "秒杀券")
public class VoucherOrderController {

}
