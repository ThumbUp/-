package com.example.thumbup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    ListView main_listView;
    ImageButton btn_addMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_addMeeting = (ImageButton)findViewById(R.id.btn_addMeeting);

        MainActivity_ListAdapter adapter;
        ArrayList<MainActivity_ItemData> items = new ArrayList<MainActivity_ItemData>() ;

        adapter = new MainActivity_ListAdapter();

        main_listView = (ListView)findViewById(R.id.main_listView);
        main_listView.setAdapter(adapter);

        for(int i = 1; i < 4; i++){
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_profile), "여기 모임" + i, "스마트 모바일 프로그래밍 프로젝트 관련 모임" + i);
        }

        btn_addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popupAdd = new PopupMenu(getApplicationContext(), view);
                popupAdd.getMenuInflater().inflate(R.menu.meeting_add,popupAdd.getMenu());
                popupAdd.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_menu1){
                            final LinearLayout linear = (LinearLayout) View.inflate(MainActivity.this,
                                    R.layout.dialog_main_add_meeting, null);

                            new AlertDialog.Builder(MainActivity.this)
                                    .setView(linear)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {


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
                        }else {
                            final LinearLayout linear = (LinearLayout) View.inflate(MainActivity.this,
                                    R.layout.dialog_main_join_meeting, null);

                            new AlertDialog.Builder(MainActivity.this)
                                    .setView(linear)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {


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
    }

}