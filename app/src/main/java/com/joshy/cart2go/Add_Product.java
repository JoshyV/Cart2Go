package com.joshy.cart2go;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joshy.cart2go.backend.Product;
import com.joshy.cart2go.backend.ProductService;
import com.joshy.cart2go.backend.RetrofitClient;
import com.joshy.cart2go.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.Manifest;

public class Add_Product extends AppCompatActivity {

    Bitmap bitmap;
    private static final String TAG = "Add_Product";
    private ImageView imageView2, imagepreview;
    private EditText barcode, brand, variant, volume, description;
    private CheckBox brandcheck, variantcheck, volumecheck, descriptioncheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode() == Activity.RESULT_OK) {
                    Intent data = o.getData();
                    Uri imageUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imagepreview.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        imagepreview = (ImageView) findViewById(R.id.imagepreview);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
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
                try {
                    addProduct();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        imagepreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
    }

    private void addProduct() throws IOException {
        if(bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody);

            String barcodeText = String.valueOf(barcode.getText());
            String brandText = brand.getText().toString();
            String variantText = variant.getText().toString();
            String volumeText = volume.getText().toString();
            String descriptionText = description.getText().toString();

            Product newProduct = new Product(barcodeText, brandText, variantText, volumeText, descriptionText);

            RequestBody requestBodyd = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(newProduct));
            ProductService productService = RetrofitClient.getClient("4a6991e578554757df7656ac3ac44b73eb9be43a54e9835fdd0444805fd346f29497c5b46a81e484f9598c915200e9fd3fc9b74a6f1e0e0798cdd0879a33439b").create(ProductService.class);
            Call<Void> addCall = productService.addProduct(requestBodyd, imagePart);
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
                        Toast.makeText(getApplicationContext(), "Product added successfully", Toast.LENGTH_LONG).show();
                    } else {
                        brand.setText(response.toString());
                        variant.setText(new Gson().toJson(newProduct));
                        Toast.makeText(getApplicationContext(), "(Adding Product) Error1: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "(Adding Product) Error2: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }else Toast.makeText(getApplicationContext(), "Select image first!", Toast.LENGTH_SHORT).show();
    }
    private void goBack() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}