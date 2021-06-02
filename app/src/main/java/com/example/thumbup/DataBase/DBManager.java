package com.example.thumbup.DataBase;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.example.thumbup.MeetingFragment;
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


import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class DBManager {
    private static DBManager mDBManager = null;
    protected DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public String uid;      //현재 로그인 된 유저 uid
    public User userData;   //현재 로그인 된 유저 정보
    public Map<String, Meeting> participatedMeetings = new HashMap<>(); //현재 로그인된 유저가 가입된 미팅 정보
    private Map<String, ValueEventListener> participatedMeetingsListeners = new HashMap<>();

    private ProgressDialog customProgressDialog;
    public MeetingFragment meetingFrament;
    public DatabaseReference returnMDB() {
        return mDatabase;
    }

    public static DBManager getInstance() {
        if (mDBManager == null)
            mDBManager = new DBManager();
        return mDBManager;
    }

    private DBManager() {
    }

    public void Lock(Context context) {
        customProgressDialog = new ProgressDialog(context);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
    }

    public void UnLock() {
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
                        GetMyMeetingData(new DBCallBack() {
                            @Override
                            public void success(Object data) {
                                callBack.success(dataSnapshot);
                            }

                            @Override
                            public void fail(String errorMessage) {

                            }
                        });
                        return;
                    }
                }

                Map<String, Object> map = new HashMap<>();
                userData = new User(name, email);
                map.put(uid, userData);
                mDatabase.child("Users").updateChildren(map);
                callBack.success(true);
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
        mDatabase.child("Users").updateChildren(map);
    }

    public void UpdateUser(final DBCallBack callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put(uid, userData);
        mDatabase.child("Users").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.success(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.fail(e.getMessage());
            }
        });
    }

    public void CheckValidMeetingId(String mid, DBCallBack callback) {
        mDatabase.child("Meetings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String clubkey = childSnapshot.getKey();
                    if (clubkey.equals(mid) == true) {
                        callback.success(true);
                        return;
                    }
                }
                callback.fail("No");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void UpdateMeeting(String mid) {
         Map<String, Object> map = new HashMap<>();
        map.put(mid, participatedMeetings.get(mid));
        mDatabase.child("Meetings").updateChildren(map);
    }

    private void GetMyMeetingData(final DBCallBack callBack) {
        Map<String, Object> map = new HashMap<>();
        int meetingSize = userData.meetings.size();
        if (meetingSize == 0)
            callBack.success(true);
        for (int i = 0; i < userData.meetings.size(); i++) {
            String uid = userData.meetings.get(i);
            mDatabase.child("Meetings").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                    } else {
                        Meeting meeting = task.getResult().getValue(Meeting.class);
                        participatedMeetings.put(uid, meeting);
                        if (participatedMeetings.size() == meetingSize)
                            callBack.success(true);
                    }
                }
            });
        }
    }

    public void WithdrawMeeting(String mid, final DBCallBack callBack) {
        userData.meetings.remove(mid);
        participatedMeetings.get(mid).members.remove(uid);
        mDatabase.child("Meetings").child(mid).removeEventListener(participatedMeetingsListeners.get(mid));
        GetMeetingData(mid, new DBCallBack() {
            @Override
            public void success(Object data) {
                Meeting meeting = (Meeting)data;
                int memberCount = meeting.members.size();
                UpdateUser(new DBCallBack() {
                    @Override
                    public void success(Object data) {
                        Meeting m = participatedMeetings.remove(mid);
                        if (memberCount == 1) {
                            mDatabase.child("Meetings").child(mid).setValue(null);
                        }
                        else {
                            mDatabase.child("Meetings").child(mid).setValue(m);
                        }
                        callBack.success(true);
                    }

                    @Override
                    public void fail(String errorMessage) {
                        callBack.fail(errorMessage);
                    }
                });
            }

            @Override
            public void fail(String errorMessage) {

            }
        });

    }

    public void WithdrawMeeting(String mid) {
        userData.meetings.remove(mid);
        participatedMeetings.get(mid).members.remove(uid);
        mDatabase.child("Meetings").child(mid).removeEventListener(participatedMeetingsListeners.get(mid));
        UpdateMeeting(mid);
        participatedMeetings.remove(mid);
        UpdateUser();
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

    public void JoinMeeting(String mid, DBCallBack callBack) {
        Map<String, Object> map = new HashMap<>();
        if (userData.meetings.contains(mid))
            return;
        userData.meetings.add(mid);
        UpdateUser(new DBCallBack() {
            @Override
            public void success(Object data) {
                GetMyMeetingData(new DBCallBack() {
                    @Override
                    public void success(Object data) {
                        callBack.success(true);
                    }

                    @Override
                    public void fail(String errorMessage) {

                    }
                });
            }

            @Override
            public void fail(String errorMessage) {

            }
        });
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
    }

    public String AddMeeting(String title, String info, String image, final DBCallBack callBack) {
        Map<String, Object> map = new HashMap<>();
        Meeting meetingData = new Meeting(title, info, image);

        String key = mDatabase.child("Meetings").push().getKey();
        participatedMeetings.put(key, meetingData);
        mDatabase.child("Meetings").child(key).setValue(meetingData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.success(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
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

    private void GetMeetingData(String uid, final DBCallBack callBack) {
        mDatabase.child("Meetings").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                } else {
                    Meeting meeting = task.getResult().getValue(Meeting.class);
                    callBack.success(meeting);
                }
            }
        });
    }

    private void addMeetingPostEventListener(DatabaseReference mPostReference, String mid) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Meeting meeting = dataSnapshot.getValue(Meeting.class);
                CheckValidMeetingId(mid, new DBCallBack() {
                    @Override
                    public void success(Object data) {
                        participatedMeetings.put(mid, meeting);
                        if (participatedMeetings.get(mid).members.contains(uid) == false) {
                            participatedMeetings.get(mid).members.add(uid);
                            UpdateMeeting(mid);
                        }
                    }

                    @Override
                    public void fail(String errorMessage) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        };
        if(participatedMeetingsListeners.containsKey(mid) == false) {
            participatedMeetingsListeners.put(mid, postListener);
            mPostReference.addValueEventListener(postListener);
        }
    }
}