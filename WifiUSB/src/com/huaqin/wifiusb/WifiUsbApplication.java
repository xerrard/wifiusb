package com.huaqin.wifiusb;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.huaqin.wifiusb.db.ClientUserSessionItem;
import com.huaqin.wifiusb.db.UserSession;

import android.app.Activity;
import android.app.Application;

public class WifiUsbApplication extends Application {


    
    private List<Activity> activityList = new LinkedList<Activity>(); 
    @Override
    public void onCreate() {
        super.onCreate();
        WifiUsbSingleton mWifiUsbSingleton = WifiUsbSingleton.getInstance();
        mWifiUsbSingleton.mUserSessionList = new ArrayList<UserSession>(); // 当前实际的session列表
        mWifiUsbSingleton.clientList = new ArrayList<ClientUserSessionItem>(); // 用于显示的session列表
        mWifiUsbSingleton.clientIpList = new ArrayList<String>(); // 用户显示的Ip列表
        mWifiUsbSingleton.sessionIpList = new ArrayList<String>(); // 当前实际的ip列表
    }

    // 添加Activity到容器中  
    public void addActivity(Activity activity) {  
        activityList.add(activity);  
    }  
  
    // 遍历所有Activity并finish  
    public void exit() {  
        for (Activity activity : activityList) {  
            activity.finish();  
        }  
        System.exit(0);  
    } 
}