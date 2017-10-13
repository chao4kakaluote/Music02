package com.example.administrator.music03.Activity;

import android.content.Intent;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.music03.R;

public class FingerAuthen extends AppCompatActivity {

    private Button startAuthen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_authen);
        startAuthen=(Button)findViewById(R.id.authen);
        startAuthen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                checkFingerPrint();
            }
        });
    }

    private void checkFingerPrint(){
        FingerprintManagerCompat.from(this).authenticate(null,0,null,new MyCallBack(),null);
    }
    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback{
        private static final String TAG = "MyCallBack";
        @Override
        public void onAuthenticationFailed(){
            Toast.makeText(FingerAuthen.this,"识别失败",Toast.LENGTH_SHORT).show();
        }
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result){
           Intent intent=new Intent(FingerAuthen.this,MainActivity1.class);
            startActivity(intent);
        }
    }
}
