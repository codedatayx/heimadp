package wallswq.dianping.domian.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EnableKnife4j
@TableName(value = "tb_shop")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Shop implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Long typeId;
    private String images;
    private String area;
    private String address;
    private Double x;
    private Double y;
    private Long avgPrice;
    private Integer sold;
    private Integer comments;
    /**
     * 评分，1~5分，乘10保存，避免小数
     */
    private Integer score;
    /**
     * 营业时间，例如 10:00-22:00
     */
    private String openHours;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Double distance;
}
