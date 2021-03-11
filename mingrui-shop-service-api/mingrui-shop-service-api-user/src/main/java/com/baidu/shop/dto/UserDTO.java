package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 2 * @ClassName UserDTO
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/10
 * 6 * @Version V1.0
 * 7
 **/
@Data
@ApiModel(value = "用户DTO")
public class UserDTO {

    @ApiModelProperty(value = "用户主键",example = "1")
    @NotNull(message = "主键不能为空", groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value = "账户")
                @NotNull(message = "账户不能为空", groups = {MingruiOperation.add.class})
    private String username;

    @ApiModelProperty(value = "密码")
    @NotNull(message = "密码不能为空", groups = {MingruiOperation.add.class})
    private String password;

    @ApiModelProperty(value = "手机号")
    @NotNull(message = "手机号不能为空", groups = {MingruiOperation.add.class})
    private String phone;

    private Date created;

    private String salt;

}
