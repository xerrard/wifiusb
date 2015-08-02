package com.huaqin.wifiusb;

import java.io.IOException;

import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;

import android.util.Log;

import com.huaqin.wifiusb.db.Const;
import com.huaqin.wifiusb.db.UserSession;

/**
 * 
 * @ClassName:WifiUsbFtplet
 * @Description:FtpServer的事件处理
 * @author:xuqiang
 * @date:2014年9月3日
 */
public class WifiUsbFtplet extends DefaultFtplet {

    WifiUsbSingleton mSingleton = WifiUsbSingleton.getInstance();

    @Override
    public FtpletResult onLogin(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        // TODO Auto-generated method stub
        Log.i("xerrard", "onLogin：" + session.getClientAddress());
        return super.onLogin(session, request);
    }

    @Override
    public FtpletResult onConnect(FtpSession session) throws FtpException,
            IOException {

        Log.i("xerrard", "建立新连接：" + session.getClientAddress());
        UserSession mSession = new UserSession(
                Const.DATA_TRANSFER_STATE_DEFAULT,
                session.getClientAddress(), 0);
        mSingleton.mUserSessionList.add(mSession); // 建立连接后加入用户列表

        return super.onConnect(session);
    }

    @Override
    public FtpletResult onDisconnect(FtpSession session) throws FtpException,
            IOException {
        Log.i("xerrard", "连接关闭：" + session.getClientAddress());
        for (UserSession mSession : mSingleton.mUserSessionList) {
            if (mSession.inetSocketAddress.equals(session.getClientAddress())) {
                mSingleton.mUserSessionList.remove(mSession); // 关闭连接后从用户列表中移除
            }
        }

        return super.onDisconnect(session);
    }

    @Override
    public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        // TODO Auto-generated method stub
        Log.i("xerrard", "onDownloadEnd"
                + session.getClientAddress());
        mSingleton.DataTransferState = Const.DATA_TRANSFER_STATE_ED;

        for (int i = 0; i < mSingleton.mUserSessionList.size(); i++) {
            if (session.getClientAddress().equals(
                    mSingleton.mUserSessionList.get(i).inetSocketAddress)) {
                mSingleton.mUserSessionList.get(i).dataTransferState = mSingleton.DataTransferState; //发送完毕后置状态
            }
        }

        return super.onDownloadEnd(session, request);
    }

    @Override
    public FtpletResult onDownloadStart(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        // TODO Auto-generated method stub
        Log.i("xerrard", "onDownloadStart"
                + session.getClientAddress());
        mSingleton.DataTransferState = Const.DATA_TRANSFER_STATE_START;
        for (int i = 0; i < mSingleton.mUserSessionList.size(); i++) {
            if (session.getClientAddress().equals(
                    mSingleton.mUserSessionList.get(i).inetSocketAddress)) {
                mSingleton.mUserSessionList.get(i).dataTransferState = mSingleton.DataTransferState;//发送开始置状态
            }
        }
        return super.onDownloadStart(session, request);
    }

    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        // TODO Auto-generated method stub
        Log.i("xerrard", "onUploadEnd"
                + session.getClientAddress());
        mSingleton.DataTransferState = Const.DATA_TRANSFER_STATE_ED;
        for (int i = 0; i < mSingleton.mUserSessionList.size(); i++) {
            if (session.getClientAddress().equals(
                    mSingleton.mUserSessionList.get(i).inetSocketAddress)) {
                mSingleton.mUserSessionList.get(i).dataTransferState = mSingleton.DataTransferState;//发送完毕后置状态
            }
        }
        return super.onUploadEnd(session, request);
    }

    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        // TODO Auto-generated method stub
        Log.i("xerrard", "onUploadStart"
                + session.getClientAddress());
        mSingleton.DataTransferState = Const.DATA_TRANSFER_STATE_START;
        for (int i = 0; i < mSingleton.mUserSessionList.size(); i++) {
            if (session.getClientAddress().equals(
                    mSingleton.mUserSessionList.get(i).inetSocketAddress)) {
                mSingleton.mUserSessionList.get(i).dataTransferState = mSingleton.DataTransferState;//发送开始置状态
            }
        }
        return super.onUploadStart(session, request);
    }

}
