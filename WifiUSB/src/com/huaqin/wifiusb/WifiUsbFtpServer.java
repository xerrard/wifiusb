package com.huaqin.wifiusb;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 
 * @ClassName:MyFtpServer
 * @Description:ftpServer的封装，只传端口号和用户属性文件的路径，外部只start,stop即可
 * @author:xerrard
 * @date:2014年7月21日
 */
public class WifiUsbFtpServer {
    private File mUserProperties; // 用户配置文件
    private int port; // 端口号
    private boolean mIsAnonymous;
    private String username;
    private String password;
    private boolean mHaveWritepermission;
    FtpServer mFtpserver;
    FtpServerFactory mServerFactory;

    public WifiUsbFtpServer(File mUserProperties, int port) {
        super();
        this.mUserProperties = mUserProperties;
        this.port = port;
    }

    /**
     * 
     * @param mUserProperties 属性文件
     * @param port 端口号
     * @param mIsAnonymous 是否匿名
     * @param username 用户名
     * @param password 密码
     * @param mHaveWritepermission 是否只读
     */
    public WifiUsbFtpServer(File mUserProperties, int port,
            boolean mIsAnonymous, String username, String password,
            boolean mHaveWritepermission) {
        super();
        this.mUserProperties = mUserProperties;
        this.port = port;
        this.mIsAnonymous = mIsAnonymous;
        this.username = username;
        this.password = password;
        this.mHaveWritepermission = mHaveWritepermission;
    }

    /**
     * <p>
     * Description:FTP服务器建立的核心代码
     * 
     * @date:2014年7月18日
     */
    @SuppressLint("SdCardPath")
    public void start() {

        mServerFactory = new FtpServerFactory(); // ftpserver工厂
        System.out.println("Factory创建");
        ListenerFactory mListenFactory = new ListenerFactory(); // 监听器工厂
        PropertiesUserManagerFactory mUserManagerFactory = new PropertiesUserManagerFactory(); // 用户管理工厂
        mUserManagerFactory.setFile(mUserProperties); // 用户管理工厂设置用户配置文件
        UserManager mUserManager = mUserManagerFactory.createUserManager();
        try {
            BaseUser adminUser = (BaseUser) mUserManager
                    .getUserByName(mUserManager.getAdminName());
            BaseUser anonymousUser = (BaseUser) mUserManager
                    .getUserByName("anonymous");
            if (mIsAnonymous) {
                adminUser.setEnabled(false);
                if (!mHaveWritepermission) { // 只读属性
                    List<Authority> authorities = new ArrayList<Authority>();
                    authorities.add(new WritePermission());
                    anonymousUser.setAuthorities(authorities);
                }
            }
            else {
                anonymousUser.setEnabled(false);
                adminUser.setName(username);
                adminUser.setPassword(password);
                if (!mHaveWritepermission) { // 只读属性
                    List<Authority> authorities = new ArrayList<Authority>();
                    authorities.add(new WritePermission());
                    adminUser.setAuthorities(authorities);
                }
            }
            mUserManager.save(adminUser);
            mUserManager.save(anonymousUser);

            /**
             * username如果和admin不一致，这样就有了三个user,此时要把admin用户disable
             */
            if (!username.equals(mUserManager.getAdminName())) {
                BaseUser adminUserr = (BaseUser) mUserManager
                        .getUserByName(mUserManager.getAdminName());
                adminUserr.setEnabled(false);
                mUserManager.save(adminUserr);
            }

        }
        catch (FtpException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        mServerFactory.setUserManager(mUserManager);// ftpserver工厂设置用户配置
        mListenFactory.setPort(port); // 设置端口号
        mServerFactory.addListener("default", mListenFactory.createListener());// ftpserver工厂设置监听器
        Map<String, Ftplet> ftplets = new LinkedHashMap<String, Ftplet>();
        ftplets.put(WifiUsbFtplet.class.getName(), new WifiUsbFtplet());
        mServerFactory.setFtplets(ftplets);
        mFtpserver = mServerFactory.createServer(); // ftpserver建立

        try {
            mFtpserver.start();
        }
        catch (FtpException e) {
            // TODO Auto-generated catch block
            System.out.println(e);
        }
    }

    // ////////////////
    // my listener start.
    // ////////////////

    public void stop() {
        mFtpserver.stop();
    }

    public boolean isStoped() {
        DefaultFtpServer concreate = (DefaultFtpServer) mFtpserver;
        if (concreate.isStopped()) {
            return true;
        }
        return false;
    }
}
