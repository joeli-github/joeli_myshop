package com.mmall.controller.portal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.vo.CartVo;

@Controller
@RequestMapping("/cart/")
public class CartController {
	
	@Autowired
	private ICartService iCartService;
	/**
	 * 查询购物车
	 * @param session
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<CartVo> list(HttpServletRequest httpServletRequest) {
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iCartService.list(user.getId());
	}
	/**
	 * 添加购物车
	 * @param session
	 * @param productId
	 * @param count
	 * @return
	 */
	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse<CartVo> add(HttpServletRequest httpServletRequest,Integer productId,Integer count) {
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iCartService.add(user.getId(), productId, count);
	}
	
	/**
	 * 更新购物车里商品的数量
	 * @param session
	 * @param productId
	 * @param count
	 * @return
	 */
	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse<CartVo> update(HttpServletRequest httpServletRequest,Integer productId,Integer count) {
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iCartService.updete(user.getId(), productId, count);
	}
	
	/**
	 * 删除购物车的商品
	 * @param session
	 * @param productIds
	 * @return
	 */
	@RequestMapping("delete_product.do")
	@ResponseBody
	public ServerResponse<CartVo> deleteProduct(HttpServletRequest httpServletRequest,String productIds) {
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iCartService.deleteProduct(user.getId(), productIds);
	}
	
	
	//全选
	@RequestMapping("select_all.do")
	@ResponseBody
	public ServerResponse<CartVo> selectAll(HttpServletRequest httpServletRequest) {
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(), Const.cart.CHECKED,null);
	}
	//全反选
	@RequestMapping("un_select_all.do")
	@ResponseBody
	public ServerResponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest) {
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(), Const.cart.UN_CHECKED,null);
	}
	//单独选
	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse<CartVo> select(HttpServletRequest httpServletRequest,Integer productId) {
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(), Const.cart.CHECKED,productId);
	}
	//单独反选
	@RequestMapping("un_select.do")
	@ResponseBody
	public ServerResponse<CartVo> unSelect(HttpServletRequest httpServletRequest,Integer productId) {
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(), Const.cart.UN_CHECKED,productId);
	}
	//查询当前用户的购物车里面的产品数量，如果一个产品有10个，那么数量就是10个
	@RequestMapping("get_cart_product_count.do")
	@ResponseBody
	public ServerResponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest) {
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user==null) {
			return ServerResponse.createBySuccess(0);
		}
		return iCartService.getCartProductCount(user.getId());
				
	}
	
	
	
}
