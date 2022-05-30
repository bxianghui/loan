package com.bxx.loan.core.controller.admin;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.excel.EasyExcel;
import com.bxx.loan.common.exception.BusinessException;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.dto.ExcelDTO;
import com.bxx.loan.core.pojo.entity.Dict;
import com.bxx.loan.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "数据字典管理")
@RestController
@RequestMapping("/admin/core/dict")
public class AdminDictController {

    @Resource
    private DictService dictService;

    @ApiOperation(value = "通过上下级标签查询列表")
    @GetMapping(value = "/listByParentId/{parentId}")
    public Result listByParentId(
            @ApiParam(value = "分支id(1,为父分支)", required = true, example = "1")
            @PathVariable("parentId") Long parentId
    ){
        List<Dict> list = dictService.listSelectByParentId(parentId);
        System.out.println(list);
        return Result.success().data(list);
    }

    @ApiOperation(value = "导入excel文件")
    @PostMapping("/import")
    public Result importExcel(
            @ApiParam(value = "excel文件", required = true)
            @RequestParam("file")MultipartFile file
            ){
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            dictService.readExcel(inputStream);
            return Result.success().message("数据批量上传成功");
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR);
        }
    }

    @ApiOperation(value = "导出excel文件")
    @GetMapping("export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("mydict", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ExcelDTO.class).sheet().doWrite(dictService.listDictData());
    }
}

