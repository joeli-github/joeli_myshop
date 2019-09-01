package com.mmall.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
@Service("iProductService")
public class ProductServiceImpl implements IProductService {
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private ICategoryService iCategoryService;
	
	public ServerResponse<Product> saveOrUpdateProduct(Product product){
		if (product!=null) {
			if (StringUtils.isNoneBlank(product.getSubImages())) {
				String[] subImageArray = product.getSubImages().split(",");
				if (subImageArray.length>0) {
					product.setMainImage(subImageArray[0]);
				}
			}
			if (product.getId()!=null) {
				int rowCount = productMapper.updateByPrimaryKeySelective(product);
				if (rowCount>0) {
					return ServerResponse.createBySuccessMessage("更新商品成功");
				}
				return ServerResponse.createBySuccessMessage("更新商品失败");
			}else {
				int rowCount = productMapper.insert(product);
				if (rowCount>0) {
					return ServerResponse.createBySuccessMessage("新增商品成功");
				}
				return ServerResponse.createBySuccessMessage("新增商品失败");
			}
			
		}
		return ServerResponse.createByErrorMessage("新增或更新商品参数不正确");
	}
	
	public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
		if (productId==null || status==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = new Product();
		product.setId(productId);
		product.setStatus(status);
		int rowCount = productMapper.updateByPrimaryKeySelective(product);
		if (rowCount>0) {
			return ServerResponse.createBySuccessMessage("更新产品销售状态成功");
		}
		return ServerResponse.createByErrorMessage("更新产品销售状态失败");
	}
	
	
	public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
		if (productId==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product==null) {
			return ServerResponse.createByErrorMessage("产品已下架或已删除");
		}
		//vo对象--value object
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);
		return ServerResponse.createBySuccess(productDetailVo);
	}
	
	private ProductDetailVo assembleProductDetailVo(Product product) {
		ProductDetailVo productDetailVo = new ProductDetailVo();
		productDetailVo.setId(product.getId());
		productDetailVo.setCategoryId(product.getCategoryId());
		productDetailVo.setDetail(product.getDetail());
		productDetailVo.setSubImages(product.getSubImages());
		productDetailVo.setSubtitle(product.getSubtitle());
		productDetailVo.setPrice(product.getPrice());
		productDetailVo.setMainImage(product.getMainImage());
		productDetailVo.setName(product.getName());
		productDetailVo.setStatus(product.getStatus());
		
		//imageHost
		productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
		
		//parentCategoryId
		Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
		if (category==null) {
			productDetailVo.setParentCategoryId(0);
		}else {
			productDetailVo.setParentCategoryId(category.getParentId());
		}
		
		//cteateTime
		productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
		productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
		
		return productDetailVo;
		
	}
	
	public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize) {
		//1,startPage--start;2,填充自己的sql查询逻辑；3，pageHelper--收尾
		PageHelper.startPage(pageNum, pageSize);
		List<Product> productList = productMapper.selectList();
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product productItem : productList) {
			ProductListVo productListVo = assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServerResponse.createBySuccess(pageResult);
	}
	
	private ProductListVo assembleProductListVo(Product product) {
		ProductListVo productListVo = new ProductListVo();
		productListVo.setId(product.getId());
		productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
		productListVo.setMainImage(product.getMainImage());
		productListVo.setCategoryId(product.getCategoryId());
		productListVo.setName(product.getName());
		productListVo.setPrice(product.getPrice());
		productListVo.setStatus(product.getStatus());
		productListVo.setSubtitle(product.getSubtitle());
		return productListVo;
		
	}
	
	public ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		if (StringUtils.isNotBlank(productName)) {
			productName = new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
		
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product productItem : productList) {
			ProductListVo productListVo = assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServerResponse.createBySuccess(pageResult);
	}
	
	public ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
		if (productId==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product==null) {
			return ServerResponse.createByErrorMessage("产品已下架或已删除");
		}
		if (product.getStatus()!=Const.ProductStatusEnum.ON_SALE.getCode()) {
			return ServerResponse.createByErrorMessage("产品已下架或已删除");
			
		}
		//vo对象--value object
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);
		return ServerResponse.createBySuccess(productDetailVo);
		
	}
	
	public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
		if (StringUtils.isBlank(keyword) && categoryId==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		List<Integer> categoryIdList = Lists.newArrayList();
		if (categoryId!=null) {
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if (category==null && StringUtils.isBlank(keyword)) {
				PageHelper.startPage(pageNum, pageSize);
				List<ProductListVo> productListVoList = Lists.newArrayList();
				PageInfo pageInfo = new PageInfo<>(productListVoList);
				return ServerResponse.createBySuccess(pageInfo);
			}
			categoryIdList = iCategoryService.selectCategoryChildrenById(categoryId).getData();
		}
		if (StringUtils.isNoneBlank(keyword)) {
			keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
		}
		PageHelper.startPage(pageNum, pageSize);
		//动态排序
		if (StringUtils.isNoneBlank(orderBy)) {
			if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
				String[] orderByArray = orderBy.split("_");
				orderBy = orderByArray[0] + " " +orderByArray[1];
				PageHelper.orderBy(orderBy);
			}
		}
		List<Product> productList = productMapper.selectByNameAndCategoryIdList(StringUtils.isBlank(keyword)?null:keyword, categoryIdList.size()==0?null:categoryIdList);
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product product : productList) {
			ProductListVo productListVo = assembleProductListVo(product);
			productListVoList.add(productListVo);
		}
		PageInfo pageInfo = new PageInfo(productList);
		pageInfo.setList(productListVoList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
