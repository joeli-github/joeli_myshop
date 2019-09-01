package com.mmall.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver{

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.error("{} Exception",request.getRequestURI(),ex);
		ModelAndView modelAndView = new ModelAndView(new MappingJacksonJsonView());
		modelAndView.addObject("status", ResponseCode.ERROR.getCode());
		modelAndView.addObject("msg", "接口异常,详情请查看服务端日志信息");
		modelAndView.addObject("data", ex.toString());
		return modelAndView;
	}

}
