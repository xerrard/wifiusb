package com.huaqin.wifiusb.db;

import java.net.InetSocketAddress;
/**
 * 
 * @ClassName:UserSession
 * @Description:原始的已经连接的客户列表数据结构
 * @author:xuqiang
 * @date:2014年9月25日
 */
public class UserSession {
    public UserSession(String dataTransferState, InetSocketAddress inetSocketAddress,int countTimeflag) {
        this.dataTransferState = dataTransferState;
        this.inetSocketAddress = inetSocketAddress;
        this.countTimeflag = countTimeflag;
        
    }
    public String dataTransferState;
    public InetSocketAddress inetSocketAddress;
    public int countTimeflag;
    public int finaldataTransferState = 2;
}
