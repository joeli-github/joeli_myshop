package com.mmall.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.mmall.util.RedisShardedPoolUtil;
@Service("IUserService")
public  class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserMapper userMapper;
	@Override
	public ServerResponse<User> login(String username, String password) {
		int resultCount = userMapper.checkUsername(username);
		if (resultCount==0) {
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		//todo md5加密password
		String md5Password=MD5Util.MD5EncodeUtf8(password);
		User user = userMapper.selectLogin(username, md5Password);
		if (user==null) {
			return ServerResponse.createByErrorMessage("密码错误");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess("登录成功", user);
	}
	
	public ServerResponse<String> register(User user) {
		ServerResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}
		validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}
		
		user.setRole(Const.Role.ROLE_CUSTOMER);
		//md5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		 int resultCount = userMapper.insert(user);
		if (resultCount==0) {
		return	ServerResponse.createByErrorMessage("注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");
	}
	
	public ServerResponse<String> checkValid(String str,String type) {
		if (StringUtils.isNotBlank(type)) {
			if (Const.EMAIL.equals(type)) {
				
				int resultCount = userMapper.checkEmail(str);
				if (resultCount>0) {
					return ServerResponse.createByErrorMessage("邮箱名已存在");
				} 
			}
			if (Const.USERNAME.equals(type)) {
				int resultCount = userMapper.checkUsername(str);
				if (resultCount>0) {
					return ServerResponse.createByErrorMessage("用户名已存在");
				} 
			}
		}else {
			return ServerResponse.createByErrorMessage("参数错误");
		}
		return ServerResponse.createBySuccessMessage("校验成功");
	}
	
	public ServerResponse<String> selectQuestion(String username) {
		ServerResponse<String> response = this.checkValid(username, Const.USERNAME);
		if (response.isSuccess()) {
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		String question=userMapper.selectQuestionByUsername(username);
		if (StringUtils.isNotBlank(question)) {
			return ServerResponse.createBySuccess(question);
		}
		return ServerResponse.createByErrorMessage("用户问题获取失败");
	}
	
	public ServerResponse<String> checkAnswer(String username,String question,String answer){
		int resultConut=userMapper.checkAnswer(username, question, answer);
		if (resultConut>0) {
			String forgetToken=UUID.randomUUID().toString();
			//TonkenCache.setKey(TonkenCache.TOKEN_PREFIX+username, forgetToken);
			RedisShardedPoolUtil.setEx(Const.TOKEN_PREFIX+username, 60*60*12, forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMessage("问题的答案是错的");
	}
	
	
	public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
		ServerResponse<String> response = this.checkValid(username, Const.USERNAME);
		if (response.isSuccess()) {
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		//String token = TonkenCache.getKey(TonkenCache.TOKEN_PREFIX+username);
		String token = RedisShardedPoolUtil.get(Const.TOKEN_PREFIX+username);
		if (StringUtils.isBlank(token)) {
			return ServerResponse.createByErrorMessage("token过期或无效");
		}
		if (StringUtils.equals(forgetToken, token)) {
			String md5Password=MD5Util.MD5EncodeUtf8(passwordNew);
			int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
			if (rowCount>0) {
				return ServerResponse.createBySuccessMessage("修改密码成功");
			}
		}else {
			return ServerResponse.createByErrorMessage("token错误，请重新获取token");
		}
		return ServerResponse.createByErrorMessage("修改密码失败");
	}
	
	
	public ServerResponse<String> restPassword (String passwordOld,String passwordNew,User user){
		int rowCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
		if (rowCount==0) {
			return ServerResponse.createByErrorMessage("旧密码输入错误");
		}
		String md5PasswordNew = MD5Util.MD5EncodeUtf8(passwordNew);
		user.setPassword(md5PasswordNew);
		rowCount = userMapper.updateByPrimaryKeySelective(user);
		if (rowCount>0) {
			return ServerResponse.createBySuccessMessage("新密码修改成功");
		}
		return ServerResponse.createByErrorMessage("密码修改失败");
	}
	
	
	
	public ServerResponse<User> updateInformation(User user){
		int resultCount=userMapper.checkEmailByUserId(user.getEmail(), user.getId());
		if (resultCount>0) {
			return ServerResponse.createByErrorMessage("邮箱已存在，请重新输入邮箱");
		}
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setAnswer(user.getAnswer());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setUsername(user.getUsername());
		int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
		if (updateCount>0) {
			return ServerResponse.createBySuccess("更新用户信息成功", updateUser);
		}
		return ServerResponse.createByErrorMessage("更新个人信息失败");
	}
	
	
	public ServerResponse<User> getInformation(Integer userId){
		User user=userMapper.selectByPrimaryKey(userId);
		if (user==null) {
			return ServerResponse.createByErrorMessage("找不到当前用户");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess(user);
		
	}
	
	
	
	
//	backend
	
	public ServerResponse checkAdminRole(User user) {
		if (user!=null && user.getRole()==Const.Role.ROLE_ADMIN) {
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}
	
}
