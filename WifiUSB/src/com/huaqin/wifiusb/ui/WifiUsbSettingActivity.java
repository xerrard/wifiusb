package com.huaqin.wifiusb.ui;

import org.xerrard.util.InputFilterUtil;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.huaqin.wifiusb.R;
import com.huaqin.wifiusb.WifiUsbApplication;

/**
 * 
 * @ClassName:WifiUsbSettingActivity
 * @Description:设置界面
 * @author:xuqiang
 * @date:2014年7月29日
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class WifiUsbSettingActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener, OnPreferenceChangeListener {
    public static final String KEY_ANONYMOUS_PREFERENCE = "anonymous_preference";
    public static final String KEY_USERNAME_PREFERENCE = "username_preference";
    public static final String KEY_PASSWORD_PREFERENCE = "password_preference";
    public static final String KEY_PORT_PREFERENCE = "port_preference";
    public static final String KEY_WRITEPERMISSION_PREFERENCE = "writepermission_perference";
    public static final String DEFAULT_SUMMARY_ANONYMOUS_PREFERENCE = "anonymous login";
    public static final String DEFAULT_SUMMARY_USERNAME_PREFERENCE = "admin";
    public static final String DEFAULT_SUMMARY_PASSWORD_PREFERENCE = "admin";
    public static final String DEFAULT_SUMMARY_PORT_PREFERENCE = "2222";
    public static final String DEFAULT_SUMMARY_WRITEPERMISSION_PREFERENCE = "have the writepermission?";
    private EditTextPreference mUsernamePreference;
    private EditTextPreference mPasswordtPreference;
    private EditTextPreference mPortPreference;
    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private EditText mPortEt;

    private WifiUsbApplication mWifiUsbApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        bindActionBarView();
        mWifiUsbApplication = (WifiUsbApplication) getApplication();
        mWifiUsbApplication.addActivity(this);
        addPreferencesFromResource(R.xml.settings);

        mUsernamePreference = (EditTextPreference) getPreferenceScreen()
                .findPreference(KEY_USERNAME_PREFERENCE);
        mUsernameEt = mUsernamePreference.getEditText();
        mUsernameEt
                .setFilters(new InputFilter[] { new InputFilter.LengthFilter(20),new InputFilterUtil.InputFilterSpace() }); // 名字长度最大20个
        mUsernamePreference.setOnPreferenceChangeListener(this);

        
        
        mPasswordtPreference = (EditTextPreference) getPreferenceScreen()
                .findPreference(KEY_PASSWORD_PREFERENCE);
        mPasswordEt = mPasswordtPreference.getEditText();
        mPasswordEt
                .setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) }); // 密码长度最大10个
        mPasswordEt.setKeyListener(new DigitsKeyListener(false, true)); // port只允许输入数字
        mPasswordtPreference.setOnPreferenceChangeListener(this);

        mPortPreference = (EditTextPreference) getPreferenceScreen()
                .findPreference(KEY_PORT_PREFERENCE);
        mPortEt = mPortPreference.getEditText();
        String digits = "1234567890";
        mPortEt.setKeyListener(DigitsKeyListener.getInstance(digits));
        mPortEt.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) }); // 长度最大5个
        
        mPortPreference.setOnPreferenceChangeListener(this);
    }

    

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        mUsernamePreference.setSummary(getPreferenceScreen()
                .getSharedPreferences().getString(KEY_USERNAME_PREFERENCE, "")
                .isEmpty() ? DEFAULT_SUMMARY_USERNAME_PREFERENCE
                : getPreferenceScreen().getSharedPreferences().getString(
                        KEY_USERNAME_PREFERENCE, ""));
        mPasswordtPreference.setSummary(getPreferenceScreen()
                .getSharedPreferences().getString(KEY_PASSWORD_PREFERENCE, "")
                .isEmpty() ? DEFAULT_SUMMARY_PASSWORD_PREFERENCE
                : getPreferenceScreen().getSharedPreferences().getString(
                        KEY_PASSWORD_PREFERENCE, ""));
        mPortPreference
                .setSummary(getPreferenceScreen().getSharedPreferences()
                        .getString(KEY_PORT_PREFERENCE, "").isEmpty() ? DEFAULT_SUMMARY_PORT_PREFERENCE
                        : getPreferenceScreen().getSharedPreferences()
                                .getString(KEY_PORT_PREFERENCE, ""));

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        Preference tmp_pre;
        if (preference instanceof EditTextPreference) {
            tmp_pre = (EditTextPreference) preference;
            EditText ed = ((EditTextPreference) tmp_pre).getEditText();
            ed.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);;
            Editable etable = ed.getText();
            ed.setSelection(etable.length());// 光标置到EditText最后的位置
            
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (key.equals(KEY_USERNAME_PREFERENCE)) {
            mUsernamePreference.setSummary(sharedPreferences.getString(key, "")
                    .isEmpty() ? DEFAULT_SUMMARY_USERNAME_PREFERENCE
                    : sharedPreferences.getString(key, ""));
        }
        else if (key.equals(KEY_PASSWORD_PREFERENCE)) {
            mPasswordtPreference
                    .setSummary(sharedPreferences.getString(key, "").isEmpty() ? DEFAULT_SUMMARY_PASSWORD_PREFERENCE
                            : sharedPreferences.getString(key, ""));
        }
        else if (key.equals(KEY_PORT_PREFERENCE)) {
            mPortPreference.setSummary(sharedPreferences.getString(key, "")
                    .isEmpty() ? DEFAULT_SUMMARY_PORT_PREFERENCE
                    : sharedPreferences.getString(key, ""));
        }

    }

    /**
     * <p>
     * Description:actionbar设置
     * <p>
     * 
     * @date:2014年9月25日
     */
    private void bindActionBarView() {
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(false);
            bar.setTitle(R.string.setting);
        }
    }

    /**
     * 对edittext的内容做判断，如果是空则取消内容变化
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String str = (String) newValue;
        if (str.equals("")) {
            Toast.makeText(this, R.string.notice_edittext_empty,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals(KEY_PORT_PREFERENCE)) {
                    int port = Integer.parseInt(str);
                    if (port <= 1024 || port >= 65535) {
                        Toast.makeText(this, R.string.notice_change_port,
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }

            return true;
        }

    }

}
