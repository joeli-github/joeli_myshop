package com.mmall.controller.backend;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {
	
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private ICategoryService iCategoryService;
	
	/**
	 * 添加节点
	 * @param session
	 * @param categoryName
	 * @param parentId
	 * @return
	 */
	@RequestMapping("add_category.do")
	@ResponseBody
	public ServerResponse addCategory(HttpServletRequest httpServletRequest,String categoryName,@RequestParam(value = "parentId", defaultValue = "0")int parentId) {
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请 登录");
//		}
//		//校验是否是管理员
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			//是管理员，增加我们处理分类的逻辑
//			return iCategoryService.addCategory(categoryName, parentId);
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//		}
		return iCategoryService.addCategory(categoryName, parentId);
	}
	
	/**
	 * 更新品类名称
	 * @param session
	 * @param categoryId
	 * @param categoryName
	 * @return
	 */
	@RequestMapping("set_category_name.do")
	@ResponseBody
	public ServerResponse setCategoryName(HttpServletRequest httpServletRequest,Integer categoryId,String categoryName) {
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请 登录");
//		}
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			//更新category
//			return iCategoryService.updateCategory(categoryId, categoryName);
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//		}
		return iCategoryService.updateCategory(categoryId, categoryName);
	}
	
	
	/**
	 * 查询子节点
	 * @param session
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("get_category.do")
	@ResponseBody
	public ServerResponse getChildrenParallelCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId) {
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请 登录");
//		}
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			//查询子节点category信息，并且不递归，保持平级
//			return iCategoryService.getChildrenParallelCategory(categoryId);
//
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//		}
		return iCategoryService.getChildrenParallelCategory(categoryId);

	}
	
//	获取当前categoryId和递归查询子节点Id
	
	@RequestMapping("get_deep_category.do")
	@ResponseBody
	public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId) {
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
//		if (user==null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请 登录");
//		}
//		if (iUserService.checkAdminRole(user).isSuccess()) {
//			//获取当前categoryId和递归查询子节点Id
//			return iCategoryService.selectCategoryChildrenById(categoryId);
//		}else {
//			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//		}
		return iCategoryService.selectCategoryChildrenById(categoryId);
	}
	
	
	
	
	
}
