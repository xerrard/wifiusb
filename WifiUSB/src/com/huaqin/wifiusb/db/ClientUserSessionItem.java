package com.huaqin.wifiusb.db;

/**
 * 
 * @ClassName:ClientUserSessionItem
 * @Description:经过一轮栓选后（相同ip不同端口号合为一个）的用户列表数据结构
 * @author:xuqiang
 * @date:2014年9月25日
 */
public class ClientUserSessionItem {
    public ClientUserSessionItem(int dataTransferState, String ipAddress,int countTimeflag) {
        this.dataTransferState = dataTransferState;
        this.ipAddress = ipAddress;
        this.countTimeflag = countTimeflag;
    }
    public int dataTransferState;
    public String ipAddress;
    public int countTimeflag;
}
