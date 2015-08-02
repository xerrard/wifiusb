package org.xerrard.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public abstract class BindWindow extends Activity implements Callback,
        ServiceThreadExecution, UncaughtExceptionHandler {

    /**
     * 主线程 handler.
     */
    protected Handler mainThreadHandler = new Handler(this);

    /**
     * 服务消息指示
     */
    private static final int WHAT_DELEGATE = 0X100;

    /**
     * 服务委托指示
     */
    private static final String HANDLER_DELEGATE_NAME = "HANDLER_DELEGATE_NAME";

    /**
     * thread 消息处理
     */
    @Override
    public boolean handleMessage(Message msg) {

        boolean ret = false;
        String delegate = msg.getData().getString(HANDLER_DELEGATE_NAME);
        try {
            if (msg.what == WHAT_DELEGATE
                    && !StringUtil.isNullOrEmpty(delegate)) {
                Method m = this.getClass().getMethod(delegate, Object[].class);

                if (m != null) {
                    m.invoke(this, msg.obj);
                }

                ret = true;
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("handle message error!", ex);
        }
        return ret;
    }

    /**
     * 调用UI例程，更新UI
     * 
     * @param delegate
     * @param arg
     */
    public void invokeUIDelegate(String delegate, Object... arg) {

        Message m = mainThreadHandler.obtainMessage(WHAT_DELEGATE, arg);
        m.getData().putString(HANDLER_DELEGATE_NAME, delegate);

        mainThreadHandler.sendMessage(m);
    }

    /**
     * 执行service线程
     * 
     * @param delegate
     * @param arg
     */
    public void invokeServiceDelegate(String delegate, Object... arguments) {
        try {
            ServiceThread t = new ServiceThread(this, this);
            t.setDelegate(delegate);
            t.setArgs(arguments);
            t.start();
        }
        catch (Exception ex) {
            throw new RuntimeException("Invoke service error, delegate: "
                    + delegate, ex);
        }
    }

    /**
     * 运行服务线程
     */
    @Override
    public void run(ServiceThread srvOwner) {
        // TODO Auto-generated method stub
        runService(this, srvOwner);
    }

    /**
     * 运行 service 的缺省实现
     * 
     * @param t
     * @param arg
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    final protected boolean runService(Context t, ServiceThread srvOwner) {
        boolean ret = true;

        try {
            if (srvOwner != null
                    && !StringUtil.isNullOrEmpty(srvOwner.getDelegate())) {
                String serviceDelegate = srvOwner.getDelegate();

                Method m = this.getClass().getMethod(serviceDelegate,
                        Object[].class);

                if (m != null) {
                    m.invoke(this, srvOwner.getArgs());
                }

                ret = true;

            }
        }
        catch (Exception ex) {
            throw new RuntimeException("runService error.", ex);
        }
        return ret;
    }

}
