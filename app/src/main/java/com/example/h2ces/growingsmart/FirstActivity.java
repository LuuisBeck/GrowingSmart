package com.example.h2ces.growingsmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FirstActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }



    public void siguiente (View view){
        Intent siguiente = new Intent(this,DosActivity.class);
        startActivity(siguiente);

    }
}
