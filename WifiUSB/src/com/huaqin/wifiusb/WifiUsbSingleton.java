package com.huaqin.wifiusb;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Environment;

import com.huaqin.wifiusb.db.ClientUserSessionItem;
import com.huaqin.wifiusb.db.Const;
import com.huaqin.wifiusb.db.UserSession;

/**
 * 
 * @ClassName:WifiUsbSingleton
 * @Description:单例模式，主要是存储一些共享信息
 * @author:xuqiang
 * @date:2014年9月25日
 */
@SuppressWarnings("serial")
public class WifiUsbSingleton implements Serializable {

    public String DataTransferState = Const.DATA_TRANSFER_STATE_DEFAULT;
    public ArrayList<UserSession> mUserSessionList; // 当前实际的session列表
    public ArrayList<ClientUserSessionItem> clientList; // 用于显示的session列表
    public ArrayList<String> clientIpList; // 用户显示的Ip列表
    public ArrayList<String> sessionIpList; // 当前实际的ip列表

    // public String propertiespath = "/mnt/sdcard/users.properties";//
    // ftp用户配置文件路径
    public String propertiespath = Environment.getExternalStorageDirectory()
            .getPath() + "/users.properties";
    public int port = 2222; // 端口号
    public int running_port = 2222; // 已经运行中的端口号
    public boolean mIsAnonymous; // 是否匿名
    public String username; // 用户名
    public String password; // 密码
    public boolean mHaveWritepermission;// 是否有写权限，即是不是只读
    public boolean mIsWifiUsbSetup = false; // ftp服务器是否在运行
    public boolean mSTORFullFlag = false;
    public boolean mIllegalPort = false;

    private static class SingletonHolder {
        /**
         * 单例对象实例
         */
        static final WifiUsbSingleton INSTANCE = new WifiUsbSingleton();
    }

    public static WifiUsbSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private WifiUsbSingleton() {
    }

    /**
     * readResolve方法应对单例对象被序列化时候
     */
    private Object readResolve() {
        return getInstance();
    }
}
