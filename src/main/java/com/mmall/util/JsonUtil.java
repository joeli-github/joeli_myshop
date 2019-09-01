package com.mmall.util;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();
	static {
		//序列化设置
		//对象的所有字段全部列入
		objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
		
		//取消默认转换timestamps形式
		objectMapper.configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		//忽略空bean转json的错误
		objectMapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
		
		//所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
		objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
		
		//反序列化
		//忽略 在json字符串中存在，但在Java对象中不存在对应属性的情况，防止错误
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static <T> String objToString(T obj) {
		if (obj==null) {
			return null;
		}
		try {
			return obj instanceof String ? (String) obj:objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.warn("Parse object to String error", e);
			return null;
		}
	}
	
	public static <T> String objToStringPretty(T obj) {
		if (obj==null) {
			return null;
		}
		try {
			return obj instanceof String ? (String) obj:objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			log.warn("Parse object to String error", e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T stringToObj(String str,Class<T> clazz) {
		if (StringUtils.isEmpty(str)||clazz==null) {
			return null;
		}
		try {
			return clazz.equals(String.class)?(T)str:objectMapper.readValue(str, clazz);
		} catch (Exception e) {
			log.warn("Parse String to Object error", e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T stringToObj(String str,TypeReference<T> typeReference) {
		if (StringUtils.isEmpty(str)||typeReference==null) {
			return null;
		}
		try {
			return (T)(typeReference.getType().equals(String.class)?str:objectMapper.readValue(str, typeReference));
		} catch (Exception e) {
			log.warn("Parse String to Object error", e);
			return null;
		}
	}
	
	public static <T> T stringToObj(String str,Class<?> collectionClass,Class<?>... elementClasses) {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
		try {
			return objectMapper.readValue(str, javaType);
		} catch (Exception e) {
			log.warn("Parse String to Object error", e);
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		User u1  = new User();
		u1.setId(1);
		u1.setUsername("joeli1");
		User u2  = new User();
		u2.setId(2);
		u2.setUsername("joeli2"); 
		String user1Json = JsonUtil.objToString(u1);
		String user1JsonPretty = JsonUtil.objToStringPretty(u1);
		log.info("user1Json:{}",user1Json);
		log.info("user1JsonPretty:{}", user1JsonPretty);
		User user = JsonUtil.stringToObj(user1Json, User.class);
		log.info("user:{}",user);
		List<User> userList = Lists.newArrayList();
		userList.add(u1);
		userList.add(u2);
		String userLiStr = JsonUtil.objToStringPretty(userList);
		log.info("==========================");
		log.info("userLiStr:{}",userLiStr);
		@SuppressWarnings("unused")
		List<User> userListObj =JsonUtil.stringToObj(userLiStr, new TypeReference<List<User>>() {
		});
		
		System.out.println("end");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
