package com.bxx.loan.core.mapper;

import com.bxx.loan.core.pojo.entity.BorrowerAttach;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 借款人上传资源表 Mapper 接口
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface BorrowerAttachMapper extends BaseMapper<BorrowerAttach> {

    void deleteByBorrowerId(@Param("borrowerId") Long borrowerId);
}
