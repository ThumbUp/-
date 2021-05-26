package com.example.thumbup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.thumbup.DataBase.DBCallBack;
import com.example.thumbup.DataBase.DBManager;

import java.util.ArrayList;

public class MeetingNoticeDialog extends Dialog {
    public MeetingNoticeDialog(@NonNull Context context) {
        super(context);
    }

    Button dialogSave;
    Button dialogBack;
    EditText noticeContent;
    String toAddNotice;
    DBManager dbManager = DBManager.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_meeting_notice);
        dialogSave = (Button) findViewById(R.id.dialog_meeting_notice_save);
        dialogBack = (Button) findViewById(R.id.dialog_meeting_notice_back);
        noticeContent = (EditText) findViewById(R.id.dialog_meeting_notice_content);

        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddNotice = noticeContent.getText().toString();
                dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").notices.add(toAddNotice);
                dbManager.UpdateMeeting("-MaZIcU6ZjxsYF_iX-6k", new DBCallBack() {
                    @Override
                    public void success(Object data) {
                        ((MeetingFragment)getContext()).noticeList = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").notices;
                        dismiss();
                    }

                    @Override
                    public void fail(String errorMessage) {
                        dismiss();
                    }
                });
            }
        });

        dialogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
