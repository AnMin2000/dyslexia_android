package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Handler를 사용하여 2초 뒤에 SignUpActivity로 넘어가도록 설정
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티를 종료하려면 이 코드를 추가합니다.
            }
        }, 2000); // 2000 밀리초 (2초) 후에 실행
    }
}
