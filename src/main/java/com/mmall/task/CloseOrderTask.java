package com.mmall.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CloseOrderTask {

	@Autowired
	private IOrderService iOrderService;
	//@Scheduled(cron = "0 */1 * * * ?" )//每一分钟（每一分钟的整数倍）
	public void closeOrderTaskV1() {
		log.info("关闭订单定时任务启动");
		int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
		//iOrderService.closeOrder(hour);
		log.info("关闭订单定时任务结束");
	}
	
	
	@Scheduled(cron = "0 */1 * * * ?" )//每一分钟（每一分钟的整数倍）
	public void closeOrderTaskV2() {
		log.info("关闭订单定时任务启动");
		Long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
		//Long setnxResult = RedisShardedPoolUtil.setnx(key, value)
		log.info("关闭订单定时任务结束");
	}
	
	
	
}
