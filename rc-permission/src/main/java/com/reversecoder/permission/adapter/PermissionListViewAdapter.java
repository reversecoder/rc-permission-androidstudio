package com.reversecoder.permission.adapter;

import android.Manifest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reversecoder.permission.R;
import com.reversecoder.permission.model.ManifestPermission;
import com.reversecoder.permission.permissify.PermissionCallOptions;

import java.util.ArrayList;

/**
 * Created by rashed on 4/4/17.
 */

public class PermissionListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ManifestPermission> mData;
    private static LayoutInflater inflater = null;

    public PermissionListViewAdapter(Context context, ArrayList<ManifestPermission> data) {
        mContext = context;
        mData = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mData.size();
    }

    public ManifestPermission getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_permission, null);

        ((TextView)vi.findViewById(R.id.tv_permission)).setText(getItem(position).getName().replace("android.permission.",""));

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getPermissifyManager().callWithPermission(mContext, CONTACTS_PERMISSION_REQUEST_ID, Manifest.permission.READ_CONTACTS,
//                        new PermissionCallOptions.Builder()
//                                .withDefaultDenyDialog(false)
//                                .withRationaleEnabled(false)
//                                .build());
            }
        });
        return vi;
    }
}