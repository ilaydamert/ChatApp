package com.example.chatapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Database.VTBaglanti;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityKayitBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class KayitActivity extends AppCompatActivity {
    private ActivityKayitBinding binding;
    private String encodedImage;
    private VTBaglanti veriTabanı;

    EditText isim,parola,email,parolaYeniden;
    Button kayit;
    ImageView pp;
    TextView bilgiler;
    String encodedpp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        veriTabanı=new VTBaglanti(this);
        binding = ActivityKayitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        isim=(EditText) findViewById(R.id.inputName);
        email=(EditText) findViewById(R.id.inputEmail);
        parola=(EditText) findViewById(R.id.inputPassword);
        parolaYeniden=(EditText) findViewById(R.id.inputConfirmPassword);
        kayit=(Button) findViewById(R.id.buttonSignUp);
        pp=(ImageView) findViewById(R.id.imageProfile);
        bilgiler=(TextView) findViewById(R.id.bilgiler);
        //Bitmap bitmap = ((BitmapDrawable)pp.getDrawable()).getBitmap();
        //encodedpp=encodeImage(bitmap);

        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(check()==true){
                        kayitEkle(isim.getText().toString(),email.getText().toString(),parola.getText().toString(),encodedImage);
                        isim.setText("");
                        parola.setText("");
                        parolaYeniden.setText("");
                        email.setText("");
                        binding.imageProfile.setImageBitmap(null);
                    }
                    else{
                        showToast("");
                    }

                }
                finally{
                    veriTabanı.close();
                }
            }
        });
    }
    public void changeActivity(){
        Intent intent=new Intent(KayitActivity.this,MainActivity.class);
        startActivity(intent);
    }
    private String encodeImage(Bitmap bitmap){
        int previewWidth=150;
        int previewHeight=bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap=Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData()!=null){
                        Uri imageUri =result.getData().getData();
                        try{
                            InputStream inputStream=getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage=encodeImage(bitmap);
                        }
                        catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private boolean kayitEkle(String isim, String email, String parola, String encodedImage){
        SQLiteDatabase db=veriTabanı.getWritableDatabase();
        ContentValues kullanıcı=new ContentValues();
        kullanıcı.put("isim",isim);
        kullanıcı.put("email",email);
        kullanıcı.put("parola",parola);
        kullanıcı.put("fotograf",encodedImage);
        long rowInterest=db.insertOrThrow("user",null,kullanıcı);
        if(rowInterest!=-1){
            Toast.makeText(getApplicationContext(),"Başarıyla Kaydedildi", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(),"Hata", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private Boolean check(){
        if(encodedImage==null){
            showToast("Profil Resmi Seçin!");
            return false;
        }
        else if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("İsim Girin!");
            return false;
        }
        else if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Email Girin!" );
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Email Formatı Girin!");
            return false;
        }
        else if(checkMail(email.getText().toString())==true){
            showToast("Email Kayıtlı!");
            return false;
        }
        else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Parola Girin!" );
            return false;
        }
        else if(binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Parolayı Yeniden Girin!" );
            return false;
        }
        else if(binding.inputPassword.getText().toString().trim().length()<6){
            showToast("Parola en az 6 karakter olmalı!" );
            return false;
        }
        else if(!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Parolalar Aynı Olmalı!" );
            return false;
        }
        else{
            return true;
        }
    }
    private void Listele(){
        String[] sutunlar={"id","isim","email","parola","fotograf"};
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        Cursor users=sldb.query("user",sutunlar,null,null,null,null,null);
        String tumbilgi="";
        while(users.moveToNext()){
            @SuppressLint("Range") String id=users.getString(users.getColumnIndex("id"));
            @SuppressLint("Range") String isim=users.getString(users.getColumnIndex("isim"));
            @SuppressLint("Range") String email=users.getString(users.getColumnIndex("email"));
            @SuppressLint("Range") String parola=users.getString(users.getColumnIndex("parola"));
            @SuppressLint("Range") String fotograf=users.getString(users.getColumnIndex("fotograf"));
            tumbilgi += id+" "+isim+" "+email+" "+parola+" "+fotograf+"\n";
        }
        bilgiler.setText(tumbilgi);
    }
    private boolean checkMail(String email){
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        Cursor cursor=sldb.rawQuery("Select * from user where email=?", new String[] {email});
        if(cursor.getCount()>0){
            return true;
        }
        return false;
    }
}