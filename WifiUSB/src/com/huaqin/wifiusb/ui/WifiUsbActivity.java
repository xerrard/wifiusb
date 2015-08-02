package com.huaqin.wifiusb.ui;

import org.xerrard.util.BindWindow;
import org.xerrard.util.MyLoggerHelper;
import org.xerrard.util.WifiUtil;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huaqin.wifiusb.ClientListAdapter;
import com.huaqin.wifiusb.R;
import com.huaqin.wifiusb.WifiUsbService;
import com.huaqin.wifiusb.WifiUsbSingleton;
import com.huaqin.wifiusb.db.Const;

public class WifiUsbActivity extends BindWindow implements OnClickListener {
    private Button mWifiUsbBtnSetup;
    private Button mWifiUsbBtnSetting;
    private Button mWifiUsbBtnSettingNoWifi;
    private TextView mWifiUsbTvewIpAddress;
    private TextView mWifiUsbTvewIntroduction;
    private TextView mWifiUsbTvewWifiName;
    private ImageView mWifiUsbIvewWifiState;
    private ListView mWifiUsbLvewClientList;
    private LinearLayout mWifiUsbLlyoutClientList;
    private LinearLayout mWifiUsbLlyoutSetting;
    private LinearLayout mWifiUsbLlyoutSettingNoWifi;
    private ClientListAdapter mClientListAdapter;
    private int ui_state;
    private WifiUsbSingleton mWifiUsbSingleton;

