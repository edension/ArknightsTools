package com.nogii.basesdk.thread;

public class ThreadPoolExecutorProxyFactory {

	static ThreadPoolExecutorProxy mNormalPoolExecutorProxy;

	public static ThreadPoolExecutorProxy getNormalPoolExecutorProxy(){
		if(mNormalPoolExecutorProxy == null){
			synchronized(ThreadPoolExecutorProxyFactory.class){
				if(mNormalPoolExecutorProxy == null){
					int numberOfCores = Runtime.getRuntime().availableProcessors();
					mNormalPoolExecutorProxy = new ThreadPoolExecutorProxy(numberOfCores, numberOfCores * 2,
							3000);
				}
			}
		}
		return mNormalPoolExecutorProxy;
	}

}
