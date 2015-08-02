package com.huaqin.wifiusb;

import java.util.ArrayList;

import com.huaqin.wifiusb.db.ClientUserSessionItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @ClassName:ClientListAdapter
 * @Description:用户列表的适配器
 * @author:xuqiang
 * @date:2014年9月25日
 */
public class ClientListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ClientUserSessionItem> clientList;
    private String user;
    public static final int TRANSFER_START = 0;
    public static final int TRANSFER_END = 1;
    public static final int TRANSFER_NO = 2;
    
    public ClientListAdapter(Context context, ArrayList<ClientUserSessionItem> clientList,String user) {
        super();
        this.context = context;
        this.clientList = clientList;
        this.user = user;
    }

    public void setUser(String user){
        this.user = user;
    }
    
    public void refreshList(ArrayList<ClientUserSessionItem> clientList){
        this.clientList = clientList;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return clientList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.client_item, null);
            convertView.setTag(convertView);
        }else{
            convertView = (View)convertView.getTag();
        }
        TextView mWifiUsbTvewClientItemUser = (TextView)convertView.findViewById(R.id.client_item_user);
        TextView mWifiUsbTvewClientItemAddress = (TextView)convertView.findViewById(R.id.client_item_address);
        TextView mWifiUsbTvewClientItemState = (TextView)convertView.findViewById(R.id.client_item_state);
        mWifiUsbTvewClientItemUser.setText(user);
        mWifiUsbTvewClientItemAddress.setText(clientList.get(position).ipAddress.substring(1));  //去掉前面的斜杠
        int currentdataTransferState = clientList.get(position).dataTransferState;
        String mTransferState = null;
        switch(currentdataTransferState){
            case TRANSFER_START:
                mTransferState = context.getResources().getString(R.string.file_transfering);
                break;
            case TRANSFER_END:
            case TRANSFER_NO:
                mTransferState = context.getResources().getString(R.string.connected);
                break;
        }
        mWifiUsbTvewClientItemState.setText(mTransferState);
        return convertView;

    }

}
