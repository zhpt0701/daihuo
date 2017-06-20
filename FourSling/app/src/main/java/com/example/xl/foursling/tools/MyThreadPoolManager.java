package com.example.xl.foursling.tools;

import android.util.Log;

import java.util.concurrent.*;

public class MyThreadPoolManager {

	private MyThreadPoolManager() {
	}

	private static MyThreadPoolManager instance;

	public synchronized static MyThreadPoolManager getInstance() {
		if (instance == null) {
			instance = new MyThreadPoolManager();
		}
		return instance;
	}

	private ThreadPoolExecutor executor;

	public void execute(Runnable r) {

		// 创建线程池
		if (executor == null) {
			// corePoolSize:核心线程池，指的是正常的情况下，池子里面最大允许的线程数量
			// maximumPoolSize:最大线程数，指的是非正常情况下，池子里面最大允许的线程数量
			// keepAliveTime：空闲时间
			// unit：空闲时间的单位
			// workQueue:等待队列
            // threadFactory:线程的创建工程
            // handler:异常处理机制
            int cpuCounts = Runtime.getRuntime().availableProcessors();
            int corePoolSize = cpuCounts * 2+1;
            executor = new ThreadPoolExecutor(cpuCounts+1, corePoolSize, 0, TimeUnit.SECONDS,
					new ArrayBlockingQueue<Runnable>(20),
					Executors.defaultThreadFactory(),
					new ThreadPoolExecutor.AbortPolicy());
		}
        try {
            executor.execute(r);
        }catch (RejectedExecutionException e){
            e.printStackTrace();
        }
	}
	
	public void cancel(Runnable r) {
		executor.getQueue().remove(r);//将任务从等待区域移除
	}

}
