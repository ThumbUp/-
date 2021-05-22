package com.example.thumbup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MeetingFragment extends Fragment {
    ListView meetingNoticeListView;
    ListView meetingListView;
    TextView meetingAddNotice;
    TextView meetingAddSchedule;
    ArrayList<String> noticeList= new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View meetingView = inflater.inflate(R.layout.meeting, container, false);

        meetingNoticeListView = (ListView) meetingView.findViewById(R.id.meeting_noticeList);
        meetingListView = (ListView) meetingView.findViewById(R.id.meeting_list);
        meetingAddNotice = (TextView) meetingView.findViewById(R.id.meeting_addNotice);
        meetingAddSchedule = (TextView) meetingView.findViewById(R.id.meeting_addSchedule);
        ArrayList<String> meetingNoticeList = new ArrayList<>();
        ArrayList<MeetingListViewItem> meetingListViewItem = new ArrayList<>();

        String[] meetingListViewItem_date = {"4/1", "4/8", "4/15", "4/22", "4/29", "5/6", "5/13", "5/20"};
        String meetingListViewItem_name = "정기모임";
        String meetingListViewItem_time = "미정";
        String meetingListViewItem_place = "성신여대";

        for (int i = 0; i < 8; i++) {
            MeetingListViewItem item = new MeetingListViewItem();
            item.MeetingListViewItem_date = meetingListViewItem_date[i];
            item.MeetingListViewItem_name = meetingListViewItem_name;
            item.MeetingListViewItem_time = meetingListViewItem_time;
            item.MeetingListViewItem_place = meetingListViewItem_place;
            meetingListViewItem.add(item);
        }

        meetingAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingNoticeDialog meetingNoticeDialog = new MeetingNoticeDialog(getActivity());
                meetingNoticeDialog.show();
//                Log.d("notice", "공지 목록 : " + meetingNoticeDialog.noticeList);
//                for (int i = 0; i < meetingNoticeDialog.noticeList.size(); i++) {
//                    meetingNoticeList.add(meetingNoticeDialog.noticeList.get(i));
//                }
            }
        });

        meetingAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingScheduleDialog meetingScheduleDialog = new MeetingScheduleDialog(getActivity());
                meetingScheduleDialog.show();
            }
        });

        ArrayAdapter meetingNoticeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, meetingNoticeList);
        meetingNoticeListView.setAdapter(meetingNoticeAdapter);
        MeetingAdapter meetingAdapter = new MeetingAdapter(meetingListViewItem);
        meetingListView.setAdapter(meetingAdapter);
        return meetingView;
    }
}
