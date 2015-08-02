package com.huaqin.wifiusb.db;

public class Const {
    public static final String KEY_ANONYMOUS_PREFERENCE = "anonymous_preference";
    public static final String KEY_USERNAME_PREFERENCE = "username_preference";
    public static final String KEY_PASSWORD_PREFERENCE = "password_preference";
    public static final String KEY_PORT_PREFERENCE = "port_preference";
    public static final String KEY_WRITEPERMISSION_PREFERENCE = "writepermission_perference";
    public static final boolean DEFAULT_ANONYMOUS_PREFERENCE = true;
    public static final String DEFAULT_USERNAME_PREFERENCE = "admin";
    public static final String DEFAULT_PASSWORD_PREFERENCE = "123456";
    public static final String DEFAULT_PORT_PREFERENCE = "2222";
    public static final boolean DEFAULT_WRITEPERMISSION_PREFERENCE = false;

    public static final int TRANSFER_START = 0;
    public static final int TRANSFER_END = 1;
    public static final int TRANSFER_NO = 2;
    public static final int TRANSFERING = 3;
    public static final int CLIENT_REFRESH = 4;
    public static final int CLIENT_NONE = 5;
    public static final int SERVER_FULL = 6;

    public static final int MAX_POSITION = 50;

    public static final int TIME_PEROID = 500;
    public static final int TIME_DELAY = 500;
    public static final int TIME_COUNT = 6;

    public static final int UI_STATE_NO_WIFI = 0;

    public static final int UI_STATE_WIFI_NOCONNECTED = 1;

    public static final int UI_STATE_RUNNING = 2;

    public static final int UI_STATE_STOPED = 3;

    public static final int UI_STATE_CLIENT_CONNECTED = 4;

    public static final int UI_UPDATE = 5;

    public final static String DATA_TRANSFER_STATE_START = "transfer start";
    public final static String DATA_TRANSFER_STATE_ED = "transfered";
    public final static String DATA_TRANSFER_STATE_DEFAULT = "transfer default";
}
