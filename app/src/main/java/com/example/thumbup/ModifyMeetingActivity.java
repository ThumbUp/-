package com.example.thumbup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thumbup.DataBase.DBManager;

public class ModifyMeetingActivity extends AppCompatActivity {
    DBManager dbManager = DBManager.getInstance();
    private final int GET_GALLERY_IMAGE = 200;
    ImageView meetingImg;
    Button mMeetingImg;
    EditText mMeetingTitle;
    EditText mMeetingInfo;
    Button mAccept;
    Button mCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main_modify_meeting);

        meetingImg = findViewById(R.id.meetingImg);
        mMeetingImg = findViewById(R.id.m_meetingImg);
        mMeetingTitle = findViewById(R.id.m_meetingTitle);
        mMeetingInfo = findViewById(R.id.m_meetingInfo);
        mAccept = findViewById(R.id.m_accept);
        mCancel = findViewById(R.id.m_cancel);

        mMeetingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });




        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mMeetingTitle.getText().toString();
                String info = mMeetingInfo.getText().toString();

             //   String key = dbManager.AddMeeting(title, info);

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            meetingImg.setImageURI(selectedImageUri);

        }

    }
}
