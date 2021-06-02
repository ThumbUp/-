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

import com.example.thumbup.DataBase.DBCallBack;
import com.example.thumbup.DataBase.DBManager;
import com.example.thumbup.DataBase.Meeting;
import com.example.thumbup.DataBase.Schedule;
import com.example.thumbup.DataBase.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MeetingFragment extends Fragment {
    String meetingId = ""; //선택된 모임의 아이디(=코드)
    TextView meetingName; //선택된 모임의 이름 넣을 공간
    ListView meetingNoticeListView;
    ListView meetingListView;
    TextView meetingAddNotice;
    TextView meetingAddSchedule;
    List<String> meetingIdList = new ArrayList<>(); //유저가 가입된 모임의 코드들(= 모임의 키 값)들
    List<Meeting> dbUserMeetingList = new ArrayList<>(); //모임 목록
    List<String> noticeList = new ArrayList<>(); //공지목록
    ArrayList<String> meetingNoticeList = new ArrayList<>();
    DBManager dbManager = DBManager.getInstance();
    ArrayList<MeetingListViewItem> meetingListViewItem = new ArrayList<>();

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    User my = dbManager.userData;
    boolean scheIn = false;

    public MeetingFragment(String _meetingId) {
        meetingId = _meetingId;
    }

    public MeetingFragment() {

    }
    //유저가 가입한 모임 팝업메뉴 생성

    void showMeeting() {
        PopupMenu meetingPopup = new PopupMenu(getActivity(), meetingName);
        Menu meetingMenu = meetingPopup.getMenu();
        dbUserMeetingList.clear();
        for( String key : dbManager.participatedMeetings.keySet() ){
            Log.e("LIST", "participatedMeetings" + dbManager.participatedMeetings.keySet());
            dbUserMeetingList.add(dbManager.participatedMeetings.get(key));
            Log.e("LIST","KEY " + key + "   dbUserMeetingList " + dbUserMeetingList.size());
            meetingIdList.add(key);
            Log.e("LIST","KEY " + key + "   meetingIdList " + dbUserMeetingList);
        }
        for (int i = dbUserMeetingList.size() - 1; i >= 0; i--) {
            meetingMenu.add(0, i,0, (CharSequence) dbUserMeetingList.get(i).title);
        }
        Log.d("menu", "meetingMenu.size(): " + meetingMenu.size() + "dbUserMeetingList.size()" + dbUserMeetingList.size());
        meetingPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e("meetingIndex", "meetingIndex" + item.getItemId());
                int meetingIndex = item.getItemId();
                meetingId = meetingIdList.get(item.getItemId());
                Log.e("meetingId", "meetingId: " + meetingId);
                meetingName.setText(item.getTitle().toString());
                showNotice();
                showSchedule();
                return false;
            }
        });
        meetingPopup.show();
    }

    void showNotice() { //공지 보여주는 것
        Log.d("meetingIdNotice", "meetingId: " + meetingId);
        noticeList = dbManager.participatedMeetings.get(meetingId).notices;
        meetingNoticeList.clear();
        for (int i = noticeList.size() - 1; i > 0; i--) {
            meetingNoticeList.add(noticeList.get(i));
        }
        Log.d("meetingNoticeList", "meetingNoticeList: " + meetingNoticeList);
        ArrayAdapter meetingNoticeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, meetingNoticeList);
        meetingNoticeListView.setAdapter(meetingNoticeAdapter);

    }

    void showSchedule(){
        ArrayList<MeetingListViewItem> meetingListViewItem = new ArrayList<>();
        List<Schedule> dbMeetingListViewItem = new ArrayList<>();
        dbMeetingListViewItem = dbManager.participatedMeetings.get(meetingId).schedules;
        for (int i = 0; i < dbMeetingListViewItem.size(); i++) {
            Log.e("DB SIZE", dbMeetingListViewItem.size()+"");
            MeetingListViewItem item = new MeetingListViewItem();
            item.MeetingListViewItem_date = dbMeetingListViewItem.get(i).date;
            item.MeetingListViewItem_name = dbMeetingListViewItem.get(i).title;
            item.MeetingListViewItem_time = dbMeetingListViewItem.get(i).time;
            //item.MeetingListViewItem_place = dbMeetingListViewItem.get(i).place;

            scheIn = false;
            int i_size = dbManager.participatedMeetings.get(meetingId).schedules.get(i).members.size();
            Log.e("I SIZE", i_size+"  ");
            for(int i2 = 0; i2 < i_size; i2++){
                Log.e("I / I2", i+"  "+i2);
                List<User> users = dbManager.participatedMeetings.get(meetingId).schedules.get(i).members;
                if(users.get(i2).email.equals(my.email) == true) {
                    scheIn = true;
                }
            }
            if(scheIn == true){
                item.MeetingListViewItem_place = "참여";
            }
            else{
                item.MeetingListViewItem_place = "미참여";
            }

            meetingListViewItem.add(item);
        }
        MeetingAdapter meetingAdapter = new MeetingAdapter(meetingListViewItem, meetingId);
        meetingListView.setAdapter(meetingAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View meetingView = inflater.inflate(R.layout.meeting, container, false);

        meetingName = (TextView) meetingView.findViewById(R.id.meeting_name);
        meetingNoticeListView = (ListView) meetingView.findViewById(R.id.meeting_noticeList);
        meetingListView = (ListView) meetingView.findViewById(R.id.meeting_list);
        meetingAddNotice = (TextView) meetingView.findViewById(R.id.meeting_addNotice);
        meetingAddSchedule = (TextView) meetingView.findViewById(R.id.meeting_addSchedule);
        ArrayList<MeetingListViewItem> meetingListViewItem = new ArrayList<>();

        //처음 화면 로드시 존재하는 공지/일정 목록 띄우기
        if (meetingId != "") {
            showNotice();
            showSchedule();
        }

        meetingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMeeting();
            }
        });

        meetingAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetingId != "") {
                    MeetingNoticeDialog meetingNoticeDialog = new MeetingNoticeDialog(getActivity(), meetingId);
                    meetingNoticeDialog.show();
                    meetingNoticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //공지 추가
                            showNotice();
                        }
                    });
                }
            }
        });

        meetingAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetingId != "") {
                    MeetingScheduleDialog meetingScheduleDialog = new MeetingScheduleDialog(getActivity(), meetingId);
                    meetingScheduleDialog.show();
                    meetingScheduleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            showSchedule();
                        }
                    });
                }

            }
        });

//        meetingListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), SubSchedule.class);
//                intent.putExtra("meetingId", meetingId);
//                getApplicationContext().startActivity(intent);
//            }
//        });
        return meetingView;
    }
}