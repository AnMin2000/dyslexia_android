package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.myapplication.dto.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginActivity extends AppCompatActivity {

    private String id = "";
    private String pw = "";

    EditText userId, userPassword, SignUpView, SearchView;
    Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button button = findViewById(R.id.button);
        userId = findViewById(R.id.userId);
        userPassword = findViewById(R.id.userPassword);
        SignUpView = findViewById(R.id.SignUpView);
        SearchView = findViewById(R.id.SearchView);
        switch1 = findViewById(R.id.switch1);

        SignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity2.class); // cameraActivity
                startActivity(intent);
            }
        });

        // loginbutton 클릭 시 입력 막기
        SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SearchActivity2.class); // cameraActivity
                startActivity(intent);
            }
        });

        button.setOnClickListener(view -> {


        });
//        SignUpView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userId.setText(null);
//                userPassword.setText(null);
//                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class); // cameraActivity
//                startActivity(intent);
//            }
//        });
//
//        // loginbutton 클릭 시 입력 막기
//        SearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userId.setText(null);
//                userPassword.setText(null);
//                Intent intent = new Intent(LoginActivity.this,SearchActivity.class); // cameraActivity
//                startActivity(intent);
//            }
//        });
//
//        button.setOnClickListener(view -> {
//            id = userId.getText().toString();
//            pw = userPassword.getText().toString();
//            User user = new User();
//            user.setId(id);
//            user.setPassword(pw);
//           // System.out.println(user.getId());
//            Log.d("BUTTON CLICKED", "id: " + user.getId() + ", pw: " + user.getPassword());
//            login(user);
//
//        });
    }

    private void login(User user) {
        Call<Integer> call = RetrofitBuilder.api.getLoginResponse(user);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                System.out.println(""+response);
                if (response.isSuccessful()) {
                    Log.d("RESPONSE: ", String.valueOf(response.body()));
                    int count = response.body();

                    if(count > 0) {

                        Intent intent = new Intent(LoginActivity.this, CameraActivity.class); // cameraActivity
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                    else {
                        showLoginFailureDialog();
                    }

                } else {
                    Log.d("RESPONSE", "FAILURE");

                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("CONNECTION FAILURE: ", t.getLocalizedMessage());

            }
        });
    }
    private void showLoginFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 실패");
        builder.setMessage("아이디 또는 비밀번호가 올바르지 않습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
