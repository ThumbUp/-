package com.example.thumbup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.thumbup.DataBase.DBManager;
import com.example.thumbup.DataBase.Meeting;
import com.example.thumbup.DataBase.Schedule;

import java.util.ArrayList;
import java.util.List;

public class MeetingFragment extends Fragment {
    ListView meetingNoticeListView;
    ListView meetingListView;
    TextView meetingAddNotice;
    TextView meetingAddSchedule;
    TextView meetingTitle;
    List<String> noticeList = new ArrayList<>(); //공지목록
    ArrayList<String> meetingNoticeList = new ArrayList<>();
    List<Meeting> dbUserMeetingList = new ArrayList<>();
    DBManager dbManager = DBManager.getInstance();
    ImageButton showSchedule;

    void showNotice() { //공지 보여주는 것
        noticeList = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").notices;
        for (int i = 0; i < noticeList.size(); i++) {
            meetingNoticeList.add(noticeList.get(i));
        }
        ArrayAdapter meetingNoticeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, meetingNoticeList);
        meetingNoticeListView.setAdapter(meetingNoticeAdapter);
   }

   void popup_meeting(List _userMeetingList) {
       PopupMenu meetingpopup = new PopupMenu(getActivity().getApplicationContext(), meetingTitle);
       Menu meetingMenu = meetingpopup.getMenu();
       for (int i = 0; i < _userMeetingList.size(); i++) {
           //모임 가져와서 넣기
       }
   }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View meetingView = inflater.inflate(R.layout.meeting, container, false);

        meetingTitle = (TextView) meetingView.findViewById(R.id.meeting_title);
        meetingNoticeListView = (ListView) meetingView.findViewById(R.id.meeting_noticeList);
        meetingListView = (ListView) meetingView.findViewById(R.id.meeting_list);
        meetingAddNotice = (TextView) meetingView.findViewById(R.id.meeting_addNotice);
        meetingAddSchedule = (TextView) meetingView.findViewById(R.id.meeting_addSchedule);
        ArrayList<MeetingListViewItem> meetingListViewItem = new ArrayList<>();
        showSchedule = (ImageButton) meetingView.findViewById(R.id.showSchedule);

        //처음 화면 로드시 존재하는 공지 목록 띄우기
        showNotice();

        meetingAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingNoticeDialog meetingNoticeDialog = new MeetingNoticeDialog(getActivity());
                meetingNoticeDialog.show();
                meetingNoticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //공지 추가
                        showNotice();
                    }
                });
            }
        });

        meetingAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingScheduleDialog meetingScheduleDialog = new MeetingScheduleDialog(getActivity());
                meetingScheduleDialog.show();
                meetingScheduleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        List<Schedule> dbMeetingListViewItem = new ArrayList<>();
                        dbMeetingListViewItem = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules;
                        for (int i = 0; i < dbMeetingListViewItem.size(); i++) {
                            MeetingListViewItem item = new MeetingListViewItem();
                            item.MeetingListViewItem_date = dbMeetingListViewItem.get(i).date;
                            item.MeetingListViewItem_name = dbMeetingListViewItem.get(i).title;
                            item.MeetingListViewItem_time = dbMeetingListViewItem.get(i).time;
                            item.MeetingListViewItem_place = dbMeetingListViewItem.get(i).place;
                            meetingListViewItem.add(item);
                        }
                        MeetingAdapter meetingAdapter = new MeetingAdapter(meetingListViewItem);
                        meetingListView.setAdapter(meetingAdapter);
                    }
                });
            }
        });

        showSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //모임 목록 뜨게끔
               List<String> dbMeetingList = new ArrayList<>();
               dbUserMeetingList = (ArrayList<Meeting>) dbManager.participatedMeetings;
               for( String key : dbManager.participatedMeetings.keySet() ){
                    dbUserMeetingList.add(dbManager.participatedMeetings.get(key));
               }
            }
        });
        return meetingView;
    }
}