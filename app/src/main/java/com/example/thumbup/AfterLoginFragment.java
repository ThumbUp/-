package com.example.thumbup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.thumbup.DataBase.DBCallBack;
import com.example.thumbup.DataBase.DBManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AfterLoginFragment extends Fragment implements View.OnClickListener  {
    public static AfterLoginFragment newInstance() {
        return new AfterLoginFragment();
    }
    ImageButton btnRevoke, btnLogout;
    TextView nameText, emailText;
    private FirebaseAuth mAuth;
    private DBManager dbManager = DBManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View afterLoginView = inflater.inflate(R.layout.activity_after_login, container, false);
        super.onCreate(savedInstanceState);

        btnLogout = (ImageButton) afterLoginView.findViewById(R.id.btn_logout);
        nameText = (TextView) afterLoginView.findViewById(R.id.myPage_nameText);
        emailText = (TextView) afterLoginView.findViewById(R.id.myPage_emailText);
        //btnRevoke = (Button)findViewById(R.id.btn_revoke);

        mAuth = FirebaseAuth.getInstance();
        //지금 하면 될까요? 넵! 로그인 했습니다!
        //다시 해보시겟어요?

        btnLogout.setOnClickListener(this);
        //btnRevoke.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
        nameText.setText(user.getDisplayName());
        emailText.setText(user.getEmail());

        return afterLoginView;
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
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}

