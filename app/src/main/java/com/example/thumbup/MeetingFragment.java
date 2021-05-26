package com.example.thumbup;

import android.content.DialogInterface;
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

import com.example.thumbup.DataBase.DBManager;

import java.util.ArrayList;
import java.util.List;

public class MeetingFragment extends Fragment {
    ListView meetingNoticeListView;
    ListView meetingListView;
    TextView meetingAddNotice;
    TextView meetingAddSchedule;
    List<String> noticeList= new ArrayList<>(); //공지목록
    ArrayList<String> meetingNoticeList = new ArrayList<>();
    DBManager dbManager = DBManager.getInstance();

//    void showNotice() { //공지 보여주는 것
//        noticeList = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").notices;
//        for (int i = 0; i < noticeList.size(); i++) {
//            meetingNoticeList.add(noticeList.get(i));
//        }
//        ArrayAdapter meetingNoticeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, meetingNoticeList);
//        meetingNoticeListView.setAdapter(meetingNoticeAdapter);
//   }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View meetingView = inflater.inflate(R.layout.meeting, container, false);

        meetingNoticeListView = (ListView) meetingView.findViewById(R.id.meeting_noticeList);
        meetingListView = (ListView) meetingView.findViewById(R.id.meeting_list);
        meetingAddNotice = (TextView) meetingView.findViewById(R.id.meeting_addNotice);
        meetingAddSchedule = (TextView) meetingView.findViewById(R.id.meeting_addSchedule);
        ArrayList<MeetingListViewItem> meetingListViewItem = new ArrayList<>();

        //처음 화면 로드시 존재하는 공지 목록 띄우기
//        showNotice();

        meetingAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingNoticeDialog meetingNoticeDialog = new MeetingNoticeDialog(getActivity());
                meetingNoticeDialog.show();
                meetingNoticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //공지 추가
//                        showNotice();
                    }
                });
            }
        });

        meetingAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingScheduleDialog meetingScheduleDialog = new MeetingScheduleDialog(getActivity());
                meetingScheduleDialog.show();
                MeetingScheduleDialog.setOnDismissListener() {
                    //일정 관련
                    List<String> meetingListViewItem_date = new ArrayList<>();
                    List<String> meetingListViewItem_name = new ArrayList<>();
                    List<String> meetingListViewItem_time = new ArrayList<>();
                    List<String> meetingListViewItem_place = new ArrayList<>();
                    for (int i = 0; i < meetingListViewItem.size(); i++) {
                        meetingListViewItem_date.add(meetingListViewItem_date.get(i));
                    }
                    for (int i = 0; i < meetingListViewItem.size(); i++) {
                        MeetingListViewItem item = new MeetingListViewItem();
                        item.MeetingListViewItem_date = meetingListViewItem_date.get(i);
                        item.MeetingListViewItem_name = meetingListViewItem_name.get(i);
                        item.MeetingListViewItem_time = meetingListViewItem_time.get(i);
                        item.MeetingListViewItem_place = meetingListViewItem_place.get(i);
                        meetingListViewItem.add(item);
                    }
                    MeetingAdapter meetingAdapter = new MeetingAdapter(meetingListViewItem);
                    meetingListView.setAdapter(meetingAdapter);
                }
            }
        });
        return meetingView;
    }
}
