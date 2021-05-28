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
    public String AddMeeting(String title, String info, String image) {
        Map<String, Object> map = new HashMap<>();
        Meeting meetingData = new Meeting(title, info, image);

        String key = mDatabase.child("Meetings").push().getKey();
        participatedMeetings.put(key, meetingData);
        mDatabase.child("Meetings").child(key).setValue(meetingData);
        return key;
        //다시해보실래요?했습니다!된요? 팝업창에 모임 코드는 뜨는데 파이어베이스에서 보면 없네요 음잠시만요 저 뭐좀 하고이성서 다른거부터 하고잇으실래요? 넵!
    }

    //된거같아유!
    //db에 모임이 안 생기는 것 같아요..!


    public String AddMeeting(String title, String info, String image, final DBCallBack callBack) {
        Map<String, Object> map = new HashMap<>();
        Meeting meetingData = new Meeting(title, info, "");

        String key = mDatabase.child("Meetings").push().getKey();
        participatedMeetings.put(key, meetingData);
        mDatabase.child("Meetings").child(key).setValue(meetingData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.success(true);
                mDatabase.child("Meetings").child(key).setValue(meetingData);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.fail(e.getMessage());
            }
        });
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