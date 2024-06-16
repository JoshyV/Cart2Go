package com.joshy.cart2go.backend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joshy.cart2go.R;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Retrieve product details from intent extras
        Intent intent = getIntent();
        String barcode = intent.getStringExtra("barcode");
        String brand = intent.getStringExtra("brand");
        String variant = intent.getStringExtra("variant");
        String volume = intent.getStringExtra("volume");
        String description = intent.getStringExtra("description");
        String imageurl = intent.getStringExtra("image");

        // Display product details in TextViews
        TextView BarcodeTextView = findViewById(R.id.text_bracode);
        TextView brandTextView = findViewById(R.id.text_brand);
        TextView VariantTextView = findViewById(R.id.text_variant);
        TextView volumeTextView = findViewById(R.id.text_volume);
        TextView descriptionTextView = findViewById(R.id.text_description);

        BarcodeTextView.setText(imageurl);
        brandTextView.setText("Brand: " + brand);
        VariantTextView.setText("Variant: " + variant);
        volumeTextView.setText("Volume: " + volume);
        descriptionTextView.setText("Description: " + description);

        // Set OnClickListener for the back button
        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}