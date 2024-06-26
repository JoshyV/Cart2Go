package com.joshy.cart2go;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.joshy.cart2go.backend.JoshEncrypter;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QuickSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search);
        findViewById(R.id.backButtonQS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        findViewById(R.id.SearchButtonQS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(new ScanContract(), result->{
        if(result.getContents() != null){
            try{
                String ScannedCode = JoshEncrypter.decode(result.getContents());

            }catch (Throwable t){
                Toast.makeText(getApplicationContext(), "That's not a crate", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "That's not a crate", Toast.LENGTH_SHORT).show();
        }
    });
}