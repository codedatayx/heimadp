package wallswq.dianping.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.Shop;
import wallswq.dianping.service.IShopService;
import wallswq.dianping.service.impl.ShopServiceImpl;


@Data
@EnableKnife4j
@RestController
@RequestMapping(value = "shop")
public class ShopController {

    @Resource
    private IShopService shopService;

    @GetMapping("/of/type")
    public Result queryShopByType(@RequestParam("typeId") Integer typeId,
                                  @RequestParam(value = "current",defaultValue = "1") Integer current) {
        Page<Shop> shop = shopService.query().eq("type_id",typeId).page(
                new Page<>(current, 5)
        );
        return Result.ok(shop.getRecords());
    }

    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        return shopService.queryShopById(id);
    }
}
