package com.mmall.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("iFileService")
public class FileServiceImpl implements IFileService {
	public String upload(MultipartFile file,String path) {
		String fileName = file.getOriginalFilename();
		String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));
		String uploadFileName = UUID.randomUUID().toString()+fileExtensionName;
		log.info("开始上传文件，上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName,path,uploadFileName);
		
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.setWritable(true);
			fileDir.mkdirs();
		}
		File targetFile = new File(path, uploadFileName);
		try {
			//文件上传成功
			file.transferTo(targetFile);
			//上传到ftp服务器
			FTPUtil.uploadFile(Lists.newArrayList(targetFile));
			//上传成功后，删除upload下面的文件
			//targetFile.delete();
		} catch (IllegalStateException | IOException e) {

			log.error("上传文件成功",e);
			return null;
		}
		
		return targetFile.getName();
	}
	 
	
	
}
