package com.example.thumbup;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.thumbup.DataBase.DBManager;

import java.io.IOException;
import java.io.InputStream;

public class AddMeetingActivity extends AppCompatActivity {
    DBManager dbManager = DBManager.getInstance();
    private final int GET_GALLERY_IMAGE = 200;
    ImageView meetingImg;
    Button addMeetingImg;
    EditText addMeetingTitle;
    EditText addMeetingInfo;
    Button addAccept;
    Button addCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main_add_meeting);

        meetingImg = findViewById(R.id.meetingImg);
        addMeetingImg = findViewById(R.id.add_meetingImg);
        addMeetingTitle = findViewById(R.id.add_meetingTitle);
        addMeetingInfo = findViewById(R.id.add_meetingInfo);
        addAccept = findViewById(R.id.add_accept);
        addCancel = findViewById(R.id.add_cancel);

        addMeetingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });




        addAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addMeetingTitle.getText().toString();
                String info = addMeetingInfo.getText().toString();

                String key = dbManager.AddMeeting(title, info);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        addCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
