package com.huaqin.wifiusb.ui;

import org.xerrard.util.WifiUtil;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.huaqin.wifiusb.R;
import com.huaqin.wifiusb.WifiUsbService;
import com.huaqin.wifiusb.WifiUsbSingleton;

public class WifiUsbWidget extends AppWidgetProvider {

    WifiUsbSingleton mWifiUsbSingleton = WifiUsbSingleton.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final Intent serviceIntent = new Intent(context, WifiUsbService.class);
        if (intent.getAction().equals("wifiusb.widget.aciton.click")) {
        	Log.d("xerrard", "widget click");
            if (mWifiUsbSingleton.mIsWifiUsbSetup) {
                context.stopService(serviceIntent);
            }
            else {
                if (WifiUtil.isWifiEnabled(context)
                        && WifiUtil.isWifiConnected(context)) {
                    context.startService(serviceIntent);
                }
            }
        }
        else if (intent.getAction().equals("wifiusb.aciton.widget.update")) {
            showWidget(context);
        }
        else if (intent.getAction().equals(
                WifiManager.WIFI_STATE_CHANGED_ACTION)
                || intent.getAction().equals(
                        ConnectivityManager.CONNECTIVITY_ACTION)) {
            showWidget(context);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
    	Log.d("xerrard", "onUpdate start");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        final Intent intent = new Intent("wifiusb.widget.aciton.click");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widgetbutton, pi);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        showWidget(context);
    	Log.d("xerrard", "onUpdate stop");
    }

    protected void showWidget(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        if (mWifiUsbSingleton.mIsWifiUsbSetup) {
            remoteViews.setImageViewResource(R.id.widgetbutton,
                    R.drawable.widget_icon_stop);
            remoteViews.setViewVisibility(R.id.widgetip, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widgettext, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widgetipaddress, View.VISIBLE);
            remoteViews.setTextViewText(R.id.widgetipaddress, ("ftp://"
                    + WifiUtil.getLocalWifiIpAddress(context) + ":"
                    + mWifiUsbSingleton.running_port + "/"));
            if (mWifiUsbSingleton.mSTORFullFlag) {
                Toast.makeText(context, R.string.phone_memory_full, Toast.LENGTH_LONG).show();;
                mWifiUsbSingleton.mSTORFullFlag = false;
                context.stopService(new Intent(context,WifiUsbService.class));
            }
        }
        else {
            if (mWifiUsbSingleton.mSTORFullFlag) {
                Toast.makeText(context, R.string.phone_memory_full, Toast.LENGTH_LONG).show();;
                mWifiUsbSingleton.mSTORFullFlag = false;
            }
            if (mWifiUsbSingleton.mIllegalPort) {
                Toast.makeText(context, R.string.notice_change_port,
                        Toast.LENGTH_LONG).show();
                mWifiUsbSingleton.mIllegalPort = false;
            }
            if (WifiUtil.isWifiEnabled(context) && WifiUtil.isWifiConnected(context)) {
                remoteViews.setImageViewResource(R.id.widgetbutton,
                        R.drawable.widget_icon_wifi);
                remoteViews.setViewVisibility(R.id.widgetip, View.INVISIBLE);
            }
            else {
                remoteViews.setImageViewResource(R.id.widgetbutton,
                        R.drawable.widget_icon_nowifi);
                remoteViews.setViewVisibility(R.id.widgetip, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.widgettext, View.GONE);
                remoteViews.setViewVisibility(R.id.widgetipaddress,
                        View.VISIBLE);
                remoteViews.setTextViewText(R.id.widgetipaddress,
                        context.getString(R.string.noticeconnectap));
            }
        }

        AppWidgetManager appWidgetManger = AppWidgetManager
                .getInstance(context);
        int[] appIds = appWidgetManger.getAppWidgetIds(new ComponentName(
                context, WifiUsbWidget.class));
        appWidgetManger.updateAppWidget(appIds, remoteViews);
    }



}
