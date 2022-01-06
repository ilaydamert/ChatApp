package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.Database.VTBaglanti;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityGirisBinding;

public class GirisActivity extends AppCompatActivity {
    Button giris;
    EditText email;
    EditText parola;
    Boolean checked;
    private VTBaglanti veriTabanı;
    private ActivityGirisBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGirisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        veriTabanı=new VTBaglanti(this);
        email=(EditText) findViewById(R.id.inputEmail);
        parola=(EditText) findViewById(R.id.inputPassword);
        giris=(Button) findViewById(R.id.buttonSignIn);
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked=check(email.getText().toString(),parola.getText().toString());
                if(checked==true){
                    changeActivity(email.getText().toString());
                }
                else{
                    Toast.makeText(getApplicationContext(),"Hatalı Giriş",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void setListeners(){
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),KayitActivity.class)));

    }
    private boolean check(String email,String parola){
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        Cursor kullanıcı=sldb.rawQuery("Select * from user where email=? and parola=?", new String[] {email,parola});
        if(kullanıcı.getCount()>0){
            return true;
        }
        else{
            return false;
        }

    }
    public void changeActivity(String email){
        Intent intent=new Intent(GirisActivity.this,MainPageActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
}