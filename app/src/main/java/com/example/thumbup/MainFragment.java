package com.example.thumbup;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class MainFragment extends Fragment {
    ListView main_listView;
    ImageButton btn_addMeeting;
    private Context mContext;
    DBManager dbManager = DBManager.getInstance();

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
            adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.ic_profile), meeting.title, meeting.info, key);
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
                            final LinearLayout linear = (LinearLayout) View.inflate(getContext(),
                                    R.layout.dialog_main_add_meeting, null);

                            AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                            dlg.setView(linear);
                            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            EditText add_meetingTitle = (EditText)((AlertDialog)dialog).findViewById(R.id.add_meetingTitle);
                                            EditText add_meetingInfo = (EditText)((AlertDialog)dialog).findViewById(R.id.add_meetingInfo);

                                            String title = add_meetingTitle.getText().toString();
                                            String info = add_meetingInfo.getText().toString();

                                            String key = dbManager.AddMeeting(title, info);

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
                            final LinearLayout linear = (LinearLayout) View.inflate(getContext(),
                                    R.layout.dialog_main_join_meeting, null);

                            new AlertDialog.Builder(getContext())
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

        return mainView;
    }
}
