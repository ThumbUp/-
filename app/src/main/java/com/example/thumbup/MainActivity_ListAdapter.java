package com.example.thumbup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.thumbup.DataBase.DBManager;
import com.example.thumbup.DataBase.Meeting;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainActivity_ListAdapter extends BaseAdapter{

    private View view;
    DBManager dbManager = DBManager.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String meetingId;
    List<String> meetingIdList = new ArrayList<>();

    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId;

    // 생성자. 마지막에 ListBtnClickListener 추가
    MainActivity_ListAdapter(Context context, int resource, ArrayList<MainActivity_ItemData> list) {
        super();

        // resource id 값 복사
        this.resourceId = resource;
    }

    private ArrayList<MainActivity_ItemData> listViewItemList = new ArrayList<MainActivity_ItemData>() ;

    public MainActivity_ListAdapter() {

    }
    public MainActivity_ListAdapter(View view) {
        this.view = view;
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        MainActivity_ListAdapter adapter;
        adapter = new MainActivity_ListAdapter();

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) view.findViewById(R.id.main_listView);
        listview.setAdapter(adapter) ;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_main_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.main_meetingIcon) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.main_meetingTitle) ;
        TextView infoTextView = (TextView) convertView.findViewById(R.id.main_meetingInfo) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MainActivity_ItemData listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getData_meetingIcon());
        titleTextView.setText(listViewItem.getData_meetingTitle());
        infoTextView.setText(listViewItem.getData_meetingInfo());

        for(String key : dbManager.participatedMeetings.keySet())
        {
            Meeting meeting = dbManager.participatedMeetings.get(key);
            meetingIdList.add(key);
        }

        ImageButton btnPopup = (ImageButton) convertView.findViewById(R.id.btnPopup);
        btnPopup.findViewById(R.id.btnPopup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_menu1){
                            //모임 나가기 아무래도 푸쉬하고 제쪽에서 보는게 편할것같네요 ㅋㅋㅋ넵 알겠습니다! 마지막으로 ㅎ나번만!..!
                            //하시고게씬가요?네 지금도 로딩중이에요...! 아하.. 그냥 푸시해주시면 제가볼게요!네!
                            final LinearLayout linear = (LinearLayout) View.inflate(getApplicationContext(),
                                    R.layout.dialog_main_delete_meeting, null);

                            new AlertDialog.Builder(getApplicationContext())
                                    .setView(linear)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            int count, checked ;
                                            count = adapter.getCount() ;

                                            if (count > 0) {
                                                // 현재 선택된 아이템의 position 획득.
                                                checked = listview.getCheckedItemPosition();

                                                if (checked > -1 && checked < count) {
                                                    // 아이템 삭제
                                                    //매번 번거로우시게 해서 죄송합니다... 아니에요! ㅎㅎㅎ
                                                    //모임의 id를 addMeeting을 통하지 않고 가져올 방법이 있을까요?
                                                    //어떤 모임의 id 말씀하시는건가요?
                                                    //선택된 모임의 id?? 넵 삭제하려는 모임의 id요!
                                                    //그 모임 추가할때
                                                    listViewItemList.remove(checked);
                                                    meetingId = listViewItem.getData_meetingId();
                                                    //네 요거아닌가요? 네 그렇게 생각하고 했는데 삭제가 안 되어서 잘못 가져왔나 했어요..!
                                                    //삭제하고나서, 이게 그 가입된 모임 탈퇴하는거죵?넵넵
                                                    //하고나서 db
                                                    deleteItem(meetingId);

                                                    // listview 선택 초기화.
                                                    listview.clearChoices();

                                                    // listview 갱신.
                                                    adapter.notifyDataSetChanged();
                                                    dbManager.participatedMeetings.remove(meetingId);
                                                    dbManager.UpdateUser();
                                                    //해보면댈것같아요! 하신건가유? 네 헉 안된건가요 그런것같아요ㅠㅠ
                                                    //이거해줘야 db에 업데이트되요!헉 감사합니다!! ㅋㅋㅋㅋ 별것도아닌데 빨리 알려드릴것그랫네요
                                                    //넘감사해요... 지금 다시 해볼게요! 네!아 잠시만요 다시해보실래요?네!
                                                    // 지금 화면에서는 없어졌는데 나갔다가 다시 들어오면 다시 생기는 상태에요
                                                    //아 방금 하셨어요??네네 다시해볼까요? 중단점이 이게 안스 버그인지먼지
                                                    //디버깅모드로해도 중단점이 안잡힐때가 많네요

                                                }
                                            }
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
                        }else if (menuItem.getItemId() == R.id.action_menu2){
                            // 모임 수정
                            Intent intent = new Intent(view.getContext(), ModifyMeetingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            view.getContext().startActivity(intent);
                        }else {
                            //모임 코드 복사
                            final LinearLayout linear = (LinearLayout) View.inflate(getApplicationContext(),
                                    R.layout.dialog_main_meeting_key, null);
                            android.app.AlertDialog.Builder dlg4 = new android.app.AlertDialog.Builder(getApplicationContext());
                            dlg4.setView(linear);
                            TextView k_meetingKey;
                            k_meetingKey = (TextView)linear.findViewById(R.id.k_meetingKey);
                            //k_meetingKey.setText(key);
                            dlg4.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    view.getContext().startActivity(intent);

                                    dialog.dismiss();
                                }
                            }).show();
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        return convertView;
    }

    public void addItem(Drawable icon, String title, String info, String mid) {
        MainActivity_ItemData item = new MainActivity_ItemData();

        item.setData_meetingIcon(icon);
        item.setData_meetingTitle(title);
        item.setData_meetingInfo(info);
        item.setData_meetingId(mid);

        listViewItemList.add(item);
    }

    private void deleteItem(String meetingId){
        mDatabase.child("Users").child(dbManager.uid).child("meetings").child(meetingId).removeValue();
    }


}