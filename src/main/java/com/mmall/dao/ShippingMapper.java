package com.mmall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mmall.pojo.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    
    int deleteShippingByUserIdAndShippingId(@Param("userId")Integer userId,@Param("shippingId")Integer shippingId);
    
    int updateShippingByUserIdAndShippingId(Shipping shipping);
    
    Shipping selectShippingByUserIdAndShippingId(@Param("userId")Integer userId,@Param("shippingId")Integer shippingId);
    
    List<Shipping> selectShippingList(Integer userId);
    
    
}