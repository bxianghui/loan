package com.bxx.loan.core.mapper;

import com.bxx.loan.core.pojo.dto.ExcelDTO;
import com.bxx.loan.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface DictMapper extends BaseMapper<Dict> {
    void saveList(@Param("list")List<ExcelDTO> list);

}
