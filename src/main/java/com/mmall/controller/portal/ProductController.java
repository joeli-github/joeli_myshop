package com.mmall.controller.portal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;

@Controller
@RequestMapping("/product/")
public class ProductController {

	@Autowired
	private IProductService iProductService;
	
	/**
	 * 前台商品详情
	 * @param productId
	 * @return
	 */
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse<ProductDetailVo> detail(Integer productId){
		return iProductService.getProductDetail(productId);
		
	}
	@RequestMapping(value = "/{productId}",method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<ProductDetailVo> detailRESTful(@PathVariable Integer productId){
		return iProductService.getProductDetail(productId);
		
	}
	/**
	 * 前台产品条件查询
	 * @param keyword
	 * @param categoryId
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
			@RequestParam(value = "categoryId",required = false)Integer categoryId,
			@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
			@RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
			@RequestParam(value = "orderBy",defaultValue = "")String orderBy){
		
		
		return iProductService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
	}
	
	@RequestMapping("/{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}")
	@ResponseBody
	public ServerResponse<PageInfo> listRESTful(@PathVariable(value = "keyword")String keyword,
			@PathVariable(value = "categoryId")Integer categoryId,
			@PathVariable(value = "pageNum")Integer pageNum,
			@PathVariable(value = "pageSize")Integer pageSize,
			@PathVariable(value = "orderBy")String orderBy){
		if (pageNum==null) {
			pageNum=1;
		}
		if (pageSize==null) {
			pageNum=10;
		}
		if (orderBy==null) {
			orderBy="price_asc";
		}
		
		return iProductService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
	}
	
	
	@RequestMapping("/keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}")
	@ResponseBody
	public ServerResponse<PageInfo> listRESTful(@PathVariable(value = "keyword")String keyword,
			@PathVariable(value = "pageNum")Integer pageNum,
			@PathVariable(value = "pageSize")Integer pageSize,
			@PathVariable(value = "orderBy")String orderBy){
		if (pageNum==null) {
			pageNum=1;
		}
		if (pageSize==null) {
			pageNum=10;
		}
		if (orderBy==null) {
			orderBy="price_asc";
		}
		
		return iProductService.getProductByKeywordCategory(keyword, null, pageNum, pageSize, orderBy);
	}
	
	
	@RequestMapping("/categoryId/{categoryId}/{pageNum}/{pageSize}/{orderBy}")
	@ResponseBody
	public ServerResponse<PageInfo> listRESTful(@PathVariable(value = "categoryId")Integer categoryId,
			@PathVariable(value = "pageNum")Integer pageNum,
			@PathVariable(value = "pageSize")Integer pageSize,
			@PathVariable(value = "orderBy")String orderBy){
		if (pageNum==null) {
			pageNum=1;
		}
		if (pageSize==null) {
			pageNum=10;
		}
		if (StringUtils.isBlank(orderBy)) {
			orderBy="price_asc";
		}
		
		return iProductService.getProductByKeywordCategory(null, categoryId, pageNum, pageSize, orderBy);
	}
	
	
}
