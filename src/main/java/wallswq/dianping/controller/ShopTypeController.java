package wallswq.dianping.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.ShopType;
import wallswq.dianping.service.IShopTypeService;

import java.util.List;

@Tag(name = "商铺类型")
@Data
@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {

    @Resource
    private IShopTypeService shopTypeService;

    @GetMapping("/list")
    public Result getShopType(){
        List<ShopType> list = shopTypeService.getShopType();
        return Result.ok(list);
    }
}
