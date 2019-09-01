package com.mmall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mmall.pojo.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
    //二期新增定时关单
    List<Order> selectOrderStatusByCreateTime(@Param("status")Integer status,@Param("date")String date);
    
    int closeOrderByOrderId(Integer id);
    
}