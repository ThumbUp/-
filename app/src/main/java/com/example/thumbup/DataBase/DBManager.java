package com.example.thumbup.DataBase;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
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
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public String uid;
    public User userData;
    public Map<String, Meeting> participatedMeetings = new HashMap<>();

    public static DBManager getInstance() {
        if (mDBManager == null)
            mDBManager = new DBManager();
        return mDBManager;
    }

    private DBManager() {
    }

    public void Init() {
        addUserPostEventListener(mDatabase.child("Users").child(uid));
    }

    public void AddUser(String uid, String name, String email) {
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String temp = snapshot.getKey();
                    if (temp.equals(uid) == true) {
                        DBManager.getInstance().Init();
                        return;
                    }
                }

                Map<String, Object> map = new HashMap<>();
                User userData = new User(name, email);
                map.put(uid, userData);
                mDatabase.child("Users").updateChildren(map);
                Init();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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