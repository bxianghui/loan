package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.dto.ExcelDTO;
import com.bxx.loan.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface DictService extends IService<Dict> {

    List<Dict> listSelectByParentId(Long id);

    void readExcel(InputStream inputStream);

    List<ExcelDTO> listDictData();

    String getDictString(String code, Integer id);

    List<Dict> getDataList(String dictCode);
}
