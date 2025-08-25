package wallswq.dianping.domian.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EnableKnife4j
@EqualsAndHashCode(callSuper = false)
@TableName("tb_user_info")
@Accessors(chain = true)
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id",type = IdType.AUTO)
    private Long user_id;
    private String city;
    private String introduce;
    private Integer fans;
    private Integer followee;
    private Boolean gender;
    private LocalDate birthday;
    private Integer credits;
    private Boolean level;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
