package com.example.thumbup.DataBase;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.example.thumbup.ProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot; //잠시만요!
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class DBManager {
    private static DBManager mDBManager = null;
    protected DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public String uid;      //현재 로그인 된 유저 uid
    public User userData;   //현재 로그인 된 유저 정보
    public Map<String, Meeting> participatedMeetings = new HashMap<>(); //현재 로그인된 유저가 가입된 미팅 정보

    private ProgressDialog customProgressDialog;

    //이렇게 되어있어요 넵
    //DB에서 해당유저나 유저가 가입된 미팅정보가 바뀌면
    //자동으로 위에있느 변수의 내용이 업데이트되게 해놓아서
    //일단 해당유저에 대한 모임이나 정보가 필요하면 그냥
    //음 그리고 따로 함수쓰자마자 데이터베이스에 업데이트 되야하는 내용이 아니면
    //그냥 위에 db매니저에있는 유저나 가입된 미팅매니저의 변수내용을 고치고
    //위에 두 함수를 실행하면 자동으로  db에 현재 변경된 변수대로 업데이트되게 해뒀어요

    //여기까지 되셧나요? 네!
    //미팅추가하는 부분은 바로 DB에 업데이트 되어야하는 내요이라서
    //Update함수는 쓸필요가 없어욤

    public DatabaseReference returnMDB(){
        return mDatabase;
    }

    public static DBManager getInstance() {
        if (mDBManager == null)
            mDBManager = new DBManager();
        return mDBManager;
    }

    private DBManager() {
    }

    public void Lock(Context context)
    {
        customProgressDialog = new ProgressDialog(context);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
    }

    public void UnLock()
    {
        customProgressDialog.dismiss();
    }

    public void Init() {
        addUserPostEventListener(mDatabase.child("Users").child(uid));
    }

    public void AddUser(String uid, String name, String email, final DBCallBack callBack) {
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String temp = snapshot.getKey();
                    if (temp.equals(uid) == true) {
                        userData = snapshot.getValue(User.class);
                        if (userData.meetings != null) {
                            for (int i = 0; i < userData.meetings.size(); i++) {
                                String mid = userData.meetings.get(i);
                                addMeetingPostEventListener(mDatabase.child("Meetings").child(mid), mid);
                            }
                        }
                        Init();
                        callBack.success(dataSnapshot);
                        return;
                    }
                }

                Map<String, Object> map = new HashMap<>();
                userData = new User(name, email);
                map.put(uid, userData);
                mDatabase.child("Users").updateChildren(map);
                Init();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.fail(databaseError.getMessage());
            }
        });
    }

    public void UpdateUser() {
        Map<String, Object> map = new HashMap<>();
        map.put(uid, userData);
        mDatabase.child("Users").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ...
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Write failed
                // ...
            }
        });
    }

    public void UpdateMeeting(String mid) {
        Map<String, Object> map = new HashMap<>();
        map.put(mid, participatedMeetings.get(mid));
        mDatabase.child("Meetings").updateChildren(map);
    }

    public void UpdateMeeting(String mid, final DBCallBack callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put(mid, participatedMeetings.get(mid));
        mDatabase.child("Meetings").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.success(true);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.fail(e.getMessage());
            }
        });
    }

    public void JoinMeeting(String mid) {
        Map<String, Object> map = new HashMap<>();
        if (userData.meetings.contains(mid))
            return;
        userData.meetings.add(mid);
        UpdateUser();
    }

    public String AddMeeting() {
        Map<String, Object> map = new HashMap<>();
        Meeting meetingData = new Meeting();

        String key = mDatabase.child("Meetings").push().getKey();
        mDatabase.child("Meetings").child(key).setValue(meetingData);
        return key;
    }

    public String AddMeeting(String title, String info) {
        Map<String, Object> map = new HashMap<>();
        Meeting meetingData = new Meeting(title, info);

        String key = mDatabase.child("Meetings").push().getKey();
        mDatabase.child("Meetings").child(key).setValue(meetingData);
        return key;
    }

    public void AddMoneyHistory(String mid, MoneyHistory moneyHistory) {
        participatedMeetings.get(mid).moneyHistories.add(moneyHistory);
        Map<String, Object> map = new HashMap<>();
        map.put(mid, participatedMeetings.get(mid));
        mDatabase.child("Meetings").updateChildren(map);
    }

    private void addUserPostEventListener(DatabaseReference mPostReference) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(User.class);

                if (userData.meetings != null) {
                    for (int i = 0; i < userData.meetings.size(); i++) {
                        String mid = userData.meetings.get(i);
                        addMeetingPostEventListener(mDatabase.child("Meetings").child(mid), mid);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        };
        //그부분 어디있을까용 화면에 표시하는 부분
        mPostReference.addValueEventListener(postListener);
    }

    private void addMeetingPostEventListener(DatabaseReference mPostReference, String mid) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Meeting meeting = dataSnapshot.getValue(Meeting.class);
                participatedMeetings.put(mid, meeting);
                if(participatedMeetings.get(mid).members.contains(uid) == false) {
                    participatedMeetings.get(mid).members.add(uid);
                    UpdateMeeting(mid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}