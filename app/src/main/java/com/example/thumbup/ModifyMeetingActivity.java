package com.example.thumbup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thumbup.DataBase.DBManager;
import com.example.thumbup.DataBase.Meeting;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class ModifyMeetingActivity extends AppCompatActivity {
    DBManager dbManager = DBManager.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final int GET_GALLERY_IMAGE = 200;
    ImageView mMeetingImg;
    TextView mMeetingText;
    EditText mMeetingTitle;
    EditText mMeetingInfo;
    Button mAccept;
    Button mCancel;
    boolean isImageChange = false;
    Context context;
    String meetingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main_modify_meeting);

        context = this;
        mMeetingImg = findViewById(R.id.m_meetingImg);
        mMeetingText = findViewById(R.id.mMeeting_text);
        mMeetingTitle = findViewById(R.id.m_meetingTitle);
        mMeetingInfo = findViewById(R.id.m_meetingInfo);
        mAccept = findViewById(R.id.m_accept);
        mCancel = findViewById(R.id.m_cancel);

        mMeetingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        //확인 버튼 클릭
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secondIntent = getIntent();
                meetingId = secondIntent.getStringExtra("meetingId");
                String title = mMeetingTitle.getText().toString();
                String info = mMeetingInfo.getText().toString();
                mMeetingImg.getDrawable().getCurrent();
                Drawable image = mMeetingImg.getDrawable();
                Drawable dImage = getResources().getDrawable(R.drawable.ic_profile);
                String simage = "";

                //이미지 바이트 변환
                if(isImageChange == true) {
                    Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteImage = stream.toByteArray();
                    simage = byteArrayToBinaryString(byteImage);
                }

                mDatabase.child("Meetings").child(meetingId).child("title").setValue(title);
                mDatabase.child("Meetings").child(meetingId).child("info").setValue(info);
                mDatabase.child("Meetings").child(meetingId).child("image").setValue(simage);
                dbManager.UpdateMeeting(meetingId);

                AlertDialog.Builder dlg4 = new AlertDialog.Builder(ModifyMeetingActivity.this);
                dlg4.setTitle("모임이 수정되었습니다");
                dlg4.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dlg4.create();

                alertDialog.show();
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
            mMeetingImg.setImageURI(selectedImageUri);
            isImageChange = true;
        }
    }

    public static String byteArrayToBinaryString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; ++i) {
            sb.append(byteToBinaryString(b[i]));
        } return sb.toString();
    }
    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        } return sb.toString();
    }

}
