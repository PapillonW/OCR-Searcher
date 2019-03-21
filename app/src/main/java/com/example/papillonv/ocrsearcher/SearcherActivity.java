package com.example.papillonv.ocrsearcher;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class SearcherActivity extends AppCompatActivity {

    public TextView textView;
    public ImageView imageView;
    public Button takePictureButton;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seacrher);


        textView =  findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        takePictureButton = findViewById(R.id.button);

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "OCR_Image");
                values.put(MediaStore.Images.Media.DESCRIPTION, "OCR Image From your Camera");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            imageView.setImageBitmap(bitmap);

            OCRReader(bitmap);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void OCRReader(Bitmap bitmap)
    {

        TextRecognizer textRecognizer = new  TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational())
        {
            Toast.makeText(this, "Kullanılamıyor", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> items = textRecognizer.detect(frame);

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); i++)
            {
                TextBlock myItem = items.valueAt(i);
                stringBuilder.append(myItem.getValue());
                stringBuilder.append(" \n");
            }

            String[] str = stringBuilder.toString().split("[?]",1);

            String a = str[0];
            textView.setText(a);

            SearchText();
        }

    }

    public void SearchText()
    {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
        startActivity(new Intent (Intent.ACTION_VIEW,Uri.parse("https://www.google.com/#q=" + textView.getText())));
    }
}
