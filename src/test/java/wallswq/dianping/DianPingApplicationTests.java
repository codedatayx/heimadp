package wallswq.dianping;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import wallswq.dianping.service.impl.ShopServiceImpl;

@SpringBootTest
class DianPingApplicationTests {
    @Resource
    ShopServiceImpl shopService;
    @Test
    void Cache_ReBuild() throws InterruptedException {
        shopService.Cache_Rebuild(10L,1L);
        shopService.Cache_Rebuild(10L,2L);
        shopService.Cache_Rebuild(10L,3L);
        shopService.Cache_Rebuild(10L,4L);
    }

    @Test
    void contextLoads() {
    }

}
