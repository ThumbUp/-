package com.example.thumbup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.thumbup.DataBase.DBCallBack;
import com.example.thumbup.DataBase.DBManager;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddMeetingActivity extends AppCompatActivity {
    DBManager dbManager = DBManager.getInstance();
    private final int GET_GALLERY_IMAGE = 200;
    ImageView addMeetingImg;
    TextView addMeetingText;
    EditText addMeetingTitle;
    EditText addMeetingInfo;
    Button addAccept;
    Button addCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main_add_meeting);

        addMeetingImg = findViewById(R.id.meetingImg);
        addMeetingText = findViewById(R.id.addMeeting_text);
        addMeetingTitle = findViewById(R.id.add_meetingTitle);
        addMeetingInfo = findViewById(R.id.add_meetingInfo);
        addAccept = findViewById(R.id.add_accept);
        addCancel = findViewById(R.id.add_cancel);

        //모임 사진 추가
        addMeetingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        //확인 버튼 클릭
        addAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addMeetingTitle.getText().toString();
                String info = addMeetingInfo.getText().toString();
                addMeetingImg.getDrawable().getCurrent();
                Drawable image = addMeetingImg.getDrawable();
                Drawable dImage = getResources().getDrawable(R.drawable.ic_profile);
                String key = "", simage = "";
                //이미지 바이트 변환
                if(image.equals(dImage) == true) {
                    Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteImage = stream.toByteArray();
                    simage = byteArrayToBinaryString(byteImage);
                }

                key = dbManager.AddMeeting(title, info, simage); //삭제부분어딧죠?

                //모임 생성 완료 다이얼로그
                final LinearLayout linear = (LinearLayout) View.inflate(getApplicationContext(),
                        R.layout.dialog_main_meeting_key, null);

                AlertDialog.Builder dlg3 = new AlertDialog.Builder(AddMeetingActivity.this);
                dlg3.setView(linear);
                TextView k_meetingKey;
                k_meetingKey = (TextView)linear.findViewById(R.id.k_meetingKey);
                k_meetingKey.setText(key);
                dlg3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                }).show();
            }
        });

        addCancel.setOnClickListener(new View.OnClickListener() {
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
            addMeetingImg.setImageURI(selectedImageUri);

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
