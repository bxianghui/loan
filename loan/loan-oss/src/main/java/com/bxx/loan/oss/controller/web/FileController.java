package com.bxx.loan.oss.controller.web;

import com.bxx.loan.common.exception.BusinessException;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author : bu
 * @date : 2022/5/23  11:57
 */
//@CrossOrigin
@RestController
@Api(tags = "阿里云OSS")
@RequestMapping("/web/oss/file")
public class FileController {

    @Resource
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result upload(
            @ApiParam(value = "上传的文件", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块", required = true, example = "avatar")
            @RequestParam("module") String module
    ){
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String url = fileService.upload(inputStream, module, originalFilename);
            return Result.success().message("文件上传成功").data(url);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, e);
        }
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/remove")
    public Result remove(
            @ApiParam(value = "文件的url", required = true)
            @RequestParam String url){
        fileService.remove(url);
        return Result.success().message("删除成功");
    }

}
