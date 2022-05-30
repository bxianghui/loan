package com.bxx.loan.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : bu
 * @date : 2022/5/24  20:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "身份证、房产信息、车辆信息对象")
public class BorrowerAttachVO {

    @ApiModelProperty(value = "图片类型")
    private String imageType;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "图片名称")
    private String imageName;

}
