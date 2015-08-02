package org.xerrard.util;

import java.lang.Thread.UncaughtExceptionHandler;

public class ServiceThread implements Runnable {
	private Thread innerThread;
	private ServiceThreadExecution execution;
	public Object getArgs() {
		return args;
	}

	public void setArgs(Object args) {
		this.args = args;
	}

	public String getDelegate() {
		return delegate;
	}

	public void setDelegate(String delegate) {
		this.delegate = delegate;
	}

	private Object args;
	private String delegate;
	
	public   ServiceThread(ServiceThreadExecution execution, UncaughtExceptionHandler handler) {
		innerThread = new Thread(this);
		this.execution = execution;
		innerThread.setUncaughtExceptionHandler(handler);
	}
	
	public void start() {
		if (innerThread != null) {
			innerThread.start();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (execution != null) {
			execution.run(this);
		}
	}
}
