package com.mmall.controller.backend;

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
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;

@Controller
@RequestMapping("/manage/user/")
public class UserManageController {
	@Autowired
	private IUserService iUserService;
	
	
	@RequestMapping(value = "login.do",method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpServletResponse httpServletResponse,HttpSession session){
		ServerResponse<User> response = iUserService.login(username, password);
		if (response.isSuccess()) {
			User user=response.getData();
			if (user.getRole()==Const.Role.ROLE_ADMIN) {
				//登录的是管理员
				CookieUtil.writeLoginToken(httpServletResponse, session.getId());
				String usetString = JsonUtil.objToString(user);
				RedisShardedPoolUtil.setEx(session.getId(), Const.redisCacheExtime.REDIS_SESSION_EXTIME,usetString);
				return response;
				
			}else {
				return ServerResponse.createByErrorMessage("不是管理员，无法登录");
			}
		}
		return response;
	}
}
