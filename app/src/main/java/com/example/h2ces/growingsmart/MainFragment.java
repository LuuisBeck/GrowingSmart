package com.example.h2ces.growingsmart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainFragment extends Fragment {

    TextView temperatura;
    TextView humedad;
    TextView iconTemperatura;
    TextView iconWater;
    Button botonT;
    Button botonH;
    DatabaseReference ref;
    Query lastTemp;

    Icon_Manager icon_manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_main, container, false);

        temperatura = myView.findViewById(R.id.tvTemperatura);
        humedad = myView.findViewById(R.id.tvWater);

        botonT = myView.findViewById(R.id.btTemperatura);
        botonH = myView.findViewById(R.id.btWater);
        iconTemperatura = myView.findViewById(R.id.tvIcon);
        iconWater = myView.findViewById(R.id.tvIconWater);

        icon_manager = new Icon_Manager();
        iconTemperatura.setTypeface(icon_manager.get_icons("fonts/fontawesome-webfont.ttf", getActivity()));
        iconWater.setTypeface(icon_manager.get_icons("fonts/fontawesome-webfont.ttf", getActivity()));

        //Database
        ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference temperatureDB = ref.child("sensor").child("dht1");
        lastTemp = temperatureDB.orderByKey().limitToLast(1);

        getLastInfo();


        //Buttons listeners
        //Temperature
        botonT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent graphIntent = new Intent(getActivity(), GraphActivity.class);
                graphIntent.putExtra("graphType", "temperatura");
                startActivity(graphIntent);
            }
        });

        //Humidity
        botonH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent graphIntent = new Intent(getActivity(), GraphActivity.class);
                graphIntent.putExtra("graphType", "humedad");
                startActivity(graphIntent);
            }
        });

        return myView;

    }

    public void getLastInfo() {
        lastTemp.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String T = null;
                String H = null;
                for (DataSnapshot temp: dataSnapshot.getChildren()) {
                    T = temp.child("temperature1").getValue().toString();
                    H = temp.child("humidity1").getValue().toString();
                }

                temperatura.setText(T+"°");
                humedad.setText(H+"%");

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
