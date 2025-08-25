package wallswq.dianping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import wallswq.dianping.domian.po.Voucher;

import java.util.List;

public interface VoucherMapper extends BaseMapper<Voucher>{

    List<Voucher> queryVoucherOfShopId(@Param("shopId") Long shopId);
}
