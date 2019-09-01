package com.mmall.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {
	
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private IProductService iProductService;
	
	@Autowired
	private IFileService iFileService;
	
	
	/**
	 * 后台新增或更新产品
	 * @param session
	 * @param product
	 * @return
	 */
	@RequestMapping("save.do")
	@ResponseBody
	public ServerResponse<Product> productSave(HttpServletRequest httpServletRequest,Product product){
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录。请登录管理员");
//		}
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			return iProductService.saveOrUpdateProduct(product);
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作");
//		}
		return iProductService.saveOrUpdateProduct(product);
	}
/**
 * 更新产品的销售状态
 * @param session
 * @param product
 * @return
 */
	@RequestMapping("set_sale_status.do")
	@ResponseBody
	public ServerResponse<String> setSaleStatus(HttpServletRequest httpServletRequest,Integer productId,Integer status){
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录。请登录管理员");
//		}
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			return iProductService.setSaleStatus(productId, status);
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作");
//		}
		return iProductService.setSaleStatus(productId, status);
	}
	
	/**
	 * 获取产品详情
	 * @param session
	 * @param productId
	 * @return
	 */
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse getDetail(HttpServletRequest httpServletRequest,Integer productId){
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录。请登录管理员");
//		}
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			
//			return iProductService.manageProductDetail(productId);
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作");
//		}
		return iProductService.manageProductDetail(productId);
	}
	
	/**
	 * 后台产品列表动态分页功能
	 * @param session
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse getList(HttpServletRequest httpServletRequest,@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,@RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录。请登录管理员");
//		}
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			
//			return iProductService.getProductList(pageNum, pageSize);
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作");
//		}
		return iProductService.getProductList(pageNum, pageSize);
	}
	
	/**
	 * 根据产品名称，Id进行搜索查询
	 * @param session
	 * @param productName
	 * @param productId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("search.do")
	@ResponseBody
	public ServerResponse productSearch(HttpServletRequest httpServletRequest,String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,@RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录。请登录管理员");
//		}
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			
//			return iProductService.searchProduct(productName, productId, pageNum, pageSize);
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作");
//		}
		return iProductService.searchProduct(productName, productId, pageNum, pageSize);
	}
	
	/**
	 * ftp文件上传
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("upload.do")
	@ResponseBody
	public ServerResponse<Map<String, String>> upload(HttpServletRequest httpServletRequest,@RequestParam(value = "upload_file",required = false)MultipartFile file) {
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录。请登录管理员");
//		}
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			String path =httpServletRequest.getSession().getServletContext().getRealPath("upload");
//			String targetFileName = "upload/"+iFileService.upload(file, path);
//			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
//			Map<String,String> fileMap = Maps.newHashMap();
//			fileMap.put("uri", targetFileName);
//			fileMap.put("url", url);
//			return ServerResponse.createBySuccess(fileMap);
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作");
//		}
		String path =httpServletRequest.getSession().getServletContext().getRealPath("upload");
		String targetFileName = "upload/"+iFileService.upload(file, path);
		String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
		Map<String,String> fileMap = Maps.newHashMap();
		fileMap.put("uri", targetFileName);
		fileMap.put("url", url);
		return ServerResponse.createBySuccess(fileMap);
	}
	/**
	 * 富文本上传文件
	 * @param session
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("richtext_img_upload.do")
	@ResponseBody
	public Map<String,Object> richtextImgUpload(HttpSession session,@RequestParam(value = "upload_file",required = false)MultipartFile file,HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> resultMap = Maps.newHashMap();
//		String cookieToken = CookieUtil.readLoginToken(request);
//		if (StringUtils.isBlank(cookieToken)) {
//			resultMap.put("success", false);
//			resultMap.put("msg", "请登录管理员");
//			return resultMap;
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			resultMap.put("success", false);
//			resultMap.put("msg", "请登录管理员");
//			return resultMap;
//		}
//		//富文本中要求返回值要有自己的格式，使用simditor所以按照simditor的要求进行返回
//		
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			String path =request.getSession().getServletContext().getRealPath("upload");
//			String targetFileName ="upload/"+ iFileService.upload(file, path);
//			if (StringUtils.isBlank(targetFileName)) {
//				resultMap.put("success", false);
//				resultMap.put("msg", "上传失败");
//				return resultMap;
//			}
//			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
//			resultMap.put("success", true);
//			resultMap.put("msg", "上传成功");
//			resultMap.put("file_path", url);
//			response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
//			return resultMap;
//		}else {
//			resultMap.put("success", false);
//			resultMap.put("msg", "无权限操作");
//			return resultMap;
//		}
		String path =request.getSession().getServletContext().getRealPath("upload");
		String targetFileName ="upload/"+ iFileService.upload(file, path);
		if (StringUtils.isBlank(targetFileName)) {
			resultMap.put("success", false);
			resultMap.put("msg", "上传失败");
			return resultMap;
		}
		String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
		resultMap.put("success", true);
		resultMap.put("msg", "上传成功");
		resultMap.put("file_path", url);
		response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
		return resultMap;
	}
	
	
	
	
	
	
	
}
