package com.example.thumbup.DataBase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBManager {
    private static DBManager mDBManager = null;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static DBManager getInstance() {
        if (mDBManager == null)
            mDBManager = new DBManager();
        return mDBManager;
    }

    public void AddUser(String uid, String name, String email) {
        UserData userData = new UserData(name, email);

        mDatabase.child("Users").child(uid).setValue(userData);
    }


}