package com.mmall.controller.common.interceptor;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("preHandle");
		//请求controller中的方法名
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		//解析handlermethod
		String methodName = handlerMethod.getMethod().getName();
		String className = handlerMethod.getBean().getClass().getSimpleName();
		//解析参数，具体的参数key以及value是什么，我们打印日志
		StringBuffer requeStringBuffer = new StringBuffer();
		Map<String,String[]> paramMap = request.getParameterMap();
		Iterator<?> it = paramMap.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String,String> entry = (Map.Entry<String,String>)it.next();
			String mapKey = (String) entry.getKey();
			String mapVaule = StringUtils.EMPTY;
			Object obj = entry.getValue();
			if (obj instanceof String[]) {
				String[] strings = (String[]) obj;
				mapVaule = Arrays.toString(strings);
			}
			requeStringBuffer.append(mapKey).append("=").append(mapVaule);
		}
		if (StringUtils.equals(className, "UserManageController")&&StringUtils.equals(methodName, "login")) {
			log.info("methodName:{},className:{}", methodName,className);
			return true;
		}
		log.info("methodName:{},className:{},requeStringBuffer:{}", methodName,className,requeStringBuffer);
		User user = null;
		String token = CookieUtil.readLoginToken(request);
		if (StringUtils.isNotBlank(token)) {
			String strUser = RedisShardedPoolUtil.get(token);
			user = JsonUtil.stringToObj(strUser, User.class);
		}
		
		if (user==null || (user.getRole()!=Const.Role.ROLE_ADMIN)) {
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if (user==null) {
				if (StringUtils.equals(className, "ProductManageController")&&StringUtils.equals(methodName, "richtextImgUpload")) {
					log.info("methodName:{},className:{}", methodName,className);
					Map<String,Object> resultMap = Maps.newHashMap();
					resultMap.put("success", false);
					resultMap.put("msg", "请登录管理员");
					out.print(resultMap);
				}else {
					out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("拦截器拦截,用户未登录-请登录管理员")));
				}
				
			}else {
				if (StringUtils.equals(className, "ProductManageController")&&StringUtils.equals(methodName, "richtextImgUpload")) {
					log.info("methodName:{},className:{}", methodName,className);
					Map<String,Object> resultMap = Maps.newHashMap();
					resultMap.put("success", false);
					resultMap.put("msg", "权限不足");
					out.print(resultMap);
				}else {
				out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("拦截器拦截,权限不足-请登录管理员")));
				}
			}
			out.flush();
			out.close();
			return false;
		}
		return true;
	}
	

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("postHandle");
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("afterCompletion");
		
	}

}
