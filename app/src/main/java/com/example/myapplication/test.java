package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class test extends AppCompatActivity {

    Context cThis;//context 설정
    String LogTT="[STT]";//LOG타이틀
    //음성 인식용
    Intent SttIntent;
    SpeechRecognizer mRecognizer;
    //음성 출력용
    TextToSpeech tts;

    // 화면 처리용
    Button btnSttStart;
    EditText txtInMsg;
    EditText txtSystem;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cThis=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //음성인식
        SttIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplicationContext().getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");//한국어 사용
        mRecognizer=SpeechRecognizer.createSpeechRecognizer(cThis);
        mRecognizer.setRecognitionListener(listener);

        //음성출력 생성, 리스너 초기화
        tts=new TextToSpeech(cThis, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=android.speech.tts.TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        //버튼설정
        btnSttStart=(Button)findViewById(R.id.btn_stt_start);
        btnSttStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("음성인식 시작!");
                if(ContextCompat.checkSelfPermission(cThis, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(test.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
                    //권한을 허용하지 않는 경우
                }else{
                    //권한을 허용한 경우
                    try {
                        mRecognizer.startListening(SttIntent);
                    }catch (SecurityException e){e.printStackTrace();}
                }
            }
        });
        txtInMsg=(EditText)findViewById(R.id.txtInMsg);
        txtSystem=(EditText)findViewById(R.id.txtSystem);
        //어플이 실행되면 자동으로 1초뒤에 음성 인식 시작
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtSystem.setText("어플 실행됨--자동 실행-----------"+"\r\n"+txtSystem.getText());
                btnSttStart.performClick();
            }
        },1000);//바로 실행을 원하지 않으면 지워주시면 됩니다
    }
    private RecognitionListener listener=new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            txtSystem.setText("onReadyForSpeech..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onBeginningOfSpeech() {
            txtSystem.setText("지금부터 말을 해주세요..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            txtSystem.setText("onBufferReceived..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onEndOfSpeech() {
            txtSystem.setText("onEndOfSpeech..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onError(int i) {
            txtSystem.setText("천천히 다시 말해 주세요..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onResults(Bundle results) {
            String key= "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult =results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            txtInMsg.setText(rs[0]+"\r\n"+txtInMsg.getText());
            FuncVoiceOrderCheck(rs[0]);
            mRecognizer.startListening(SttIntent);

        }

        @Override
        public void onPartialResults(Bundle bundle) {
            txtSystem.setText("onPartialResults..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            txtSystem.setText("onEvent..........."+"\r\n"+txtSystem.getText());
        }
    };
    //입력된 음성 메세지 확인 후 동작 처리
    private void FuncVoiceOrderCheck(String VoiceMsg){
        if(VoiceMsg.length()<1)return;

        VoiceMsg=VoiceMsg.replace(" ","");//공백제거

        if(VoiceMsg.indexOf("카카오톡")>-1 || VoiceMsg.indexOf("카톡")>-1){
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.kakao.talk");
            startActivity(launchIntent);
            onDestroy();
        }//카카오톡 어플로 이동
        if(VoiceMsg.indexOf("전등 꺼")>-1 || VoiceMsg.indexOf("불꺼")>-1){
            FuncVoiceOut("전등을 끕니다");//전등을 끕니다 라는 음성 출력
        }
    }



    //음성 메세지 출력용
    private void FuncVoiceOut(String OutMsg){
        if(OutMsg.length()<1)return;

        tts.setPitch(1.0f);//목소리 톤1.0
        tts.setSpeechRate(1.0f);//목소리 속도
        tts.speak(OutMsg,TextToSpeech.QUEUE_FLUSH,null);

        //어플이 종료할때는 완전히 제거

    }
    //카톡으로 이동을 했는데 음성인식 어플이 종료되지 않아 계속 실행되는 경우를 막기위해 어플 종료 함수
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts!=null){
            tts.stop();
            tts.shutdown();
            tts=null;
        }
        if(mRecognizer!=null){
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer=null;
        }
    }
}