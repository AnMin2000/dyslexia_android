package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageActivity extends AppCompatActivity {

    TableLayout tableLayout;
    int numColumns = 2; // 열 수
    int rowCount = 0; // 현재 행 수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        tableLayout = findViewById(R.id.tableLayout3);

        // 총 사진 갯수를 가져오는 AsyncTask 실행
        new GetPhotoCountTask().execute("http://172.29.27.192:8080/getPhotoCount");
    }

    private class GetPhotoCountTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... urls) {
            String url = urls[0];

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                byte[] responseBytes = new byte[1024];
                int bytesRead;
                StringBuilder response = new StringBuilder();
                while ((bytesRead = inputStream.read(responseBytes)) != -1) {
                    response.append(new String(responseBytes, 0, bytesRead));
                }
                inputStream.close();

                return Integer.parseInt(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result != null) {
                // 총 사진 갯수를 확인한 후, 해당 갯수만큼 요청을 보내는 AsyncTask 실행
                for (int i = 0; i < result; i++) {
                    new DownloadImageTask().execute("http://172.29.27.192:8080/getPhoto?index=" + i);
                }
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                byte[] imageBytes = new byte[1024];
                int bytesRead;
                StringBuilder response = new StringBuilder();
                while ((bytesRead = inputStream.read(imageBytes)) != -1) {
                    response.append(new String(imageBytes, 0, bytesRead));
                }
                inputStream.close();

                byte[] decodedString = android.util.Base64.decode(response.toString(), android.util.Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                if (rowCount % numColumns == 0) {
                    // 새로운 행을 추가
                    TableRow currentRow = new TableRow(ImageActivity.this);
                    tableLayout.addView(currentRow);

                    // TableRow의 크기를 설정
                    currentRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            500)); // TableRow의 높이를 500dp로 고정
                }

                // ImageView를 생성하고 추가
                ImageView imageView = new ImageView(ImageActivity.this);

                // ImageView의 크기를 설정
                imageView.setLayoutParams(new TableRow.LayoutParams(
                        250, // TableRow의 가로를 250dp로 고정
                        280)); // TableRow의 높이를 280dp로 고정
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setAdjustViewBounds(true);
                imageView.setImageBitmap(bitmap);

                // 현재 행에 ImageView 추가
                TableRow currentRow = (TableRow) tableLayout.getChildAt(rowCount / numColumns);
                currentRow.addView(imageView);
                rowCount++;
            }
        }
    }
}