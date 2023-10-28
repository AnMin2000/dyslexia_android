package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.bumptech.glide.Glide; // Glide 라이브러리 추가
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class PictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // 여기서 이미지 파일 이름을 변경하여 이미지를 추가하세요.
        String[] imageFileNames = {"a1", "a2", "a3", "a4","a5", "a6", "a7", "a13","a9", "a10", "a11", "a12"};

        int columnCount = 2; // 열 수
        int rowCount = (int) Math.ceil((double) imageFileNames.length / columnCount);

        Context context = this;

        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);

            for (int j = 0; j < columnCount; j++) {
                int index = i * columnCount + j;
                if (index < imageFileNames.length) {
                    ImageView imageView = new ImageView(context);

                    // Glide를 사용하여 이미지 로딩 및 크기 조절
                    Glide.with(context)
                            .load(getImageResource(imageFileNames[index]))
                            .override(360, 300) // 이미지 크기 조절
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐시 사용
                            .into(imageView);

                    // TableRow의 높이를 동적으로 조절
                    int tableRowHeight = getResources().getDimensionPixelSize(R.dimen.image_height); // 세로 길이 145dp
                    TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            tableRowHeight
                    );
                    tableRow.setLayoutParams(rowParams);


                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the clicked image's filename
                            String clickedImageName = imageFileNames[index];
                            if(clickedImageName.equals("a12"))
                            {
                                Intent intent = new Intent(PictureActivity.this,SummarizeActivity.class); // cameraActivity
                                startActivity(intent);
                            }
                            // Log the filename to see which picture was clicked
                            Log.d("PictureActivity", "Clicked image: " + clickedImageName);

                        }
                    });

                    tableRow.addView(imageView);
                }
            }

            tableLayout.addView(tableRow);
        }
    }

    // 이미지 리소스 아이디를 가져오는 메서드
    private int getImageResource(String imageName) {
        return getResources().getIdentifier(imageName, "drawable", getPackageName());
    }
}
