package com.mmall.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

public interface IShippingService {

	ServerResponse<Map<String,Integer>> add(Integer userId,Shipping shipping);
	
	ServerResponse<String> dle(Integer userId,Integer shippingId);
	
	ServerResponse<String> update(Integer userId,Shipping shipping);
	
	ServerResponse<Shipping> select(Integer userId,Integer shippingId);
	
	ServerResponse<PageInfo<Shipping>> list(Integer userId,int pageNum,int pageSize);
	
}
