package com.mmall.task;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mmall.common.Const;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CloseOrderTask {

	@Autowired
	private IOrderService iOrderService;
	@PreDestroy
	public void delLock() {
		RedisShardedPoolUtil.del(Const.redisLock.CLOSE_ORDER_TASK_LOCK);
	}
	
	//@Scheduled(cron = "0 */1 * * * ?" )//每一分钟（每一分钟的整数倍）
	public void closeOrderTaskV1() {
		log.info("关闭订单定时任务启动");
		int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
		iOrderService.closeOrder(hour);
		log.info("关闭订单定时任务结束");
	}
	
	
	//@Scheduled(cron = "0 */1 * * * ?" )//每一分钟（每一分钟的整数倍）
	public void closeOrderTaskV2() {
		log.info("关闭订单定时任务启动");
		Long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
		Long setnxResult = RedisShardedPoolUtil.setnx(Const.redisLock.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeOut));
		if (setnxResult!=null && setnxResult.intValue()==1) {
			closeOrder(Const.redisLock.CLOSE_ORDER_TASK_LOCK);
		}else {
			log.info("没有获取到分布式锁:{}",Const.redisLock.CLOSE_ORDER_TASK_LOCK);
		}
		log.info("关闭订单定时任务结束");
	}
	
	@Scheduled(cron = "0 */1 * * * ?" )//每一分钟（每一分钟的整数倍）
	public void closeOrderTaskV3() {
		log.info("关闭订单定时任务启动");
		Long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
		Long setnxResult = RedisShardedPoolUtil.setnx(Const.redisLock.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeOut));
		if (setnxResult!=null && setnxResult.intValue()==1) {
			closeOrder(Const.redisLock.CLOSE_ORDER_TASK_LOCK);
		}else {
			String lockVauleString = RedisShardedPoolUtil.get(Const.redisLock.CLOSE_ORDER_TASK_LOCK);
			if (lockVauleString!=null && System.currentTimeMillis()>Long.parseLong(lockVauleString)) {
				String getSetString = RedisShardedPoolUtil.getSet(Const.redisLock.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeOut));
				if (getSetString == null || (getSetString!=null && StringUtils.equals(lockVauleString, getSetString))) {
					closeOrder(Const.redisLock.CLOSE_ORDER_TASK_LOCK);
				}else {
					log.info("没有获取到分布式锁:{}",Const.redisLock.CLOSE_ORDER_TASK_LOCK);
				}
			}else {
				log.info("没有获取到分布式锁:{}",Const.redisLock.CLOSE_ORDER_TASK_LOCK);
			}
		}
		log.info("关闭订单定时任务结束");
		log.info("=================================");
	}
	
	private void closeOrder(String lockName) {
		RedisShardedPoolUtil.expire(lockName, 50);
		log.info("获取分布式锁:{},ThreadName:{}",lockName,Thread.currentThread().getName());
		int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
		//iOrderService.closeOrder(hour);
		RedisShardedPoolUtil.del(lockName);
		log.info("释放分布式锁:{},ThreadName:{}",lockName,Thread.currentThread().getName());
		
	}
	
}
