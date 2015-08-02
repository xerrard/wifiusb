package com.huaqin.wifiusb;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.xerrard.util.WifiUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.huaqin.wifiusb.db.Const;
import com.huaqin.wifiusb.ui.WifiUsbActivity;
import com.huaqin.wifiusb.util.Util;

/**
 * 
 * @ClassName:WifiUsbService
 * @Description:主service，可以后台运行ftpserver
 * @author:xuqiang
 * @date:2014年9月25日
 */
public class WifiUsbService extends Service {
    private WifiUsbFtpServer mMyFtpServer = null;
    private WifiUsbSingleton mWifiUsbSingleton;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private Timer mTimer;
    File UserProperties = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        mWifiUsbSingleton = WifiUsbSingleton.getInstance(); // 单例，存储数据
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.icon = R.drawable.ic_launcher;
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        getPreference();
        registerWifiReceiver();
        if (safeSetupListener() && isUserProfertiesOK()) {
            startFtpServer();
            mWifiUsbSingleton.mIsWifiUsbSetup = true;
            updateWidget();
            updateUi();
            startNotification();
            ListOperation mListOperation = new ListOperation();
            mTimer = new Timer();
            mTimer.schedule(new RemindTask(mListOperation), Const.TIME_DELAY,
                    Const.TIME_PEROID); // 延时1000ms后执行，1000ms执行一次
        }
        else {
            if (!isUserProfertiesOK()) {
                mWifiUsbSingleton.mSTORFullFlag = true;
            }
            else {
                mWifiUsbSingleton.mIllegalPort = true;
            }
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWidget() {
        Intent intent = new Intent("wifiusb.aciton.widget.update");
        sendBroadcast(intent);
    }

    private void updateUi() {
        Intent intent = new Intent("wifiusb.aciton.ui.update");
        sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        stopFtpServer();
        mWifiUsbSingleton.mIsWifiUsbSetup = false;
        unregisterWifiReceiver();
        updateUi();
        updateWidget();
        cancelNotification();
        clearClientList();
        super.onDestroy();
    }

    private void clearClientList() {
        mWifiUsbSingleton.mUserSessionList.clear();// 当前实际的session列表
        mWifiUsbSingleton.clientList.clear();// 用于显示的session列表
        mWifiUsbSingleton.clientIpList.clear();// 用户显示的Ip列表
        mWifiUsbSingleton.sessionIpList.clear();// 当前实际的ip列表

    }

    private boolean isUserProfertiesOK() {
        try {
            UserProperties = getUserProperties();
            if (UserProperties.exists()) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return false;
        }
    }

    private void startFtpServer() {
        mWifiUsbSingleton.running_port = mWifiUsbSingleton.port;
        mMyFtpServer = new WifiUsbFtpServer(UserProperties,
                mWifiUsbSingleton.running_port, mWifiUsbSingleton.mIsAnonymous,
                mWifiUsbSingleton.username, mWifiUsbSingleton.password,
                mWifiUsbSingleton.mHaveWritepermission);
        mMyFtpServer.start(); // ftpserver启动

    }

    private void stopFtpServer() {
        if (mMyFtpServer != null) {
            mMyFtpServer.stop();
            while (!mMyFtpServer.isStoped())
                ;
            mMyFtpServer = null;
        }
    }

    /**
     * <p>
     * Description:配置好服务器的属性，属性值原本是写在string中的，现在给配置到文件中
     * <p>
     * 
     * @throws IOException
     * 
     * @date:2014年7月18日
     */

    private File getUserProperties() throws IOException {
        String user = getString(R.string.users);
        Util.stringToFile(user, mWifiUsbSingleton.propertiespath);

        return new File(mWifiUsbSingleton.propertiespath);
    }

    private void getPreference() {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        mWifiUsbSingleton.mIsAnonymous = settings.getBoolean(
                Const.KEY_ANONYMOUS_PREFERENCE,
                Const.DEFAULT_ANONYMOUS_PREFERENCE);
        mWifiUsbSingleton.username = settings.getString(
                Const.KEY_USERNAME_PREFERENCE,
                Const.DEFAULT_USERNAME_PREFERENCE);
        mWifiUsbSingleton.password = settings.getString(
                Const.KEY_PASSWORD_PREFERENCE,
                Const.DEFAULT_PASSWORD_PREFERENCE);
        mWifiUsbSingleton.port = Integer.parseInt(settings.getString(
                Const.KEY_PORT_PREFERENCE, Const.DEFAULT_PORT_PREFERENCE));
        mWifiUsbSingleton.mHaveWritepermission = settings.getBoolean(
                Const.KEY_WRITEPERMISSION_PREFERENCE,
                Const.DEFAULT_WRITEPERMISSION_PREFERENCE);
    }

    @SuppressWarnings("deprecation")
    private void startNotification() {
        String mNotificationContentTitle = getString(R.string.ftp_server)
                .toString();
        String mNotificationContentText = getString(R.string.running)
                .toString();
        Intent mNotificationIntent = new Intent(WifiUsbService.this,
                WifiUsbActivity.class);
        PendingIntent mNotificationPendingIntent = PendingIntent.getActivity(
                WifiUsbService.this, 0, mNotificationIntent, 0);
        mNotification.setLatestEventInfo(getApplicationContext(),
                mNotificationContentTitle, mNotificationContentText,
                mNotificationPendingIntent);
        mNotification.tickerText = getString(R.string.ftp_server).toString()
                + " " + getString(R.string.running).toString();
        mNotificationManager.notify(1, mNotification);
    }

    private void cancelNotification() {
        mNotificationManager.cancelAll();
    }

    class RemindTask extends TimerTask {
        ListOperation mListOperation;

        public RemindTask(ListOperation mListOperation) {
            super();
            this.mListOperation = mListOperation;
        }

        public void run() {
            mListOperation.refreshClientList();
            updateUi();
            updateWidget();
        }
    }

    private boolean safeSetupListener() {
        if (mWifiUsbSingleton.port > 1024 && mWifiUsbSingleton.port < 65535)
            return true;
        else
            return false;
    }

    protected void registerWifiReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiStateReceiver, filter);
    }

    protected void unregisterWifiReceiver() {
        unregisterReceiver(wifiStateReceiver);
    }

    BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            if (!WifiUtil.isWifiEnabled(context)
                    || !WifiUtil.isWifiConnected(context)) {
                stopSelf();
            }
        }
    };

}
