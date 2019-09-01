package com.mmall.service;

import java.util.List;

import com.mmall.common.ServerResponse;

public interface ICategoryService {
	ServerResponse addCategory(String categoryName,Integer parentId);
	ServerResponse updateCategory(Integer categoryId,String categoryName);
	ServerResponse getChildrenParallelCategory(Integer categoryId);
	ServerResponse<List<Integer>> selectCategoryChildrenById(Integer categoryId);
	
	
	
}
