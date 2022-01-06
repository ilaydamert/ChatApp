package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.chatapp.Database.VTBaglanti;
import com.example.chatapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MainPageActivity extends AppCompatActivity {
    FloatingActionButton message,add,setting;
    private VTBaglanti veriTabanı;
    TextView kullanıcıAdı;
    String temp;
    Bitmap fotografBitmap;
    FrameLayout other1;
    FrameLayout other2;
    FrameLayout other3;
    FrameLayout other4;
    FrameLayout other5;
    RoundedImageView profile;
    FloatingActionButton CıkısButton;
    TextView karsıKullanıcı;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veriTabanı=new VTBaglanti(this);
        String sessionId = getIntent().getStringExtra("email");
        //setKullanıcıAdı(sessionId);
        //setFotograf(sessionId);
        setContentView(R.layout.activity_main_page);
        //listele();
        other1=(FrameLayout) findViewById(R.id.otherKullanıcı1);
        other2=(FrameLayout) findViewById(R.id.otherKullanıcı2);
        other3=(FrameLayout) findViewById(R.id.otherKullanıcı3);
        other4=(FrameLayout) findViewById(R.id.otherKullanıcı4);
        other5=(FrameLayout) findViewById(R.id.otherKullanıcı5);
        FrameLayout[] frames=new FrameLayout[5];
        frames[0]=other1;
        frames[1]=other2;
        frames[2]=other3;
        frames[3]=other4;
        frames[4]=other5;
        //change(sessionId,frames);
        karsıKullanıcı=(TextView) findViewById(R.id.otherKullanıcıAd);
        message=(FloatingActionButton) findViewById(R.id.message);
        add=(FloatingActionButton) findViewById(R.id.add);
        setting=(FloatingActionButton) findViewById(R.id.setting);
        kullanıcıAdı=(TextView) findViewById(R.id.kullanıcıAd);
        profile=(RoundedImageView) findViewById(R.id.imageProfile);
        CıkısButton=(FloatingActionButton) findViewById(R.id.cıkısYap);
        kullanıcıAdı.setText(sessionId);
        CıkısButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity2(sessionId);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity3(sessionId);
            }
        });
        other1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity4(sessionId,karsıKullanıcı.getText().toString());
            }
        });
        if(other5.getVisibility()==View.VISIBLE){
            add.setVisibility(View.INVISIBLE);
        }

    }
    public void changeActivity(){
        Intent intent=new Intent(MainPageActivity.this,GirisActivity.class);
        startActivity(intent);
    }
    public void changeActivity2(String email){
        Intent intent=new Intent(MainPageActivity.this,SettingActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    public void changeActivity3(String email){
        Intent intent=new Intent(MainPageActivity.this,ArkadasBulActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    public void setKullanıcıAdı(String email){
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        Cursor kullanıcı=sldb.rawQuery("Select isim from user where email=?", new String[] {email});
        kullanıcıAdı.setText(kullanıcı.toString());
    }
    public void setFotograf(String email){
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        Cursor fotograf=sldb.rawQuery("Select fotograf from user where email=?", new String[] {email});
        fotografBitmap=StringToBitMap(fotograf.toString());
        profile.setImageBitmap(fotografBitmap);
    }
    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public void Delete(String id){
        SQLiteDatabase sldb=veriTabanı.getWritableDatabase();
        sldb.delete("user","id=?",new String[] {id});
    }
    public void changeActivity4(String email,String email2){
        Intent intent=new Intent(MainPageActivity.this,ChatActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("other",email2);
        startActivity(intent);
    }
    public int hesapla(String email) {

        SQLiteDatabase sldb = veriTabanı.getReadableDatabase();
        int sayi;
        int count=0;
        Cursor one = sldb.rawQuery("Select id from message where gönderen=?", new String[] {email});
        Cursor two = sldb.rawQuery("Select id from message where alan=?", new String[] {email});
        sayi=one.getCount()+two.getCount();
        for(int i=0;i<sayi;i++){
            one.moveToNext();
            two.moveToNext();
            String bir=one.getString(1)+two.getString(1);
            String iki=two.getString(1)+one.getString(1);

            if(bir!=iki){
                count++;
            }

        }
        return count;
    }
    public void change(String email,FrameLayout[] frames) {
        int sayı=hesapla(email);
        for(int i=0;i<sayı;i++){
            frames[i].setVisibility(View.VISIBLE);
        }

    }
    public void listele(){
        String[] sutunlar={"id","gönderen","alan","mesaj"};
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        Cursor ogrenciler=sldb.query("message",sutunlar,null,null,null,null,null);
        String tumbilgi="";
        while(ogrenciler.moveToNext()){
            @SuppressLint("Range") String adbilgisi=ogrenciler.getString(ogrenciler.getColumnIndex("id"));
            @SuppressLint("Range") String nobilgisi=ogrenciler.getString(ogrenciler.getColumnIndex("gönderen"));
            @SuppressLint("Range") String nobilgisi2=ogrenciler.getString(ogrenciler.getColumnIndex("alan"));
            @SuppressLint("Range") String nobilgisi3=ogrenciler.getString(ogrenciler.getColumnIndex("mesaj"));
            tumbilgi += adbilgisi+" "+nobilgisi+" "+nobilgisi2+" "+nobilgisi3+"\n";
        }
        kullanıcıAdı.setText(tumbilgi);

    }

}