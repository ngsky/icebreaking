package com.ngsky.ice.rest.common;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public abstract class Task implements Runnable {
	
	/**
	 * 任务名称
	 */
	protected String name ;

	/**
	 * 任务描述
	 */
	protected String description ;

	/**
	 * 任务是否顺利完成
	 */
	protected boolean success = true;

	/**
	 * 是否需要重试
	 */
	protected boolean retry = false ;

	/**
	 * 允许重试次数
	 */
	protected int retryTimes = 0 ;

	/**
	 * 重试次数序号, 1、2、3...
	 */
	protected int retryIndex = 0;

	/**
	 * 失败等待时间
	 */
	protected int retryDelay = 0;

	@Override
	public void run() {
		log.info("&&&&&&&&&&&&&&&&&&&& TASK:{} START &&&&&&&&&&&&&&&&&&&&", getName());
		long beginTime = System.currentTimeMillis();
		try {
			doWork();
		} catch (Exception e) {
			log.error("任务:{} 异常:{}", getName(), e);
			markResult(false, true);
		}
		long endTime = System.currentTimeMillis();
		log.info("任务 \"{}\" 结束，success：{}， 耗时：{} 秒；{}", getName(), isSuccess(),(endTime-beginTime)/1000, getDescription());
		
		if(retryTimes <= 0)
			return;
		//如果任务失败，重试
		for (retryIndex = 1; !isSuccess() && retry && retryIndex <= retryTimes; retryIndex++) {
			if (retryDelay > 0) {
				try {
					Thread.sleep(retryDelay * 1000);
				} catch (InterruptedException e) {

				}
			}
			log.info("任务:{}, 第{}次重试 开始", getName(), retryIndex);
			beginTime = System.currentTimeMillis();
			try {
				doWork();
				markResult(true, false);
			} catch (Exception e) {
				log.error("任务:{}, 第{}次重试 异常； {}", getName(), retryIndex, e);
				markResult(false, true);
			}
			endTime = System.currentTimeMillis();
			log.info("任务:{}, 第{}次重试  结束，success：{}， 耗时：{} 秒；{}", getName(), retryIndex,isSuccess(), (endTime-beginTime)/1000, getDescription());
		}
	}
	
	protected abstract void doWork() throws Exception;

	public abstract void notifyResult(boolean isSuccess);
	
	/**
	 * 标记任务执行结果、是否需要重试
	 * @param success 任务是否成功
	 * @param retry 是否需要重试
	 */
	public void markResult(boolean success, boolean retry){
		this.success = success;
		this.retry = retry;
	}
}
