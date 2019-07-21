package com.ngsky.ice.rest.common;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Setter
@Getter
public class IceScheduledExecutorService implements InitializingBean {
	private String name;
	private int corePoolSize;
	private long delay;
	private long interval;
	private List<Runnable> tasks = new ArrayList<Runnable>();
	public IceScheduledExecutorService() {
	}

	public IceScheduledExecutorService(String name, int corePoolSize, long delay, long interval) {
		super();
		this.name = name;
		this.corePoolSize = corePoolSize;
		this.delay = delay;
		this.interval = interval;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() {
		log.info("定时任务启动， name:{}, corePoolSize:{}",name, corePoolSize);
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(corePoolSize);
		for (Runnable runnable : tasks) {
			scheduledThreadPool.scheduleAtFixedRate(runnable, delay, interval, TimeUnit.SECONDS);
		}
	}
}
