package com.example.thumbup;

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

public class MeetingNoticeDialog extends Dialog {
    public MeetingNoticeDialog(@NonNull Context context) {
        super(context);
    }

    Button dialogSave;
    Button dialogBack;
    EditText noticeContent;
    String toAddNotice;
    DBManager dbManager = DBManager.getInstance();
    private OnDismissListener onDismissListener = null;

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
                Log.e("notice - ", "update before");
                dbManager.UpdateMeeting("-MaZIcU6ZjxsYF_iX-6k", new DBCallBack() {
                    @Override
                    public void success(Object data) {
                        dismiss();
                        Log.e("notice - ", "success");
                    }

                    @Override
                    public void fail(String errorMessage) {
                        dismiss();
                        Log.e("notice - ", "fail");
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
