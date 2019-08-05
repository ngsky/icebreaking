package com.ngsky.ice.rest.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.*;

@Slf4j
@Setter
@Getter
public class IceExecutorService implements InitializingBean {

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 核心线程数
	 */
	private int corePoolSize;

	/**
	 * 最大线程数
	 */
	private int maxPoolSize;

	/**
	 * 线程存活时间（分钟）
	 */
	private int keepAliveMinute;

	/**
	 * 失败重试次数
	 */
	private int retryTimes;

	/**
	 * 失败等待时间(秒)
	 */
	private int retryDelay;

	/**
	 * 线程工厂
	 */
	private ThreadFactory threadFactory;

	/**
	 * 线程池阻塞队列
	 */
	private LinkedBlockingQueue<Runnable> taskQueue;

	/**
	 * 线程池执行器
	 */
	private ExecutorService executorService;

	public IceExecutorService() {
		this.name = name;
		this.corePoolSize = corePoolSize <= 0 ? 5 : corePoolSize;
		this.maxPoolSize = maxPoolSize <= 0 ? 10 : maxPoolSize;
		this.keepAliveMinute = keepAliveMinute <= 0 ? 3 : keepAliveMinute;
	}

	public IceExecutorService(String name, int corePoolSize, int maxPoolSize, int keepAliveMinute) {
		super();
		this.name = name;
		this.corePoolSize = corePoolSize <= 0 ? 5 : corePoolSize;
		this.maxPoolSize = maxPoolSize <= 0 ? 10 : maxPoolSize;
		this.keepAliveMinute = keepAliveMinute <= 0 ? 3 : keepAliveMinute;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() {
		log.info("线程池启动,启动参数: name:{}, corePoolSize:{}, maxPoolSize:{}, keepAliveMinute:{}",name, corePoolSize, maxPoolSize, keepAliveMinute);
		taskQueue = new LinkedBlockingQueue<Runnable>();
		threadFactory = new ThreadFactoryBuilder().setNameFormat("ice-thread-pool-%d").build();
		executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveMinute, TimeUnit.MINUTES, taskQueue, threadFactory, new ThreadPoolExecutor.AbortPolicy());
	}

	public <T> Future<T> submit(Callable<T> job) {
		return executorService.submit(job);
	}

	public void execute(Task task) {
		if (retryTimes > 0) {
			task.setRetryTimes(retryTimes);
		}

		if (retryDelay > 0) {
			task.setRetryDelay(retryDelay);
		}
		if (null == executorService) {
			init();
		}
		executorService.execute(task);
	}

	public void shutdown() {
		log.info("线程池关闭， name:{}", name);
		executorService.shutdown();
	}
}
