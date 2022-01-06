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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Database.VTBaglanti;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivitySettingBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    private VTBaglanti veriTabanı;
    private String encodedImage;
    FloatingActionButton MainPageButton;
    FloatingActionButton AyarlarButton;
    FloatingActionButton CıkısButton;
    TextView text;
    Button guncelle,sil;
    EditText isim,parola,parolaYeniden;
    ImageView pp;
    ImageView newImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        String sessionId = getIntent().getStringExtra("email");
        veriTabanı=new VTBaglanti(this);
        text=(TextView) findViewById(R.id.kullanıcıAd);
        text.setText(sessionId);
        guncelle=(Button) findViewById(R.id.buttonGuncelle);
        sil=(Button) findViewById(R.id.buttonSil);
        newImage=(ImageView) findViewById(R.id.pp);
        isim=(EditText) findViewById(R.id.inputName);
        parola=(EditText) findViewById(R.id.inputPassword);
        parolaYeniden=(EditText) findViewById(R.id.inputConfirmPassword);
        MainPageButton=(FloatingActionButton) findViewById(R.id.messageButton);
        AyarlarButton=(FloatingActionButton) findViewById(R.id.settingButton);
        CıkısButton=(FloatingActionButton) findViewById(R.id.cıkısYapButton);
        MainPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(sessionId,pp.toString());
            }
        });
        Listele();
        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForInfo()==true){
                    guncelle(sessionId,isim.getText().toString(),parola.getText().toString(),encodedImage);

                    isim.setText("");
                    parola.setText("");
                    parolaYeniden.setText("");
                    binding.pp.setImageBitmap(null);
                }
            }
        });
        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForDelete(sessionId)==true){
                    sil(sessionId);
                    changeActivity2();
                }
            }
        });
        CıkısButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity2();
            }
        });

    }
    private void changeActivity(String isim,String fotograf){
        Intent intent=new Intent(SettingActivity.this,MainPageActivity.class);
        intent.putExtra("isim",isim);
        intent.putExtra("fotograf",fotograf);
        startActivity(intent);
    }
    private void changeActivity2(){
        Intent intent=new Intent(SettingActivity.this,GirisActivity.class);
        startActivity(intent);
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

    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private Boolean checkForInfo(){
        if(encodedImage==null){
            showToast("Profil Resmi Seçin!");
            return false;
        }
        else if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("İsim Girin!");
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
    private boolean checkForDelete(String email){
        SQLiteDatabase sldb=veriTabanı.getReadableDatabase();
        Cursor kullanıcı=sldb.rawQuery("Select id from user where email=?", new String[] {email});
        if(kullanıcı.getCount()>0){
            return true;
        }
        return false;


    }
    private void setListeners() {
        binding.textAddImage.setOnClickListener(v->{
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);

        });

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
                            binding.pp.setImageBitmap(bitmap);
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
    public void guncelle(String email,String isim,String parola,String encodedImage){
        SQLiteDatabase sldb=veriTabanı.getWritableDatabase();
        ContentValues kullanıcı=new ContentValues();
        kullanıcı.put("isim",isim);
        kullanıcı.put("parola",parola);
        kullanıcı.put("fotograf",encodedImage);
        long etkilenen=sldb.update("user",kullanıcı,"email=?",new String[] {email});
        if(etkilenen!=-1){
            showToast("Güncellendi!");
        }
        else{
            showToast("Hata!");
        }
    }
    public void sil(String email){
        SQLiteDatabase sldb=veriTabanı.getWritableDatabase();
        long etkilenen=sldb.delete("user","email=?",new String[] {email});
        if(etkilenen!=-1){
            showToast("Silindi!");
        }
        else{
            showToast("Hata!");
        }
    }
}