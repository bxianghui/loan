package com.bxx.loan.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxx.loan.core.enums.DictEnum;
import com.bxx.loan.core.listener.ExcelListener;
import com.bxx.loan.core.mapper.DictMapper;
import com.bxx.loan.core.pojo.dto.ExcelDTO;
import com.bxx.loan.core.pojo.entity.Dict;
import com.bxx.loan.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取dict表格 父节点
     *
     * @param id
     * @return
     */
    @Override
    public List<Dict> listSelectByParentId(Long id) {

        //1.查看redis中是否存在
        List<Dict> dictList = null;
        try {
            dictList = (List<Dict>) redisTemplate.opsForValue().get("loan:core:dict:" + id);
            //2.存在直接返回
            if (dictList != null) {
                return dictList;
            }
        } catch (Exception e) {
            //如果出现异常的话，不中断，继续执行下方，获取数据库数据
            log.error("redis服务器故障:" + ExceptionUtils.getStackTrace(e));
        }
        //3.不存在，数据库中读取
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        dictList = baseMapper.selectList(queryWrapper);
        //填充hasChildren字段
        dictList.forEach(dict -> {
            //判断当前父节点有无子节点
            dict.setHasChildren(hasChildren(dict.getId()));
        });

        try {
            //4.存入redis中
            log.info("将数据存入redis");
            redisTemplate.opsForValue().set("loan:core:dict:" + id, dictList, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis服务器故障:" + ExceptionUtils.getStackTrace(e));
        }

        return dictList;
    }

    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    /**
     * 读入excel文件
     *
     * @param inputStream
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void readExcel(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDTO.class, new ExcelListener(baseMapper)).sheet().doRead();
    }

    /**
     * 为了写入excel文件，读出数据库的列表信息
     *
     * @return
     */
    @Override
    public List<ExcelDTO> listDictData() {
        List<Dict> dicts = baseMapper.selectList(null);
        List<ExcelDTO> list = new ArrayList<>(dicts.size());
        dicts.forEach(dict -> {
            ExcelDTO excelDTO = new ExcelDTO();
            BeanUtils.copyProperties(dict, excelDTO);
            list.add(excelDTO);
        });
        return list;
    }

    /**
     * 通过code_dict 将integer 转换为 string
     *
     * @param code
     * @param id
     * @return
     */
    @Override
    public String getDictString(String code, Integer id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code", code);
        Dict dict = baseMapper.selectOne(queryWrapper);
        queryWrapper.clear();
        queryWrapper.eq("parent_id", dict.getId()).eq("value", id);
        dict = baseMapper.selectOne(queryWrapper);
        if (dict == null) {
            return null;
        }
        return dict.getName();
    }

    /**
     * 获取数据字典每个分类的列表
     * @param dictCode
     * @return
     */
    @Override
    public List<Dict> getDataList(String dictCode) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").eq("dict_code", dictCode);
        List<Object> objects = baseMapper.selectObjs(queryWrapper);
        Long parentId = (Long) objects.get(0);
        List<Dict> dictList = listSelectByParentId(parentId);
        return dictList;
    }

}
