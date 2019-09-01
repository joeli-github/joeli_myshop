package com.mmall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mmall.pojo.Cart;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    
    Cart selectCartByUserIdAndProductId(@Param("userId")Integer userId,@Param("productId")Integer productId);
    
    List<Cart> selectCartListByUserId(Integer userId);
    
    int selectCartProductCheckdedStatusByUserId(Integer userId);
    
    int deleteCartByUserIdProductIds(@Param("userId")Integer userId,@Param("productList")List<String> productList);
    
    int checkedOrUnCheckedProduct(@Param("userId")Integer userId,@Param("checked")Integer checked,@Param("productId")Integer productId);
    
    int selectCartProductCount(Integer userId);
    
    
}