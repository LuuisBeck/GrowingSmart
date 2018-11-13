package com.example.h2ces.growingsmart;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.EditText;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.Query;

public class TresActivity extends AppCompatActivity {

    private TextView Temperatura;
    private TextView Humedad;
    private EditText mensajeEditText;



    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    DatabaseReference mensajeRef = ref.child("sensor").child("dht1");

    Query REF = mensajeRef.orderByKey().limitToLast(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tres);
    }



    @Override

    protected void onStart() {

        super.onStart();



        REF.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                String temp = dataSnapshot.child("temperature1").getValue(String.class);
                String hum = dataSnapshot.child("humedity1").getValue(String.class);

                Temperatura.setText(temp);
                Humedad.setText(hum);

            }



            @Override

            public void onCancelled(DatabaseError databaseError) {



            }

        });

    }



}

