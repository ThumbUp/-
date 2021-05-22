package com.example.thumbup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

public class MeetingScheduleDialog extends Dialog {
    public MeetingScheduleDialog(@NonNull Context context) {
        super(context);
    }
    DatePicker datePicker = (DatePicker) findViewById(R.id.meeting_schedule_date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_meeting_schedule);
        
    }
}
