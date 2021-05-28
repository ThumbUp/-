package com.example.thumbup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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

import static com.facebook.FacebookSdk.getApplicationContext;

public class MeetingFragment extends Fragment {
    String meetingId = ""; //선택된 모임의 아이디(=코드)
    TextView meetingName; //선택된 모임의 이름 넣을 공간
    LinearLayout meetingUserMeetingList;
    ListView meetingNoticeListView;
    ListView meetingListView;
    TextView meetingAddNotice;
    TextView meetingAddSchedule;
    List<String> meetingIdList = new ArrayList<>(); //유저가 가입된 모임의 코드들(= 모임의 키 값)들
    List<Meeting> dbUserMeetingList = new ArrayList<>(); //모임 목록
    List<String> noticeList = new ArrayList<>(); //공지목록
    ArrayList<String> meetingNoticeList = new ArrayList<>();
    DBManager dbManager = DBManager.getInstance();

    public MeetingFragment(String _meetingId) {
        meetingId = _meetingId;
    }

    public MeetingFragment() {

    }

    void showMeeting() {
        for( String key : dbManager.participatedMeetings.keySet() ){
            dbUserMeetingList.add(dbManager.participatedMeetings.get(key));
            meetingIdList.add(key);
        }
        PopupMenu meetingPopup = new PopupMenu(getActivity().getApplicationContext(), meetingUserMeetingList);
        Menu meetingMenu = meetingPopup.getMenu();
        for (int i = 0; i < dbUserMeetingList.size(); i++) {
            meetingMenu.add((CharSequence) dbUserMeetingList.get(i).title);
        }
        meetingPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int meetingIndex = item.getItemId();
                meetingId = meetingIdList.get(item.getItemId());
                Log.d("meetingId", "meetingId: " + meetingId);
                meetingName.setText((CharSequence) meetingMenu.getItem(meetingIndex));
                return false;
            }
        });
        meetingPopup.show();
    }

    void showNotice() { //공지 보여주는 것
        noticeList = dbManager.participatedMeetings.get(meetingId).notices;
        for (int i = 0; i < noticeList.size(); i++) {
            meetingNoticeList.add(noticeList.get(i));
        }
        ArrayAdapter meetingNoticeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, meetingNoticeList);
        meetingNoticeListView.setAdapter(meetingNoticeAdapter);
    }

    void showSchedule(){
        ArrayList<MeetingListViewItem> meetingListViewItem = new ArrayList<>();

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

    void showNoticeAdd(){
        noticeList = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").notices;
        meetingNoticeList.add(noticeList.get(noticeList.size()-1));
        ArrayAdapter meetingNoticeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, meetingNoticeList);
        meetingNoticeListView.setAdapter(meetingNoticeAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View meetingView = inflater.inflate(R.layout.meeting, container, false);

        meetingName = (TextView) meetingView.findViewById(R.id.meeting_name);
        meetingUserMeetingList = (LinearLayout) meetingView.findViewById(R.id.meeting_userMeetingList);
        meetingNoticeListView = (ListView) meetingView.findViewById(R.id.meeting_noticeList);
        meetingListView = (ListView) meetingView.findViewById(R.id.meeting_list);
        meetingAddNotice = (TextView) meetingView.findViewById(R.id.meeting_addNotice);
        meetingAddSchedule = (TextView) meetingView.findViewById(R.id.meeting_addSchedule);
        ArrayList<MeetingListViewItem> meetingListViewItem = new ArrayList<>();

        //처음 화면 로드시 존재하는 공지 목록 띄우기
        if (meetingId != "") {
            showNotice();
            showSchedule();
        }

        meetingUserMeetingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMeeting();
            }
        });

        meetingAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetingId != "") {
                    MeetingNoticeDialog meetingNoticeDialog = new MeetingNoticeDialog(getActivity());
                    meetingNoticeDialog.show();
                    meetingNoticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //공지 추가
                            showNoticeAdd();
                        }
                    });
                }
            }
        });

        meetingAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetingId != "") {
                    MeetingScheduleDialog meetingScheduleDialog = new MeetingScheduleDialog(getActivity());
                    meetingScheduleDialog.show();
                    meetingScheduleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            showSchedule();

//                        List<Schedule> dbMeetingListViewItem = new ArrayList<>();
//                        dbMeetingListViewItem = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules;
//                        for (int i = 0; i < dbMeetingListViewItem.size(); i++) {
//                            MeetingListViewItem item = new MeetingListViewItem();
//                            item.MeetingListViewItem_date = dbMeetingListViewItem.get(i).date;
//                            item.MeetingListViewItem_name = dbMeetingListViewItem.get(i).title;
//                            item.MeetingListViewItem_time = dbMeetingListViewItem.get(i).time;
//                            item.MeetingListViewItem_place = dbMeetingListViewItem.get(i).place;
//                            meetingListViewItem.add(item);
//                        }
//                        MeetingAdapter meetingAdapter = new MeetingAdapter(meetingListViewItem);
//                        meetingListView.setAdapter(meetingAdapter);

                        }
                    });
                }

            }
        });

        return meetingView;
    }
}