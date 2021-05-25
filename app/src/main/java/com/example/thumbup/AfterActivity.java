package com.example.thumbup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thumbup.DataBase.DBManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AfterActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnRevoke, btnLogout;
    TextView nameText, emailText;
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        btnLogout = (ImageButton)findViewById(R.id.btn_logout);
        nameText = (TextView)findViewById(R.id.myPage_nameText);
        emailText = (TextView)findViewById(R.id.myPage_emailText);
        //btnRevoke = (Button)findViewById(R.id.btn_revoke);

        mAuth = FirebaseAuth.getInstance();
        //지금 하면 될까요? 넵! 로그인 했습니다!
        //다시 해보시겟어요?

        btnLogout.setOnClickListener(this);
        //btnRevoke.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DBManager.getInstance().AddUser(user.getUid(), user.getDisplayName(), user.getEmail());
        nameText.setText(user.getDisplayName());
        emailText.setText(user.getEmail());
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.btn_revoke:
//                revokeAccess();
//                finishAffinity();
//                break;
        }
    }
}
