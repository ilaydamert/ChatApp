package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chatapp.R;

public class MainActivity extends AppCompatActivity {
    Button giris,kayit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        giris=(Button) findViewById(R.id.giris);
        kayit=(Button) findViewById(R.id.kayit);
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity2();
            }
        });
        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();

            }
        });
    }
    public void changeActivity(){
        Intent intent=new Intent(MainActivity.this,KayitActivity.class);
        startActivity(intent);
    }

    public void changeActivity2(){
        Intent intent=new Intent(MainActivity.this,GirisActivity.class);
        startActivity(intent);
    }
}