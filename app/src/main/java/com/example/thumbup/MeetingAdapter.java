package com.example.thumbup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MeetingAdapter extends BaseAdapter {
    LayoutInflater meetingAdapterInflater = null;
    private ArrayList<MeetingListViewItem> meetingListViewItem = null;
    private int meetingListViewItemCount = 0;

    public MeetingAdapter(ArrayList<MeetingListViewItem> _meetingListViewItem) {
        meetingListViewItem = _meetingListViewItem;
        meetingListViewItemCount = _meetingListViewItem.size();
    }
    @Override
    public int getCount() {
        return meetingListViewItemCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final Context MeetingAdapterContext = parent.getContext();
            if (meetingAdapterInflater == null) {
                meetingAdapterInflater = (LayoutInflater) MeetingAdapterContext.getSystemService(MeetingAdapterContext.LAYOUT_INFLATER_SERVICE);
            }
            convertView = meetingAdapterInflater.inflate(R.layout.activity_meeting_listview, parent, false);
        }

        TextView meetingListViewItem_date = (TextView) convertView.findViewById(R.id.meeting_listview_date);
        TextView meetingListViewItem_name = (TextView) convertView.findViewById(R.id.meeting_listview_name);
        TextView meetingListViewItem_time = (TextView) convertView.findViewById(R.id.meeting_listview_time);
        TextView meetingListViewItem_place = (TextView) convertView.findViewById(R.id.meeting_listview_place);

        meetingListViewItem_date.setText(meetingListViewItem.get(position).MeetingListViewItem_date);
        meetingListViewItem_name.setText(meetingListViewItem.get(position).MeetingListViewItem_name);
        meetingListViewItem_time.setText(meetingListViewItem.get(position).MeetingListViewItem_time);
        meetingListViewItem_place.setText(meetingListViewItem.get(position).MeetingListViewItem_place);
        return convertView;
    }
}
