package com.example.googlemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
EditText name, mail, phone, pass, conf_pass;
RadioGroup rgrp;
public String strname, strmail, strphone, strpass, strcon_pass, id, regTime, strdes;
RadioButton rbtn;
private ProgressDialog progressDialog;
TextView login;
Button reg;
TextView loging;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        mail =findViewById(R.id.mail);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
        conf_pass = findViewById(R.id.con_pass);
        reg = findViewById(R.id.reg_btn);
        loging = findViewById(R.id.sign_tv);
        rgrp = findViewById(R.id.radioGroup);
//        int radioids = rgrp.getCheckedRadioButtonId();
//        rbtn = findViewById(radioids);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
        loging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void checkButton(View v){
        int radioids = rgrp.getCheckedRadioButtonId();
        rbtn = findViewById(radioids);
    }
    private void validation() {
        strname = name.getText().toString();
        strmail = mail.getText().toString();
        strphone = phone.getText().toString();
        strpass = pass.getText().toString();
        strdes = rbtn.getText().toString();
        strcon_pass = conf_pass.getText().toString();
        if(strname.isEmpty()){
            name.setError("Please fill your name");
            name.requestFocus();
            return;
        }
        if(strphone.isEmpty()){
            name.setError("Please fill your Phone Number");
            name.requestFocus();
            return;
        }
        if (!numberCheck(strphone)) {
            phone.setError("Please enter only");
        }
        if(strmail.isEmpty()){
            name.setError("Please enter your Email");
            name.requestFocus();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            if (!Patterns.EMAIL_ADDRESS.matcher(strmail).matches()) {
                mail.setError("Please enter the valid email");
                mail.requestFocus();
                return;
            }
        }
        if(strpass.isEmpty()){
            name.setError("Please enter your password");
            name.requestFocus();
            return;
        }
        if (!passwordValidation(strpass)) {
            pass.setError("Enter maximum 8 characters and password must contain Alphanumeric and @ and _ special characters");
            pass.requestFocus();
            return;
        }
        if(strcon_pass.isEmpty()){
            name.setError("Please fill your confirm password field");
            name.requestFocus();
            return;
        }
        createAccount();
    }

    private void createAccount() {
        progressDialog.setMessage("Creati your Account...");
        progressDialog.show();

        sendConfirmDB();
    }

    private void sendConfirmDB() {
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String db_id = dataSnapshot.child("UID").getValue(String.class);
                    if(id.equals(db_id)){
                        Toast.makeText(Register.this, "You can't signup from same mobile", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else {
                        sendTodb();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendTodb() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
        regTime = sdf.format(new Date());
        HashMap<String, Object> data = new HashMap<>();
        data.put("UID", id);
        data.put("Name", strname);
        data.put("E-Mail", strmail);
        data.put("Phone", strphone);
        data.put("Password", strpass);
        data.put("Designation", strdes);
        data.put("RegTime", regTime);
        DatabaseReference mreference = FirebaseDatabase.getInstance().getReference("Users");
        mreference.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, "You are registered Successfully", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private boolean passwordValidation(String strpass) {
        Pattern lowercase = Pattern.compile(".*[a-z].*$");
        Pattern uppercase = Pattern.compile(".*[A-Z].*$");
        Pattern numbers = Pattern.compile(".*[0-9].*$");
        Pattern special = Pattern.compile("^.*[a-zA-Z0-9].*$");
        if(strpass.length()<8){
            return false;
        }
        if(!lowercase.matcher(strpass).matches())
            return false;
        if(!uppercase.matcher(strpass).matches())
            return false;
        if(!numbers.matcher(strpass).matches())
            return false;
        if (!special.matcher(strpass).matches())
            return false;
        return true;
    }
    private boolean numberCheck(String strphone) {
        Pattern p = Pattern.compile("[0-9]{10}");
        Matcher m = p.matcher(strphone);
        return m.matches();
    }
}