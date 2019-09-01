package com.mmall.controller.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

/**
 * @author 94420
 *
 */
@Controller
@RequestMapping(value = "/user/springsession/")
public class UserSpringSessionController {
	
	@Autowired
	private IUserService iUserService; 
/**
 * 1用户登录
 * @param name
 * @param password 
 * @param session
 * @return
 */
	@RequestMapping(value = "login.do",method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session,HttpServletResponse httpServletResponse ) {
		//测试全局异常
		//int i = 0;
		//int m=666/i;
		
		ServerResponse<User> response=iUserService.login(username, password);
		if (response.isSuccess()) {
//			CookieUtil.writeLoginToken(httpServletResponse, session.getId());
//			RedisShardedPoolUtil.setEx(session.getId(), Const.redisCacheExtime.REDIS_SESSION_EXTIME, JsonUtil.objToString(response.getData()));
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	
/**
 * 2用户登出	
 * @param session
 * @return
 */
	@RequestMapping(value = "logout.do",method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> logout(HttpSession session,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
//		String tokenString = CookieUtil.readLoginToken(httpServletRequest);
//		CookieUtil.delLoginToken(httpServletRequest, httpServletResponse);
//		RedisShardedPoolUtil.del(tokenString);
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}

	
	/**
	 * 获取登录信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session,HttpServletRequest httpServletRequest){
//		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
//		if (StringUtils.isBlank(cookieToken)) {
//			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
//		}
//		String userString = RedisShardedPoolUtil.get(cookieToken);
//		User user= JsonUtil.stringToObj(userString, User.class);
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if (user !=null) {
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
	}

	
	
	
}
