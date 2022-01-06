package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

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

public class ArkadasBulActivity extends AppCompatActivity {
TextView kullanıcıAd;
FloatingActionButton back;
    FloatingActionButton ara;
    private VTBaglanti veriTabanı;
    FrameLayout bulundu;
    FloatingActionButton ekle;
    TextView otherKullanıcıAd;
    EditText EmailAra;
    String bilgiler="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veriTabanı = new VTBaglanti(this);
        setContentView(R.layout.activity_arkadas_bul);
        String sessionId = getIntent().getStringExtra("email");
        back = (FloatingActionButton) findViewById(R.id.backButton);
        ara = (FloatingActionButton) findViewById(R.id.search);
        EmailAra = (EditText) findViewById(R.id.findFriend);
        otherKullanıcıAd = (TextView) findViewById(R.id.otherKullanıcıAd);
        bulundu = (FrameLayout) findViewById(R.id.finded);
        bulundu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(sessionId,EmailAra.getText().toString());

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity2(sessionId);
            }
        });
        ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EmailAra.getText().toString()!=sessionId){
                    if (check(EmailAra.getText().toString()) == true) {
                        try {
                            //bilgiler = ara(EmailAra.getText().toString());
                            bulundu.setVisibility(View.VISIBLE);
                            otherKullanıcıAd.setText(EmailAra.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            bulundu.setVisibility(View.INVISIBLE);
                        }
                    }
                    else{
                        bulundu.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Bulunmadı",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //İsim bul
        //foto ayarla
        kullanıcıAd = (TextView) findViewById(R.id.kullanıcıAd4);
        kullanıcıAd.setText(sessionId);
    }
    public void changeActivity(String email,String other){
        Intent intent=new Intent(ArkadasBulActivity.this,ChatActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("other",other);
        startActivity(intent);
    }
    public void changeActivity2(String email){
        Intent intent=new Intent(ArkadasBulActivity.this,MainPageActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    public boolean check(String email) {
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        String rv="";
        Cursor kullanıcı=sldb.rawQuery("Select email from user where email=?", new String[] {email});
        if(kullanıcı.getCount()>0){
            return true;
        }
        else{
            return false;
        }

    }
    public String ara(String email) throws Exception {
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        Cursor isim=sldb.rawQuery("Select isim from user where email=?", new String[] {email});
        Cursor fotograf=sldb.rawQuery("Select fotograf from user where email=?", new String[] {email});
        isim.moveToFirst();
        String ad=isim.toString();
        return ad;
    }
    public boolean ekle(String ekleyen, String eklenen) {
        SQLiteDatabase db=veriTabanı.getWritableDatabase();
        ContentValues arkadas=new ContentValues();
        arkadas.put("ekleyen",ekleyen);
        arkadas.put("eklenen",eklenen);
        long rowInterest=db.insertOrThrow("friend",null,arkadas);
        if(rowInterest!=-1){
            Toast.makeText(getApplicationContext(),"Başarıyla Kaydedildi", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(),"Hata", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean checkIsFriend(String email,String digerMail) throws Exception {
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        Cursor one=sldb.rawQuery("Select * from friend where ekleyen=? and eklenen=?", new String[] {email,digerMail});
        Cursor two=sldb.rawQuery("Select * from friend where ekleyen=? and eklenen=?", new String[] {digerMail,email});
        if(one.getCount()>0){
            return false;
        }
        else if(two.getCount()>0){
            return false;
        }
        else{
            return true;
        }
    }
}