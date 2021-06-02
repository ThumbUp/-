package com.example.thumbup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thumbup.DataBase.DBCallBack;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MeetingAdapter extends BaseAdapter implements Serializable  {
    LayoutInflater meetingAdapterInflater = null;
    private ArrayList<MeetingListViewItem> meetingListViewItem = null;
    private int meetingListViewItemCount = 0;
    String meetID = "";
    public MeetingAdapter adapter = this;

    public MeetingAdapter(ArrayList<MeetingListViewItem> _meetingListViewItem, String meetingid) {
        meetingListViewItem = _meetingListViewItem;
        meetingListViewItemCount = _meetingListViewItem.size();
        meetID = meetingid;
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
        return position;
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

        LinearLayout clickedListView = (LinearLayout) convertView.findViewById(R.id.meeting_listview_schedule);
        clickedListView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long click_position = getItemId(position);
                String s = ""+click_position;
                Log.e("Click list ID : ", s);

                Intent intent = new Intent(getApplicationContext(), SubSchedule.class);
                intent.putExtra("ListID", s);
                intent.putExtra("MeetID", meetID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }
        });
        return convertView;
    }
}