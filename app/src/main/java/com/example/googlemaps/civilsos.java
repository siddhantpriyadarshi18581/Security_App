package com.example.googlemaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class civilsos extends AppCompatActivity implements LocationListener {
    Spinner prob;
    List<Address> addressList;
    Button sendsos;
    String address;
    LocationManager mngr;
    String id;
    EditText custom;
    String[] listofprobs = {"Murder", "Assault", "Chain Snatching", "Robbery", "Pick Pocketing", "Other"};
    String str_probs;
//    String address;
    double lati, longi;
    String latit, longit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civilsos);
        sendsos = findViewById(R.id.sos);
        custom = findViewById(R.id.customProbs);
        prob = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(civilsos.this, android.R.layout.simple_dropdown_item_1line, listofprobs);
        prob.setAdapter(adapter);
        prob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getSelectedItem().toString();
                str_probs = item;
                if(item.equals("Other")){
                    str_probs=custom.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sendsos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                onStorage();
            }
        });
    }

    @SuppressLint("HardwareIds")
    private void onStorage() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy  'at' HH:mm:ss z");
        String regTime = sdf.format(new Date());
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        Intent i1 = new Intent(this,);
        int flag = 0;
        String name = getIntent().getStringExtra("name");
        HashMap<String, Object> data = new HashMap<>();
        data.put("Name",name);
        data.put("User-ID", id);
        data.put("Flag",flag);
        data.put("Problem", str_probs);
        data.put("Latitude", latit);
        data.put("Longitude", longit);
        data.put("Address", address);
        data.put("Registration Time", regTime);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SOS");
        ref.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(civilsos.this, "Helping You as always", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(civilsos.this, "You have to wait for a while", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void getLocation() {
//        Checking its access from manifest files
        if (ContextCompat.checkSelfPermission(civilsos.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    @SuppressLint("MissingPermission")
    private void retrieveLocation() {
        mngr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mngr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

        Location location = mngr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        onLocationChanged(location);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lati = location.getLatitude();
        longi = location.getLongitude();
        latit = Double.toString(lati);
        longit = Double.toString(longi);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(lati, longi, 1);
//            address = addressList.toString();
            StringBuilder stringBuilder = new StringBuilder();

            for (Address address : addressList) {
                int addressLineCount = address.getMaxAddressLineIndex();
                for (int i = 0; i <= addressLineCount; i++) {
                    stringBuilder.append(address.getAddressLine(i));
                    if (i < addressLineCount) {
                        stringBuilder.append(", ");
                    }
                }
                stringBuilder.append("\n");
            }

            address = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }
}
