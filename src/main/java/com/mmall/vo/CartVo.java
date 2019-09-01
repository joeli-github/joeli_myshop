package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {

	private List<CartProductVo> cartProductVoList;
	private BigDecimal cartProductTotalPrice;
	private boolean allChecked;
	private String imageHost;
	
	public List<CartProductVo> getCartProductVoList() {
		return cartProductVoList;
	}
	public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
		this.cartProductVoList = cartProductVoList;
	}
	public BigDecimal getCartProductTotalPrice() {
		return cartProductTotalPrice;
	}
	public void setCartProductTotalPrice(BigDecimal cartProductTotalPrice) {
		this.cartProductTotalPrice = cartProductTotalPrice;
	}
	public boolean isAllChecked() {
		return allChecked;
	}
	public void setAllChecked(boolean allChecked) {
		this.allChecked = allChecked;
	}
	public String getImageHost() {
		return imageHost;
	}
	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}
	
}
