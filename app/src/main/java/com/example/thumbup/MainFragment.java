package com.example.thumbup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.thumbup.DataBase.DBManager;
import com.example.thumbup.DataBase.Meeting;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    ListView main_listView;
    ImageButton btn_addMeeting;
    private Context mContext;
    DBManager dbManager = DBManager.getInstance();
    //유정 수정
    String meetingId; //선택된 모임의 아이디(=코드)
    List<String> meetingIdList = new ArrayList<>(); //유저가 가입된 모임의 코드들(= 모임의 키 값)들


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_main, container, false);

        btn_addMeeting = (ImageButton) mainView.findViewById(R.id.btn_addMeeting);

        MainActivity_ListAdapter adapter;
        ArrayList<MainActivity_ItemData> items = new ArrayList<MainActivity_ItemData>() ;

        adapter = new MainActivity_ListAdapter();

        main_listView = (ListView) mainView.findViewById(R.id.main_listView);
        main_listView.setAdapter(adapter);

        for(String key : dbManager.participatedMeetings.keySet())
        {
            Meeting meeting = dbManager.participatedMeetings.get(key);
            byte[] b = binaryStringToByteArray(meeting.image);
            ByteArrayInputStream is = new ByteArrayInputStream(b);
            Drawable profile = Drawable.createFromStream(is, "profile");

            adapter.addItem(profile, meeting.title, meeting.info, key);
            //유정 수정
            meetingIdList.add(key);
        }

        btn_addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popupAdd = new PopupMenu(getContext(), view);
                popupAdd.getMenuInflater().inflate(R.menu.meeting_add,popupAdd.getMenu());
                popupAdd.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_menu1){
                            Intent intent = new Intent(getContext(), AddMeetingActivity.class);
                            startActivity(intent);
                        }else {
                            final LinearLayout linear = (LinearLayout) View.inflate(getContext(),
                                    R.layout.dialog_main_join_meeting, null);

                            AlertDialog.Builder dlg2 = new AlertDialog.Builder(getContext());
                            dlg2.setView(linear);
                            dlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    EditText meetingKey = (EditText) linear.findViewById(R.id.meetingKey);
                                    String meeting_key = meetingKey.getText().toString();

                                    dbManager.JoinMeeting(meeting_key);
                                    dialog.dismiss();
                                }
                            })
                                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                                }

                        return false;
                    }
                });
                popupAdd.show();
            }
        });

        //유정 수정
        main_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                meetingId = meetingIdList.get(position);
                ((HomeActivity)getActivity()).replaceFragment(new MeetingFragment(meetingId));
            }
        });
        return mainView;

    }

    // 스트링을 바이너리 바이트 배열로
    private byte[] binaryStringToByteArray (String s){
        int count = s.length() / 8;
        byte[] b = new byte[count];
        for (int i = 1; i < count; ++i) {
            String t = s.substring((i - 1) * 8, i * 8);
            b[i - 1] = binaryStringToByte(t);
        }
        return b;
    }
    // 스트링을 바이너리 바이트로
    private byte binaryStringToByte (String s){
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }
}
