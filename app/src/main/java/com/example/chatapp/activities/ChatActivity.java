package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Database.VTBaglanti;
import com.example.chatapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatActivity extends AppCompatActivity {
    FloatingActionButton gonder;
    private VTBaglanti veriTabanı;
    FrameLayout mesajEkranı;
    EditText mesaj;
    String mesaj2;
    FloatingActionButton back;
    TextView karsiİsim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String sessionId = getIntent().getStringExtra("email");
        mesajEkranı=(FrameLayout) findViewById(R.id.mesajlaşmaEkranı);
        back=(FloatingActionButton) findViewById(R.id.backButton2);
        String karsikullanici = getIntent().getStringExtra("other");
        try{
            mesajlarıGetir(mesajEkranı,sessionId,karsikullanici);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Konuşma yok",Toast.LENGTH_SHORT);
        }
        veriTabanı = new VTBaglanti(this);
        karsiİsim=(TextView) findViewById(R.id.karsıİsim);
        karsiİsim.setText(karsikullanici);
        mesaj=(EditText) findViewById(R.id.mesajYaz);
        gonder = (FloatingActionButton) findViewById(R.id.send);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(sessionId);
            }
        });
        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesaj2=mesaj.getText().toString();
                if(mesaj2!=""){
                    gonder(sessionId,karsikullanici,mesaj2);
                    sendMessage(karsikullanici,mesajEkranı,mesaj.getText().toString());
                    mesaj.setText("");
                }


            }
        });
    }
    public void changeActivity(String email){
        Intent intent=new Intent(ChatActivity.this,MainPageActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    public boolean gonder(String gonderen,String alan,String msj){
        SQLiteDatabase db=veriTabanı.getWritableDatabase();
        ContentValues mesaj=new ContentValues();
        mesaj.put("gönderen",gonderen);
        mesaj.put("alan",alan);
        mesaj.put("mesaj",msj);
        mesaj.put("karsılastır1",gonderen+alan);
        mesaj.put("karsılastır2",alan+gonderen);
        long rowInterest = db.insert("message", null, mesaj);
        if(rowInterest!=-1){
            Toast.makeText(getApplicationContext(),"Başarıyla Kaydedildi", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(),"Hata", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void sendMessage(String gonderen,FrameLayout frameLayout,String message){
        TextView newBalon=new TextView(this);
        newBalon.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT));
        newBalon.setText(gonderen+": "+message);
        ((FrameLayout) frameLayout).addView(newBalon);
    }

    public void getMessage(String alan,FrameLayout frameLayout,String message){
        TextView newBalon=new TextView(this);
        newBalon.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT));
        newBalon.setText(alan+": "+message);
        ((FrameLayout) frameLayout).addView(newBalon);
    }
    public void mesajlarıGetir(FrameLayout frameLayout,String kullanıcı, String other){
        SQLiteDatabase sldb = veriTabanı.getReadableDatabase();
        Cursor one = sldb.rawQuery("Select mesaj from message where gönderen=? and alan=?", new String[] {kullanıcı,other});
        Cursor two = sldb.rawQuery("Select mesaj from message where alan=? and kullanıcı=?", new String[] {other,kullanıcı});
        if(one.getCount()>0||two.getCount()>0){
            while(one.moveToNext()){
                @SuppressLint("Range") String giden=one.getString(one.getColumnIndex("mesaj"));
                @SuppressLint("Range") String gelen=two.getString(two.getColumnIndex("mesaj"));
                two.moveToNext();
                sendMessage(other,frameLayout,giden);
                getMessage(kullanıcı,frameLayout,gelen);
            }
        }


    }


}