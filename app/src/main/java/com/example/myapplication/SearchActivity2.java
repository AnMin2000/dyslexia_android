package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchActivity2 extends AppCompatActivity {

    Button button;
    String pw;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchSuccessDialog("dksals");

            }
        });
    }
    private void showSearchSuccessDialog(final String pw) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 찾기 성공");
        builder.setMessage("비밀번호 : " + pw);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                // '확인' 버튼을 클릭하면 다음 화면으로 이동
                Intent intent = new Intent(SearchActivity2.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}