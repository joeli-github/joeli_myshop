package com.mmall.controller.portal;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

	@Autowired
	private IShippingService iShippingService;
	
	/**
	 * 添加地址
	 * @param session
	 * @param shipping
	 * @return
	 */
	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse<Map<String, Integer>> add(HttpServletRequest httpServletRequest,Shipping shipping){
		
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iShippingService.add(user.getId(), shipping);
	}
	
	/**
	 * 删除地址
	 * @param session
	 * @param shippingId
	 * @return
	 */
	@RequestMapping("del.do")
	@ResponseBody
	public ServerResponse<String> del(HttpServletRequest httpServletRequest,Integer shippingId){
		
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iShippingService.dle(user.getId(), shippingId);
	}
	
	/**
	 * 更新地址
	 * @param session
	 * @param shipping
	 * @return
	 */
	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse<String> update(HttpServletRequest httpServletRequest,Shipping shipping){
		
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iShippingService.update(user.getId(), shipping);
	}
	
	/**
	 * 查看某个地址
	 * @param session
	 * @param shippingId
	 * @return
	 */
	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse<Shipping> select(HttpServletRequest httpServletRequest,Integer shippingId){
		
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iShippingService.select(user.getId(), shippingId);
	}
	
	/**
	 * 该用户地址LIST查询
	 * @param session
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo<Shipping>> list(HttpServletRequest httpServletRequest,@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
		
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iShippingService.list(user.getId(),pageNum,pageSize);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
