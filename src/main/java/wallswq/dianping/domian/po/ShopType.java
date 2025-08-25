package wallswq.dianping.domian.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EnableKnife4j
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_shop_type")
public class ShopType implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id")
    private Long id;
    private String name;
    private String icon;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
