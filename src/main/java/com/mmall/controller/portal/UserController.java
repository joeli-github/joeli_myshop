package com.mmall.controller.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;

/**
 * @author 94420
 *
 */
@Controller
@RequestMapping(value = "/user/",method = RequestMethod.POST)
public class UserController {
	
	@Autowired
	private IUserService iUserService; 
/**
 * 1用户登录
 * @param name
 * @param password 
 * @param session
 * @return
 */
	@RequestMapping(value = "login.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session,HttpServletResponse httpServletResponse ) {
		ServerResponse<User> response=iUserService.login(username, password);
		if (response.isSuccess()) {
			CookieUtil.writeLoginToken(httpServletResponse, session.getId());
			RedisShardedPoolUtil.setEx(session.getId(), Const.redisCacheExtime.REDIS_SESSION_EXTIME, JsonUtil.objToString(response.getData()));
		}
		return response;
	}
	
/**
 * 2用户登出	
 * @param session
 * @return
 */
	@RequestMapping(value = "logout.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> logout(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
		String tokenString = CookieUtil.readLoginToken(httpServletRequest);
		CookieUtil.delLoginToken(httpServletRequest, httpServletResponse);
		RedisShardedPoolUtil.del(tokenString);
		return ServerResponse.createBySuccess();
	}
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "register.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> register(User user){
		return iUserService.register(user);
	}
	
	/**
	 * 验证用户名和邮箱是否重名
	 * @param str
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type){
		return iUserService.checkValid(str, type);
	}
	
	/**
	 * 获取登录信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpServletRequest httpServletRequest){
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user= JsonUtil.stringToObj(userString, User.class);
		if (user !=null) {
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
	}
		
	//忘记密码
	/**
	 * 获取用户问题
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		
			return iUserService.selectQuestion(username);
	}
	/**
	 * 验证用户问题答案
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
		
		return iUserService.checkAnswer(username, question, answer);
	}
	
	/**
	 * 忘记密码重置密码+token
	 * @param username
	 * @param passwordNew
	 * @param forgrtToken
	 * @return
	 */
	@RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
		return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
	}
	
	/**
	 * 登录状态修改密码
	 * @param session
	 * @param passwordOld
	 * @param passwordNew
	 * @return
	 */
	@RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> resetPassword(HttpServletRequest httpServletRequest,String passwordOld,String passwordNew ){
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User user = JsonUtil.stringToObj(userString, User.class);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		return iUserService.restPassword(passwordOld, passwordNew, user);
	}
	/**
	 * 更新用户信息
	 * @param session
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "update_information.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> update_information(HttpServletRequest httpServletRequest,User user){
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User currentUser= JsonUtil.stringToObj(userString, User.class);
		if (currentUser == null) {
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		ServerResponse<User> response=iUserService.updateInformation(user);
		if (response.isSuccess()) {
			response.getData().setUsername(currentUser.getUsername());
			//session.setAttribute(Const.CURRENT_USER, response.getData());
			String userString2 = JsonUtil.objToString(response.getData());
			RedisShardedPoolUtil.setEx(cookieToken, Const.redisCacheExtime.REDIS_SESSION_EXTIME, userString2);
		}
		
		return response;
	}
	
	/**
	 * 获取用户的详细信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "get_information.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> get_information(HttpServletRequest httpServletRequest){
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isBlank(cookieToken)) {
			return ServerResponse.createByErrorMessage("用户未登录，无法获取登录信息");
		}
		String userString = RedisShardedPoolUtil.get(cookieToken);
		User currentUser= JsonUtil.stringToObj(userString, User.class);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
		}
		return iUserService.getInformation(currentUser.getId());
	}
	
	
	
	
	
	
	
}
