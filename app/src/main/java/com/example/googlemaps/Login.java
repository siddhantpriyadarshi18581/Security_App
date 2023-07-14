package com.example.googlemaps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
EditText username, password;
Button login;
TextView signup;
String id, str_name, str_pass;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.name_log);
        password = findViewById(R.id.password_log);
        login = findViewById(R.id.login_btn);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        signup = findViewById(R.id.register_tv_log);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_name = username.getText().toString();
                str_pass = password.getText().toString();
                if(str_name.isEmpty()){
                    username.setError("Username is not filled");
                    username.requestFocus();
                    return;
                }
                if(str_pass.isEmpty()){
                    password.setError("Password is not filled");
                    password.requestFocus();
                    return;
                }
                validation();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validation() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String db_name = dataSnapshot.child("Name").getValue(String.class);
                        String db_pass = dataSnapshot.child("Password").getValue(String.class);
                        String db_des = dataSnapshot.child("Designation").getValue(String.class);
                        String db_id = dataSnapshot.child("UID").getValue(String.class);
                        assert db_id != null;
                        if(db_id.equals(id)) {
                            assert db_name != null;
                            if (db_name.equals(str_name)) {
                                assert db_pass != null;
                                if (db_pass.equals(str_pass)) {
                                    assert db_des != null;
                                    if (db_des.equals("Civilian")) {
                                        Intent i2 = new Intent(getApplicationContext(), civilsos.class);
                                        i2.putExtra("name", str_name);
                                        startActivity(i2);
                                        finish();
                                    } else {
                                        Intent intent1 = new Intent(getApplicationContext(), generatesos.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }
                            }if(!str_name.equals(db_name)){
                                if (!str_pass.equals(db_pass)){
                                    Toast.makeText(Login.this, "User Name and password is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(Login.this, "You have to login from your registered mobile device only", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(Login.this, "Record Not Found Sign In First", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}