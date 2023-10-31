package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.dto.Picture;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends AppCompatActivity {
    private ImageView btn_picture, album;

    File photoFile;
    String timeStamp, fileName;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        btn_picture = findViewById(R.id.btn_picture);
        album = findViewById(R.id.album);

        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CameraActivity.this,PictureActivity.class); // cameraActivity
                startActivity(intent);
            }
        });
    }

    // 사진찍기
    static final int REQUEST_IMAGE_CAPTURE = 1;


    // 결과값 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ActivityResult", "Request Code: " + requestCode + ", Result Code: " + resultCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (currentPhotoPath != null) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
                if (imageBitmap != null) {
                    int rotation = getImageRotation(currentPhotoPath);
                    imageBitmap = rotateBitmap(imageBitmap, rotation);



                    uploadImageToServer(photoFile);


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] byteArray = stream.toByteArray();

                    Intent intent = new Intent(CameraActivity.this, OcrActivity.class);
                    String id = getIntent().getStringExtra("id");
                    intent.putExtra("id", id);
                    intent.putExtra("image", byteArray);
                    intent.putExtra("photoFile", photoFile);
                    intent.putExtra("fileName", fileName);
                    //System.out.println("******************************************");
                    startActivity(intent);


                } else {
                    Log.e("ActivityResult", "Bitmap could not be loaded from file");
                }
            } else {
                Log.e("ActivityResult", "currentPhotoPath is null");
            }
        } else {
            Log.e("ActivityResult", "Invalid request or result code");
        }
    }



    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.myapplication.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // 이미지 업로드 메서드
    private void uploadImageToServer(File imageFile) {
        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);


            fileName = imageFile.getName();
            //
            String id = getIntent().getStringExtra("id");
            RequestBody idPart = RequestBody.create(MediaType.parse("text/plain"), id);
            //



            Call<String> call = RetrofitBuilder.api.getCameraResponse(body, idPart);


            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d("Upload", response.body());
                    } else {
                        Log.e("Upload", "이미지 업로드 실패");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Upload", "이미지 업로드 실패", t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 이미지 회전 정보 가져오기
    private int getImageRotation(String imagePath) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 이미지 회전하기
    private Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            try {
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (bitmap != rotatedBitmap) {
                    bitmap.recycle();
                }
                return rotatedBitmap;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        }
        return bitmap;
    }
    private RequestBody pictureToRequestBody(Picture picture) {
        // Picture 객체를 바이트 배열로 변환하는 로직을 작성하세요.
        // 예를 들어, Gson 라이브러리를 사용하여 JSON 문자열로 변환한 후 바이트 배열로 변환할 수 있습니다.
        // Gson 사용 예시:
        Gson gson = new Gson();
        String json = gson.toJson(picture);
        return RequestBody.create(MediaType.parse("application/json"), json.getBytes());
    }
}
