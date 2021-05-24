package com.example.thumbup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

public class MeetingScheduleDialog extends Dialog {
    public MeetingScheduleDialog(@NonNull Context context) {
        super(context);
    }
    Button dialogSave;
    Button dialogBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_meeting_schedule);
        dialogSave = (Button) findViewById(R.id.dialog_meeting_notice_save);
        dialogBack = (Button) findViewById(R.id.dialog_meeting_notice_back);
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
