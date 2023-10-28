package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Locale;

public class SummarizeActivity extends AppCompatActivity {
    TextToSpeech textToSpeech;
    Button soundButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summarize);

        soundButton = findViewById(R.id.soundButton);
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
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textToSpeech != null) {
                    String textToRead = "오늘은 좋은 날씨로 시작해 산책, 커피, 아침 뉴스를 즐겼고, 회의에서 프로젝트 논의하고 계획을 세웠다. 점심엔 동료와 식사하며 이야기하고, 오후에는 업무에 집중하고 체육관에서 운동했다. 저녁엔 가족과 맛있는 식사로 하루를 마무리했다.";
                    textToSpeech.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }
}