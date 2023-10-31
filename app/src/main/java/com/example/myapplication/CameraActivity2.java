package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper; // 추가
import android.os.Message; // 추가
import android.os.MessageQueue; // 추가
import android.os.MessageQueue.IdleHandler; // 추가
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import java.util.Locale;

public class CameraActivity2 extends AppCompatActivity {

    ImageView btn_picture, btn_sound;
    TextToSpeech textToSpeech;
    ImageView gifImageView;
    private boolean gifVisible = false; // GIF 이미지의 가시성 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        gifImageView = findViewById(R.id.loading);
        Glide.with(this).asGif().load(R.drawable.loading).into(gifImageView);
        gifImageView.setVisibility(View.INVISIBLE); // 초기에 숨김

        btn_picture = findViewById(R.id.btn_picture);
        btn_sound = findViewById(R.id.btn_sound);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int langResult = textToSpeech.setLanguage(Locale.KOREA); // 한 영 설정

                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // 언어 데이터가 없거나 지원되지 않는 경우 처리
                        // 필요에 따라 에러 처리를 추가하세요.
                    }
                }
            }
        });

        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takePicture();

            }
        });

        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gifImageView.setVisibility(View.VISIBLE); // GIF 이미지 표시
                // 3초 후에 GIF 이미지를 숨김
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gifImageView.setVisibility(View.INVISIBLE);
                        if (textToSpeech != null) {

                            String textToRead = "오늘은 좋은 날씨로 시작해 산책, 커피, 아침 뉴스를 즐겼고, 회의에서 프로젝트 논의하고 계획을 세웠다. 점심엔 동료와 식사하며 이야기하고, 오후에는 업무에 집중하고 체육관에서 운동했다. 저녁엔 가족과 맛있는 식사로 하루를 마무리했다.";
                            textToSpeech.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    }
                }, 3000); // 3초 지연

            }
        });
    }

    // 사진찍기
    private static final int REQUEST_IMAGE_CODE = 101;
    public void takePicture() {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE);
        }
    }
}
