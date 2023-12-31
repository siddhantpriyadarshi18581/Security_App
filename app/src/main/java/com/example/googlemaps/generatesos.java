package com.example.googlemaps;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class generatesos extends AppCompatActivity {
    String id;
//    String db_lat, db_long;
    Uri imageUri;
    StorageReference storageref;
    ImageView profile;
//    String db_id;
private BroadcastReceiver smsReceiver;
    Button generated;
    @SuppressLint({"MissingInflatedId", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generatesos);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        profile = findViewById(R.id.profiler);
        generated = findViewById(R.id.generate);

        profile.setOnClickListener(view -> selectImage());
        generated.setOnClickListener(view -> searchFlag());
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        if (pdus != null) {
                            for (Object pdu : pdus) {
                                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                                String senderPhoneNumber = smsMessage.getOriginatingAddress();
                                String message = smsMessage.getMessageBody();
                                Toast.makeText(context, "Received SMS from " + senderPhoneNumber + ": " + message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        };
    }

    private void selectImage() {
        Intent i1 = new Intent();
        i1.setType("image/*");
        i1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i1, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            imageUri = data.getData();
            profile.setImageURI(imageUri);
            uploadImage();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsReceiver);
    }

    private void uploadImage() {
       storageref = FirebaseStorage.getInstance().getReference("images/"+id);

       storageref.putFile(imageUri)
               .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Toast.makeText(generatesos.this, "Your profile pic is successfully uploaded", Toast.LENGTH_SHORT).show();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(generatesos.this, "Sorry we could not process your file", Toast.LENGTH_SHORT).show();
                   }
               });
    }

    private void convertFlag(String db_id) {
        int flag=1;
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("SOS").child(db_id).child("Flag");
//        DatabaseReference newUserRef = dref.push();
        HashMap<String, Object> data = new HashMap<>();
        data.put("Flag", flag);
       dref.setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

//                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(generatesos.this, "SOS Indicators are not received", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchFlag() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SOS");
        ref.child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String db_id = dataSnapshot.child("User-ID").getValue(String.class);
                        if (!id.equals(db_id)) {
                            int db_flag = dataSnapshot.child("Flag").getValue(Integer.class);
                            if (db_flag == 0) {
                                String db_lat = dataSnapshot.child("Latitude").getValue(String.class);
                                String db_long = dataSnapshot.child("Longitude").getValue(String.class);
                                Intent i3 = new Intent(generatesos.this, MapsActivity.class);
                                i3.putExtra("lat", db_lat);
                                i3.putExtra("long", db_long);
                                startActivity(i3);
                                convertFlag(db_id);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}