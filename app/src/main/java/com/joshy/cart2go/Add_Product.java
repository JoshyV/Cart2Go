package com.joshy.cart2go;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.joshy.cart2go.backend.Product;
import com.joshy.cart2go.backend.ProductService;
import com.joshy.cart2go.backend.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Product extends AppCompatActivity {

    private static final String TAG = "Add_Product";

    private EditText barcode, brand, variant, volume, description;
    private CheckBox brandcheck, variantcheck, volumecheck, descriptioncheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        barcode = findViewById(R.id.ap_barcode);
        brand = findViewById(R.id.ap_brand);
        variant = findViewById(R.id.ap_variant);
        volume = findViewById(R.id.ap_volume);
        description = findViewById(R.id.ap_description);

        brandcheck = findViewById(R.id.bcheckbox);
        variantcheck = findViewById(R.id.vcheckbox);
        volumecheck = findViewById(R.id.vlcheckbox);
        descriptioncheck = findViewById(R.id.descheckbox);
        
        findViewById(R.id.addProductButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    private void addProduct() {
        String barcodeText = String.valueOf(barcode.getText());
        String brandText = brand.getText().toString();
        String variantText = variant.getText().toString();
        String volumeText = volume.getText().toString();
        String descriptionText = description.getText().toString();

        Product newProduct = new Product(barcodeText, brandText, variantText, volumeText, descriptionText);

        ProductService productService = RetrofitClient.getClient().create(ProductService.class);
        Call<Void> addCall = productService.addProduct(newProduct);
        addCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    EditText[] editTexts = {brand, variant, volume, description};
                    CheckBox[] checkBoxes = {brandcheck, variantcheck, volumecheck, descriptioncheck};
                    for (int i = 0; i < editTexts.length; i++) {
                        if (!checkBoxes[i].isChecked()) {
                            editTexts[i].setText("");
                        }
                    }
                    Toast.makeText(getApplicationContext(),"Product added successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"(Adding Product) Error: "+ response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"(Adding Product) Error: "+ t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goBack() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}