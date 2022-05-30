package com.bxx.loan.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : bu
 * @date : 2022/5/24  20:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "审批借款人的对象")
public class BorrowerDetailVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("认证状态")
    private String status;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("电话")
    private String mobile;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("教育")
    private String education;

    @ApiModelProperty("婚配")
    private String marry;

    @ApiModelProperty("行业")
    private String industry;

    @ApiModelProperty("收入")
    private String income;

    @ApiModelProperty("还款来源")
    private String returnSource;

    @ApiModelProperty("联系人")
    private String contactsName;

    @ApiModelProperty("联系人电话")
    private String contactsMobile;

    @ApiModelProperty("联系人关系")
    private String contactsRelation;

    @ApiModelProperty("图片集合")
    private List<BorrowerAttachVO> borrowerAttachVOList = new ArrayList<>();


}