    public static Handler serviceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLoggerHelper.setConext(this);
        MyLoggerHelper.setHandler(new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if(msg.what==1){
                    CharSequence str = (CharSequence) msg.obj;
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                }
            }
            
        });
        mWifiUsbSingleton = WifiUsbSingleton.getInstance();
        setActionBarView();
        setContentView(R.layout.main_activity);
        initRes();
        registerWifiReceiver();
        registerServiceReceiver();

        /*
         * 第一次进入应用程序，如果一切条件允许，则启动服务
         */
        updateUI();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        getPreference(); // 获得属性值
        if (ui_state == Const.UI_STATE_STOPED) {
            invokeServiceDelegate("startFtpServerService");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreference(); // 获得属性值
        if (mWifiUsbSingleton.mIsAnonymous) {
            mClientListAdapter.setUser(getString(R.string.anonymous));
        }
        else {
            mClientListAdapter.setUser(mWifiUsbSingleton.username);
        }
        updateUI();

    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            if (v.equals(mWifiUsbBtnSetting)
                    || v.equals(mWifiUsbBtnSettingNoWifi)) {
                startActivity(new Intent(WifiUsbActivity.this,
                        WifiUsbSettingActivity.class));
            }
            else if (v.equals(mWifiUsbBtnSetup)) {
                if (mWifiUsbSingleton.mIsWifiUsbSetup) {
                    queryStopFtpServerService();
                }
                else {
                    invokeServiceDelegate("startFtpServerService");
                }
            }
        }
        else if ((v instanceof ImageView) && (v.equals(mWifiUsbIvewWifiState))) {
            startActivity(new Intent(
                    android.provider.Settings.ACTION_WIFI_SETTINGS));
        }

    }

    private void initRes() {
        mWifiUsbBtnSetup = (Button) findViewById(R.id.wifiusb_btn_setup);
        mWifiUsbBtnSetting = (Button) findViewById(R.id.wifiusb_btn_setting);
        mWifiUsbBtnSettingNoWifi = (Button) findViewById(R.id.wifiusb_btn_setting_2);
        mWifiUsbTvewIpAddress = (TextView) findViewById(R.id.ip_address);
        mWifiUsbTvewIntroduction = (TextView) findViewById(R.id.instruction);
        mWifiUsbTvewWifiName = (TextView) findViewById(R.id.wifi_name_tv);
        mWifiUsbIvewWifiState = (ImageView) findViewById(R.id.wifi_state_image);
        mWifiUsbLvewClientList = (ListView) findViewById(R.id.client_list);
        mWifiUsbLlyoutClientList = (LinearLayout) findViewById(R.id.client_list_layout);
        mWifiUsbLlyoutSetting = (LinearLayout) findViewById(R.id.setting_llyout);
        mWifiUsbLlyoutSettingNoWifi = (LinearLayout) findViewById(R.id.setting_llyout_nowifi);
        mWifiUsbBtnSetup.setOnClickListener(this);
        mWifiUsbBtnSetting.setOnClickListener(this);
        mWifiUsbBtnSettingNoWifi.setOnClickListener(this);
        mWifiUsbIvewWifiState.setOnClickListener(this);

        mClientListAdapter = new ClientListAdapter(this,
                mWifiUsbSingleton.clientList, mWifiUsbSingleton.username);
        mWifiUsbLvewClientList.setAdapter(mClientListAdapter);
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

    protected void registerServiceReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("wifiusb.aciton.ui.update");
        registerReceiver(serviceUpdateUiReceiver, filter);
    }

    protected void unRegisterServiceReceiver() {
        unregisterReceiver(serviceUpdateUiReceiver);
    }

    BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            if (!WifiUtil.isWifiEnabled(context)
                    || !WifiUtil.isWifiConnected(context)) {
                invokeServiceDelegate("stopFtpServerService");
                updateUI();
            }
            else {
                updateUI();
            }
        }
    };

    BroadcastReceiver serviceUpdateUiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals("wifiusb.aciton.ui.update")) {
                updateUI();
            }
        }
    };

    public void startFtpServerService(Object...objects) {
        if (!mWifiUsbSingleton.mIsWifiUsbSetup) {
            startService(new Intent(WifiUsbActivity.this, WifiUsbService.class));
        }

    }

    public void stopFtpServerService(Object...objects) {
        if (mWifiUsbSingleton.mIsWifiUsbSetup) {
            final Intent serviceIntent = new Intent(WifiUsbActivity.this,
                    WifiUsbService.class);
            stopService(serviceIntent);
        }

    }

    public void queryStopFtpServerService() {
        if (isTransfering()) {
            AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
            adBuilder.setPositiveButton(R.string.stop_it_anyway,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            invokeServiceDelegate("stopFtpServerService");
                        }
                    });
            adBuilder.setNegativeButton(R.string.no,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            adBuilder.setMessage(R.string.file_transfer_dialog);
            adBuilder.create().show();
        }
        else {
            invokeServiceDelegate("stopFtpServerService");
        }
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

    private boolean isTransfering() {
        for (int i = 0; i < mWifiUsbSingleton.clientList.size(); i++) {
            if (mWifiUsbSingleton.clientList.get(i).dataTransferState == Const.TRANSFER_START) {
                return true;
            }
        }
        return false;
    }

    protected void updateUI() {
        if (mWifiUsbSingleton.mIllegalPort) {
            noticeLegalPort();
            mWifiUsbSingleton.mIllegalPort = false;
        }
        if (mWifiUsbSingleton.mSTORFullFlag) {
            noticePhoneMemorryFull();
            mWifiUsbSingleton.mSTORFullFlag = false;
        }
        else if (!WifiUtil.isWifiEnabled(this)) {
            ui_state = Const.UI_STATE_NO_WIFI;
        }
        else if (!WifiUtil.isWifiConnected(this)) {
            ui_state = Const.UI_STATE_WIFI_NOCONNECTED;
        }
        else if (mWifiUsbSingleton.mIsWifiUsbSetup) {
            if (mWifiUsbSingleton.clientList.isEmpty()) {
                ui_state = Const.UI_STATE_RUNNING;
            }
            else {
                ui_state = Const.UI_STATE_CLIENT_CONNECTED;
            }
        }
        else {
            ui_state = Const.UI_STATE_STOPED;
        }

        switch (ui_state) {
            case Const.UI_STATE_NO_WIFI:
                updateUiPleaseOpenWifi();
                break;
            case Const.UI_STATE_WIFI_NOCONNECTED:
                updateUiPleaseConnectAp();
                break;
            case Const.UI_STATE_RUNNING:
                updateUiServerRunning();
                break;
            case Const.UI_STATE_STOPED:
                updateUiServerStoped();
                break;
            case Const.UI_STATE_CLIENT_CONNECTED:
                updateUiClientConnected();
                break;
            default:
                updateUiServerStoped();
                break;
        }

    }

    private void updateUiPleaseOpenWifi() {
        mWifiUsbLlyoutSetting.setVisibility(View.INVISIBLE);
        mWifiUsbLlyoutSettingNoWifi.setVisibility(View.VISIBLE);
        mWifiUsbTvewIntroduction.setVisibility(View.INVISIBLE);
        mWifiUsbTvewIpAddress.setVisibility(View.GONE);
        mWifiUsbTvewWifiName.setText(R.string.noticeopenwifi);
        mWifiUsbIvewWifiState.setImageResource(R.drawable.wifi_state_none);
        mWifiUsbLlyoutClientList.setVisibility(View.GONE);
        mWifiUsbIvewWifiState.setVisibility(View.VISIBLE);
    }

    private void updateUiPleaseConnectAp() {

        mWifiUsbLlyoutSetting.setVisibility(View.INVISIBLE);
        mWifiUsbLlyoutSettingNoWifi.setVisibility(View.VISIBLE);
        mWifiUsbTvewIntroduction.setVisibility(View.INVISIBLE);
        mWifiUsbTvewIpAddress.setVisibility(View.GONE);
        mWifiUsbTvewWifiName.setText(R.string.noticeconnectap);
        mWifiUsbIvewWifiState.setImageResource(R.drawable.wifi_state_none);
        mWifiUsbLlyoutClientList.setVisibility(View.GONE);
        mWifiUsbIvewWifiState.setVisibility(View.VISIBLE);
    }

    public void updateUiServerRunning() {

        mWifiUsbLlyoutSetting.setVisibility(View.VISIBLE);
        mWifiUsbLlyoutSettingNoWifi.setVisibility(View.INVISIBLE);
        mWifiUsbBtnSetup.setEnabled(true);
        mWifiUsbBtnSetup.setText(R.string.stop);
        mWifiUsbTvewWifiName.setText(WifiUtil.getWifiSSID(this));
        mWifiUsbIvewWifiState.setImageResource(R.drawable.wifi_state_full);
        mWifiUsbIvewWifiState.setVisibility(View.VISIBLE);
        mWifiUsbLlyoutClientList.setVisibility(View.GONE);
        mWifiUsbTvewIntroduction.setVisibility(View.VISIBLE);
        mWifiUsbTvewIpAddress.setVisibility(View.VISIBLE);
        mWifiUsbTvewIpAddress.setText("ftp://"
                + WifiUtil.getLocalWifiIpAddress(this) + ":"
                + mWifiUsbSingleton.running_port + "/");
        mWifiUsbTvewIntroduction.setText(R.string.instruction);

    }

    private void updateUiServerStoped() {

        mWifiUsbLlyoutSetting.setVisibility(View.VISIBLE);
        mWifiUsbLlyoutSettingNoWifi.setVisibility(View.INVISIBLE);
        mWifiUsbTvewWifiName.setText(WifiUtil.getWifiSSID(this));
        mWifiUsbIvewWifiState.setImageResource(R.drawable.wifi_state_full);
        mWifiUsbTvewIntroduction.setVisibility(View.VISIBLE);
        mWifiUsbTvewIpAddress.setVisibility(View.GONE);
        mWifiUsbLlyoutClientList.setVisibility(View.GONE);
        mWifiUsbIvewWifiState.setVisibility(View.VISIBLE);
        mWifiUsbTvewIntroduction.setText(R.string.instruction_stopstate);
        mWifiUsbBtnSetup.setText(R.string.start);
        mWifiUsbBtnSetup.setEnabled(true);
    }

    private void updateUiClientConnected() {

        mWifiUsbLlyoutSetting.setVisibility(View.VISIBLE);
        mWifiUsbLlyoutSettingNoWifi.setVisibility(View.INVISIBLE);
        mWifiUsbBtnSetup.setText(R.string.stop);
        mWifiUsbBtnSetup.setEnabled(true);
        mWifiUsbTvewWifiName.setText(WifiUtil.getWifiSSID(this));
        mWifiUsbIvewWifiState.setImageResource(R.drawable.wifi_state_full);
        mWifiUsbTvewIntroduction.setVisibility(View.VISIBLE);
        mWifiUsbTvewIntroduction.setText(R.string.instruction);
        mWifiUsbTvewIpAddress.setVisibility(View.VISIBLE);
        mWifiUsbTvewIpAddress.setText("ftp://"
                + WifiUtil.getLocalWifiIpAddress(this) + ":"
                + mWifiUsbSingleton.port + "/");
        mWifiUsbLlyoutClientList.setVisibility(View.VISIBLE);
        mWifiUsbIvewWifiState.setVisibility(View.GONE);
        mClientListAdapter.refreshList(mWifiUsbSingleton.clientList);
        //mWifiUsbLvewClientList.setAdapter(mClientListAdapter);
        mClientListAdapter.notifyDataSetChanged();
    }

    private void noticeLegalPort() {
        Toast.makeText(getApplicationContext(), R.string.notice_change_port,
                Toast.LENGTH_LONG).show();
        mWifiUsbSingleton.mIllegalPort = false;
    }

    private void noticePhoneMemorryFull() {
        invokeServiceDelegate("stopFtpServerService");
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setNeutralButton(R.string.ok, null);
        adBuilder.setMessage(R.string.phone_memory_full);
        adBuilder.create().show();
    }

    private void setActionBarView() {
        // 若支持ActionBar

        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setDisplayShowHomeEnabled(false);
            bar.setDisplayShowTitleEnabled(false);
            bar.setDisplayUseLogoEnabled(false);
            bar.setDisplayShowCustomEnabled(true);
            bar.setCustomView(R.layout.common_actionbar);

            // 默认退出当前页面
            Button btn = (Button) findViewById(R.id.actionbar_back);
            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            // 默认标题栏视图
            TextView tvewTitle = (TextView) findViewById(R.id.actionbar_title);
            PackageManager pm = getPackageManager();
            ResolveInfo info = pm.resolveActivity(getIntent(),
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (info != null) {
                tvewTitle.setText((String) info.loadLabel(pm));
            }
            btn.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        unregisterWifiReceiver();
        unRegisterServiceReceiver();
        super.onDestroy();
    }


    
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // TODO Auto-generated method stub 
    }

}
