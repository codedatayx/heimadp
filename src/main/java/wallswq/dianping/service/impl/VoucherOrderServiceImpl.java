package wallswq.dianping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wallswq.dianping.domian.Result;
import wallswq.dianping.domian.po.SeckillVoucher;
import wallswq.dianping.domian.po.VoucherOrder;
import wallswq.dianping.mapper.VoucherOrderMapper;
import wallswq.dianping.service.ISeckillVoucherService;
import wallswq.dianping.service.IUserService;
import wallswq.dianping.service.IVoucherOrderService;
import wallswq.dianping.utils.RedisIDWorker;
import wallswq.dianping.utils.UserHolrder;

import java.time.LocalDateTime;

@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {
    @Resource
    private RedisIDWorker redisIDWorker;
    @Resource
    ISeckillVoucherService seckillVoucherService;
    @Override
    public Result newSeckill(Long id) {
        //查询优惠券信息
        SeckillVoucher Voucher = seckillVoucherService.getById(id);
        //判断优惠活动是否开始
        if(LocalDateTime.now().isBefore(Voucher.getCreateTime())){
            return Result.fail("活动还未开始");
        }
        else if(LocalDateTime.now().isAfter(Voucher.getEndTime())){
            return Result.fail("活动已经结束啦");
        }//判断库存
        if(Voucher.getStock()<1){
            return Result.fail("库存不足");
        }
        Long userId = UserHolrder.getUser().getId();
        synchronized (userId.toString().intern()){
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.creatDingdan(id);
        }
    }

    @Transactional
    public Result creatDingdan(Long id) {
        Long userId = UserHolrder.getUser().getId();
        Long count = query().eq("user_Id", userId).eq("voucher_Id", id).count();
        //扣减库存
        if(count > 0){
            return Result.fail("用户已经购买过了");
        }
        boolean isSuccess = seckillVoucherService.update().setSql("stock=stock-1")
                .eq("voucher_id", id)
                .gt("stock",0).update();
        if(!isSuccess){
            return Result.fail("订单不足");
        }
        //创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setUserId(userId);
        voucherOrder.setId(redisIDWorker.nexID("order"));
        voucherOrder.setVoucherId(id);
        save(voucherOrder);
        return Result.ok(id);
    }
}
