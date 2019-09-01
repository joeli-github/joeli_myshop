package com.mmall.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;

@Service("iCartService")
public class CartServiceImpl implements ICartService {

	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private ProductMapper productMapper;
	public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count) {
		if (productId==null || count==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);
		if (cart==null) {
			//如果这个购物车是空，说明需要新增这个产品
			Cart cartNew = new Cart();
			cartNew.setUserId(userId);
			cartNew.setProductId(productId);
			cartNew.setQuantity(count);
			cartNew.setChecked(Const.cart.CHECKED);
			cartMapper.insert(cartNew);
		}else {
			cart.setQuantity(cart.getQuantity()+count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		CartVo cartVo = this.getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}
	
	public ServerResponse<CartVo> updete(Integer userId,Integer productId,Integer count) {
		if (productId==null || count==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);
		if (cart!=null) {
			cart.setQuantity(count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		CartVo cartVo = this.getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}
	
	public ServerResponse<CartVo> deleteProduct(Integer userId,String productIds) {
		List<String> productList = Splitter.on(",").splitToList(productIds);
		if (CollectionUtils.isEmpty(productList)) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		cartMapper.deleteCartByUserIdProductIds(userId, productList);
		CartVo cartVo = this.getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}
	
	public ServerResponse<CartVo> list(Integer userId) {
		CartVo cartVo = this.getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}
	
	public ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer checked,Integer productId) {
		cartMapper.checkedOrUnCheckedProduct(userId, checked,productId);
		return this.list(userId);
		
	}
	
	public ServerResponse<Integer> getCartProductCount(Integer userId) {
		if (userId == null) {
			return ServerResponse.createBySuccess(0);
		}
		Integer count = cartMapper.selectCartProductCount(userId);
		return ServerResponse.createBySuccess(count);
	}
	
	
	
	
	
	
	
	
	
	//购物车高复用方法
	private CartVo getCartVoLimit(Integer userId) {
		
		CartVo cartVo = new CartVo();
		List<Cart> cartList = cartMapper.selectCartListByUserId(userId);
		List<CartProductVo> cartProductVos=Lists.newArrayList();
		BigDecimal cartTotalPrice = new BigDecimal("0");
		
		if (CollectionUtils.isNotEmpty(cartList)) {
			for (Cart cartItem : cartList) {
				CartProductVo cartProductVo = new CartProductVo();
				cartProductVo.setId(cartItem.getId());
				cartProductVo.setUserId(userId);
				cartProductVo.setProductId(cartItem.getProductId());
				
				Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
				if (product!=null) {
					cartProductVo.setProductMainImage(product.getMainImage());
					cartProductVo.setProductName(product.getName());
					cartProductVo.setProductPrice(product.getPrice());
					cartProductVo.setProductStatus(product.getStatus());
					cartProductVo.setProductStock(product.getStock());
					cartProductVo.setProductSubtitle(product.getSubtitle());
					//判断库存
					int buyLimitCount = 0;
					if (product.getStock()>=cartItem.getQuantity()) {
						cartProductVo.setLimitQuantity(Const.cart.LIMIT_NUM_SUCCESS);
						buyLimitCount = cartItem.getQuantity();
					}else {
						cartProductVo.setLimitQuantity(Const.cart.LIMIT_NUM_FAIL);
						buyLimitCount = product.getStock();
						//更新购物车的有效库存
						Cart cartForQuantityCart = new Cart();
						cartForQuantityCart.setId(cartItem.getId());
						cartForQuantityCart.setQuantity(buyLimitCount);
						cartMapper.updateByPrimaryKeySelective(cartForQuantityCart);
					}
					cartProductVo.setQuantity(buyLimitCount);
					//计算总价
					cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
					cartProductVo.setProductChecked(cartItem.getChecked());
				}else {
					cartMapper.deleteByPrimaryKey(cartItem.getId());
					continue;
				}
				if (cartItem.getChecked()==Const.cart.CHECKED) {
					cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
				}
				cartProductVos.add(cartProductVo);
			}
		}
		cartVo.setCartProductVoList(cartProductVos);
		cartVo.setCartProductTotalPrice(cartTotalPrice);
		cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		cartVo.setAllChecked(this.getAllCheckedStatus(userId));
		return cartVo;
	}
	
	
	private boolean getAllCheckedStatus(Integer userId) {
		if (userId==null) {
			return false;
		}
		return cartMapper.selectCartProductCheckdedStatusByUserId(userId)==0;
	}
	
	
	
	
	
	
	
	
}
