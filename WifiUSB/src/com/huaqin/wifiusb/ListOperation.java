package com.huaqin.wifiusb;

import com.huaqin.wifiusb.db.ClientUserSessionItem;
import com.huaqin.wifiusb.db.Const;
import com.huaqin.wifiusb.db.UserSession;

public class ListOperation {
    WifiUsbSingleton mWifiUsbSingleton = WifiUsbSingleton.getInstance();

    public boolean isListUpdate = false;

    /**
     * <p>
     * Description:1.先将mUserSessionList中新成员放入clientList，
     * 2.再将mUserSessionList中已经消失的成员从clientList去除
     * 3.对mUserSessionList中的transferstate进行设置
     * <p>
     * 
     * @date:2014年10月6日
     */
    public boolean refreshClientList() {
        isListUpdate = false;
        listAdd();
        listDelete();
        listcheckTransfering();
        return isListUpdate;
    }

    /**
     * <p>
     * Description:将新的客户端加入clientList
     * <p>
     * 
     * @date:2014年10月6日
     */
    public void listAdd() {
        mWifiUsbSingleton.sessionIpList.clear();
        for (int i = 0; i < mWifiUsbSingleton.mUserSessionList.size(); i++) {
            UserSession currentsession = mWifiUsbSingleton.mUserSessionList
                    .get(i);
            String currentIpAddress = currentsession.inetSocketAddress
                    .getAddress().toString();

            if (!mWifiUsbSingleton.clientIpList.contains(currentIpAddress)) {
                mWifiUsbSingleton.clientIpList.add(currentIpAddress);
                mWifiUsbSingleton.clientList.add(new ClientUserSessionItem(
                        Const.TRANSFER_NO, currentIpAddress, 0));
                isListUpdate = true;
            }
            mWifiUsbSingleton.sessionIpList.add(currentIpAddress);
        }
    }

    /**
     * <p>
     * Description:delete操作，将已经离开的client从客户端列表中删除
     * <p>
     * 
     * @date:2014年10月6日
     */
    public void listDelete() {
        int removePosition = Const.MAX_POSITION;
        for (int i = 0; i < mWifiUsbSingleton.clientIpList.size(); i++) {
            if (!mWifiUsbSingleton.sessionIpList
                    .contains(mWifiUsbSingleton.clientIpList.get(i))) {
                removePosition = i;
            }
        }
        if (removePosition != Const.MAX_POSITION) {
            mWifiUsbSingleton.clientIpList.remove(removePosition);
            mWifiUsbSingleton.clientList.remove(removePosition);
            isListUpdate = true;
        }

    }

    /**
     * <p>
     * Description:判定每个成员是否在传输中
     * <p>
     * 
     * @date:2014年10月6日
     * @return
     */
    public void listcheckTransfering() {
        for (int i = 0; i < mWifiUsbSingleton.clientList.size(); i++) {
            mWifiUsbSingleton.clientList.get(i).dataTransferState = Const.TRANSFER_NO;
        }
        for (int i = 0; i < mWifiUsbSingleton.mUserSessionList.size(); i++) {
            // 遍历用户列表
            UserSession currentsession = mWifiUsbSingleton.mUserSessionList
                    .get(i);
            String currentIpAddress = currentsession.inetSocketAddress
                    .getAddress().toString();
            int currentPositionInClient = mWifiUsbSingleton.clientIpList
                    .indexOf(currentIpAddress);
            String currentdataTransferState = currentsession.dataTransferState;
            if (currentdataTransferState
                    .equals(Const.DATA_TRANSFER_STATE_START)) {
                if (mWifiUsbSingleton.clientList.get(currentPositionInClient).dataTransferState != Const.TRANSFER_START) {
                    mWifiUsbSingleton.clientList.get(currentPositionInClient).dataTransferState = Const.TRANSFER_START;
                    isListUpdate = true;
                }

            }
        }
    }
}
