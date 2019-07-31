package com.nogii.basesdk.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorProxy {

	ThreadPoolExecutor mExecutor;

	private int mCorePoolSize;
	private int mMaximumPoolSize;
	private long mKeepAliveTime;

	public ThreadPoolExecutorProxy(int corePoolSize, int maximumPoolSize,
								   long keepAliveTime) {
		super();
		mCorePoolSize = corePoolSize;
		mMaximumPoolSize = maximumPoolSize;
		mKeepAliveTime = keepAliveTime;
	}

	private void initThreadPoolExecutor() {

		if (mExecutor == null || mExecutor.isShutdown()
				|| mExecutor.isTerminated()) {

			synchronized (ThreadPoolExecutorProxy.class) {

				if (mExecutor == null) {
					TimeUnit unit = TimeUnit.MILLISECONDS;
					BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
					ThreadFactory threadFactory = Executors.defaultThreadFactory();
					RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

					mExecutor = new ThreadPoolExecutor(mCorePoolSize,
							mMaximumPoolSize, mKeepAliveTime, unit, workQueue,
							threadFactory, handler);
				}
			}
		}
	}

	public Future<?> submit(Runnable task) {
		initThreadPoolExecutor();
		Future<?> submit = mExecutor.submit(task);
		return submit;
	}

	public void execute(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.execute(task);
	}

	public void remove(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.remove(task);
	}

	public void release() {
		if (!(mExecutor == null || mExecutor.isShutdown()
				|| mExecutor.isTerminated())) {
			mExecutor.shutdown();
		}
	}
}
