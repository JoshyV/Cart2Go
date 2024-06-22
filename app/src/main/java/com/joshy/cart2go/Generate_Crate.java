package com.joshy.cart2go;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joshy.cart2go.backend.BarcodeGenerator;
import com.joshy.cart2go.backend.JoshEncrypter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Generate_Crate extends AppCompatActivity {
    TextView cratenumber, CodeText;
    Button copycode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_crate);
        cratenumber = findViewById(R.id.cratenumber);
        CodeText = findViewById(R.id.CodeText);
        copycode = findViewById(R.id.copycode);
        ImageView qrCodeImageView = findViewById(R.id.barcodeHolder);
        findViewById(R.id.GenerateBarcode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(v.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Crate Code Generator:");
                builder.setMessage("Enter the crate number:");
                builder.setView(editText);

                builder.setPositiveButton("Generate Crate", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = editText.getText().toString();
                        String newtext = JoshEncrypter.encode(text).toString();
                        cratenumber.setText(text);
                        CodeText.setText(newtext);
                        int width = 800; // Replace with your desired width
                        int height = 800; // Replace with your desired height
                        Bitmap qrCodeBitmap = BarcodeGenerator.generateQRCode(newtext, width, height);
                        qrCodeImageView.setImageBitmap(qrCodeBitmap);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        findViewById(R.id.DownloadQR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the bitmap from the ImageView

                Bitmap bitmap = ((BitmapDrawable) qrCodeImageView.getDrawable()).getBitmap();

                // Create a new file in the Pictures directory
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "Cart2Go/" + "CrateCodes");
                values.put(MediaStore.Images.Media.TITLE, "Crate_"+cratenumber.getText().toString()+"");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Barcode image");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                // Save the bitmap to the file
                OutputStream outputStream = null;
                try {
                    outputStream = getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    Toast.makeText(Generate_Crate.this, "Barcode saved to Pictures/Cart2Go/CrateCodes", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Generate_Crate.this, "Failed to save barcode", Toast.LENGTH_SHORT).show();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        copycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Crate Code", CodeText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Generate_Crate.this, "Crate code copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.branding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}