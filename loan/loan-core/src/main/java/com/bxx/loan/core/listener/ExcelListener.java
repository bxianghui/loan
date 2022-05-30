package com.bxx.loan.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.bxx.loan.core.mapper.DictMapper;
import com.bxx.loan.core.pojo.dto.ExcelDTO;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * excel文件读的监听器
 * @author : bu
 * @date : 2022/5/18  19:11
 */
@NoArgsConstructor
public class ExcelListener extends AnalysisEventListener<ExcelDTO> {

    private static final int BATCH_COUNT = 100;

    private List<ExcelDTO> cachedDataList = new ArrayList<>();

    private DictMapper dictMapper;

    public ExcelListener(DictMapper dictMapper){
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(ExcelDTO data, AnalysisContext context) {

        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT){
            saveData();
            cachedDataList.clear();
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    private void saveData() {
        dictMapper.saveList(cachedDataList);
    }
}
