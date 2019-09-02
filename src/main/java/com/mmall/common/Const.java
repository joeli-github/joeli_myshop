package com.mmall.common;

import java.util.Set;

import com.google.common.collect.Sets;


public class Const {
	public static final String CURRENT_USER="currentUser";
	public static final String EMAIL="email";
	public static final String USERNAME="username";
	public static final String TOKEN_PREFIX = "token_";
	
	public interface ProductListOrderBy{
		Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc","price_desc");
	}
	public interface redisCacheExtime{
		int REDIS_SESSION_EXTIME = 60*30;//30分钟
	}
	public interface cart{
		int CHECKED = 1;//已购选
		int UN_CHECKED = 0;//未勾选
		String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
		String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
		
	}
	public interface Role{
		int ROLE_CUSTOMER = 0;//普通用户
		int ROLE_ADMIN = 1;//管理员
		
	}
	 
	public enum ProductStatusEnum {
		ON_SALE(1,"在线");
		private String value;
		private int code;
		ProductStatusEnum(int code,String value){
			this.code=code;
			this.value=value;
		}
		public String getValue() {
			return value;
		}
		public int getCode() {
			return code;
		}
		
	}
    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");


        OrderStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }
    }
	
	public interface redisLock{
		String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";//关闭订单的分布式锁
	}
}
