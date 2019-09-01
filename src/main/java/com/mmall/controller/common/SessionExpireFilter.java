package com.mmall.controller.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;

public class SessionExpireFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
		String cookieToken = CookieUtil.readLoginToken(httpServletRequest);
		if (StringUtils.isNotBlank(cookieToken)) {
			String userString = RedisShardedPoolUtil.get(cookieToken);
			User user = JsonUtil.stringToObj(userString, User.class);
			if (user!=null) {
				//如果user不为空。则重置session的时间，即调用expire命令
				RedisShardedPoolUtil.expire(cookieToken, Const.redisCacheExtime.REDIS_SESSION_EXTIME);
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	

}
