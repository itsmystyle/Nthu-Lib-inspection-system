package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Gallery_View_Activity extends AppCompatActivity {
    ImageView imageView;
    Button button_save;
    Button button_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);

        byte[] byteArray = getIntent().getByteArrayExtra(WebServerContract.IMAGE_CONTENT);
        final Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        button_save = (Button) findViewById(R.id.gallery_view_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getDisc();
                if(!file.exists() && !file.mkdirs()){
                    Toast.makeText(v.getContext(), "Unable to save", Toast.LENGTH_SHORT).show();
                    return ;
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
                String date = simpleDateFormat.format(new Date());
                String name = "Img_" + date + ".jpg";
                String file_name = file.getAbsolutePath() + "/" + name;
                File new_file = new File(file_name);
                try{
                    FileOutputStream fileOutputStream = new FileOutputStream(new_file);
                    Bitmap bitmap = bmp;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    Toast.makeText(v.getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    fileOutputStream.flush();
                    fileOutputStream.close();

                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                sendBroadcast(intent);
            }
        });

        button_close = (Button) findViewById(R.id.gallery_view_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.imageView_image);
        imageView.setImageBitmap(bmp);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
        photoViewAttacher.update();
    }

    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Saved Images");
    }
}
