package com.mmall.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtil {

	private final static String COOKIE_DOMAIN = ".happymmall.com";
	private final static String COOKIE_NAME = "mmall_login_token";
	/**
	 * 登录时写入cookie
	 * @param response
	 * @param token
	 */
	public static void writeLoginToken(HttpServletResponse response,String token) {
		Cookie ck =new Cookie(COOKIE_NAME, token);
		ck.setDomain(COOKIE_DOMAIN);
		ck.setPath("/");
		ck.setHttpOnly(true);
		//如果不设置maxage，cookie就不会写入硬盘，而是写在内存。只在当前页面有效
		ck.setMaxAge(60*60*24*365);//设置-1，代表永久 时间单位是秒
		log.info("write cookieName:{},cookieValue:{}", ck.getName(),ck.getValue());
		response.addCookie(ck);
	}
	
	/**
	 * 读取cookie
	 * @param request
	 * @return
	 */
	public static String readLoginToken(HttpServletRequest request) {

		Cookie[] cks = request.getCookies();
		if (cks!= null) {
			for (Cookie cookie : cks) {
				log.info("read cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
				if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
					log.info("return cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
					return cookie.getValue();
					
				}
			}
		}
		return null;
	}
	
	//退出注销cookie
	public static void delLoginToken(HttpServletRequest request,HttpServletResponse response) {
		
		Cookie[] cks = request.getCookies();
		if (cks!=null) {
			for (Cookie cookie : cks) {
				if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
					cookie.setDomain(COOKIE_DOMAIN);
					cookie.setPath("/");
					
					//如果不设置maxage，cookie就不会写入硬盘，而是写在内存。只在当前页面有效
					cookie.setMaxAge(0);//设置-1，代表永久 时间单位是秒
					log.info("del cookieName:{},cookieValue:{}", cookie.getName(),cookie.getValue());
					response.addCookie(cookie);
					return;
				}
			}
		}
	}	
}
