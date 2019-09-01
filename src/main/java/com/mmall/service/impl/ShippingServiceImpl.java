package com.mmall.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

	@Autowired
	private ShippingMapper shippingMapper;
	
	public ServerResponse<Map<String, Integer>> add(Integer userId,Shipping shipping){
		shipping.setUserId(userId);
		int rowCount=shippingMapper.insert(shipping);
		if (rowCount>0) {
			Map<String,Integer> result = Maps.newHashMap();
			result.put("shippingId", shipping.getId());
			return ServerResponse.createBySuccess("新增地址成功",result);
		}
		return ServerResponse.createByErrorMessage("新增地址失败");
	}
	
	public ServerResponse<String> dle(Integer userId,Integer shippingId) {
		if (shippingId!=null) {
			int rowCount = shippingMapper.deleteShippingByUserIdAndShippingId(userId, shippingId);
			if (rowCount>0) {
				return ServerResponse.createBySuccess("删除地址成功");
			}
			return ServerResponse.createByErrorMessage("删除地址失败");
		}
		return ServerResponse.createByErrorMessage("地址ID输入错误");
	}
	
	public ServerResponse<String> update(Integer userId,Shipping shipping) {
		shipping.setUserId(userId);
		int rowCount = shippingMapper.updateShippingByUserIdAndShippingId(shipping);
		if (rowCount>0) {
			return ServerResponse.createBySuccess("更新地址成功");
		}
		return ServerResponse.createByErrorMessage("更新地址失败");
	}
	
	
	public ServerResponse<Shipping> select(Integer userId,Integer shippingId) {
		Shipping shipping = shippingMapper.selectShippingByUserIdAndShippingId(userId, shippingId);
		if (shipping!=null) {
			return ServerResponse.createBySuccess("查询地址成功",shipping);
		}
		return ServerResponse.createByErrorMessage("查询地址失败");
	}
	
	public ServerResponse<PageInfo<Shipping>> list(Integer userId,int pageNum,int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Shipping> shippingList = shippingMapper.selectShippingList(userId);
		PageInfo<Shipping> pageInfo = new PageInfo<Shipping>(shippingList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
