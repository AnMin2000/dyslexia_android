package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.dto.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText userName, userPhone, userId, userPassword;
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.name);
        userPhone = findViewById(R.id.phone);
        userId = findViewById(R.id.userId);
        userPassword = findViewById(R.id.userPassword);
        button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            String name  = userName.getText().toString();
            String phone = userPhone.getText().toString();
            String id = userId.getText().toString();
            String pw = userPassword.getText().toString();
            User user = new User();
            user.setName(name);
            user.setPhone(phone);
            user.setId(id);
            user.setPassword(pw);
            // System.out.println(user.getId());
            Log.d("BUTTON CLICKED", "id: " + id + ", pw: " + pw);
            SignUp(user);
            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class); // cameraActivity
            startActivity(intent);
        });
    }
    private void SignUp(User user) {
        Call<String> call = RetrofitBuilder.api.getSignUpResponse(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(""+response);
                if (response.isSuccessful()) {
                    Log.d("RESPONSE: ", response.body());
                } else {
                    Log.d("RESPONSE", "FAILURE");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("CONNECTION FAILURE: ", t.getLocalizedMessage());
            }
        });
    }
}