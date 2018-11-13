package com.example.h2ces.growingsmart;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.Query;



public class DosActivity extends AppCompatActivity {

    private TextView Temperatura;
    private TextView Humedad;
    private EditText mensajeEditText;

    private String T;
    private String H;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    DatabaseReference mensajeRef = ref.child("sensor").child("dht1");
    Query Ref = mensajeRef.orderByKey().limitToLast(1);


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dos);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Temperatura = (TextView) findViewById(R.id.TextLayout);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container
                    , new MainFragment()).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override

    protected void onStart() {

        super.onStart();

        Ref.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot temp: dataSnapshot.getChildren()) {

                    T = temp.child("temperature1").getValue().toString();
                    //H = temp.child("humedity1").getValue().toString();
                }

                //Temperatura.setText(T);

               /* for (DataSnapshot temp: dataSnapshot.getChildren()) {

                    T = temp.child("tercero").getValue(String.class);
                }

                Temperatura.setText(T);
*/
            }



            @Override

            public void onCancelled(DatabaseError databaseError) {



            }

        });

    }







}
