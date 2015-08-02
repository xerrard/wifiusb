package org.xerrard.util;

import com.huaqin.wifiusb.util.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiUtil {

    public static String getWifiSSID(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mWifiManager.getConnectionInfo().getSSID();
    }

    /**
     * <p>
     * Description:Wifi是否开启
     * <p>
     * 
     * @date:2014年9月25日
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mWifiManager.isWifiEnabled();
    }

    /**
     * <p>
     * Description:Wifi是否已经连接到热点
     * <p>
     * 
     * @date:2014年9月25日
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     * <p>
     * Description:获得当前服务器的IP地址
     * <p>
     * 
     * @date:2014年9月3日
     * @return
     */
    public static String getLocalWifiIpAddress(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddr = mWifiManager.getConnectionInfo().getIpAddress();
        return Util.intToIpAddress(ipAddr);
    }
    
    
}
