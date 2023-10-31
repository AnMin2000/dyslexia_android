package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class LoginActivity2 extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    EditText SignUpView, SearchView;

    TextView userId, userPassword;
    ImageView mike, mike2;
    Button button;
    int num;
    Switch switch1;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        button = findViewById(R.id.button);
        SignUpView = findViewById(R.id.SignUpView);
        SearchView = findViewById(R.id.SearchView);
        userId = findViewById(R.id.userId);
        userPassword = findViewById(R.id.userPassword);
        mike = findViewById(R.id.mike);
        mike2 = findViewById(R.id.mike2);
        switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // 스위치가 켜질 때 다음 화면으로 이동하도록 처리
                    Intent intent = new Intent(LoginActivity2.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        mike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 1;
                startSpeechToText();
            }
        });
        mike2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 2;
                startSpeechToText();
            }
        });

        SignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity2.this,SignUpActivity2.class); // cameraActivity
                startActivity(intent);
            }
        });

        // loginbutton 클릭 시 입력 막기
        SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity2.this,SearchActivity2.class); // cameraActivity
                startActivity(intent);
            }
        });

        button.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity2.this,CameraActivity2.class); // cameraActivity
            startActivity(intent);
        });

    }
    private void startSpeechToText() {
        // 권한 확인
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return;
        }

        // 음성 인식 액티비티 호출
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "음성 입력을 지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String recognizedText = result.get(0);

                if(num == 1) userId.setText(recognizedText);
                else userPassword.setText(recognizedText);
            }
        }
    }
}