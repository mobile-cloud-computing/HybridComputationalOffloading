package ee.ut.cs.d2d.data;

/**
 * Created by hflores on 27/11/15.
 */

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ee.ut.cs.d2d.framework.R;


public class DeviceListAdapter extends BaseAdapter {
    private Activity mContext;
    private List<String> mList;
    private LayoutInflater mLayoutInflater = null;

    public DeviceListAdapter(Activity context, DeviceData D2DPeers, String nInterface) {
        mContext = context;
        mList = D2DPeers.getPeers(nInterface);
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        DeviceListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.device_list_layout, null);
            viewHolder = new DeviceListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (DeviceListViewHolder) v.getTag();
        }
        viewHolder.mTVItem.setText(mList.get(position));
        return v;
    }
}
class DeviceListViewHolder {
    public TextView mTVItem;
    public DeviceListViewHolder(View base) {
        mTVItem = (TextView) base.findViewById(R.id.listTV);
    }
}