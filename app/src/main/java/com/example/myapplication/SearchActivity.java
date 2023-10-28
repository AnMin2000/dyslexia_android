package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.dto.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    EditText userName, userPhone;
    Button button;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userName = findViewById(R.id.name);
        userPhone = findViewById(R.id.number);
        button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            String name  = userName.getText().toString();
            String phone = userPhone.getText().toString();

            User user = new User();
            user.setName(name);
            user.setPhone(phone);
            // System.out.println(user.getId());
            Log.d("BUTTON CLICKED", "name: " + name + ", phone: " + phone);
            Search(user);


        });
    }
    private String Search(User user) {
        Call<User> call = RetrofitBuilder.api.getSearchResponse(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println(""+response);
                if (response.isSuccessful()) {
                    User user2 = response.body();
                     pw = user2.getPassword();

                     if(pw != null)
                     {
                         showSearchSuccessDialog(pw);
                     }
                     else {
                         showSearchFailDialog();
                     }
                } else {
                    Log.d("RESPONSE", "FAILURE");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("CONNECTION FAILURE: ", t.getLocalizedMessage());
            }
        });
        return pw;
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
                Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showSearchFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 찾기 실패");
        builder.setMessage("이름 또는 전화번호가 올바르지 않습니다.");
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