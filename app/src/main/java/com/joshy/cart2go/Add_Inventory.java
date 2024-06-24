package com.joshy.cart2go;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joshy.cart2go.backend.BarcodeGenerator;
import com.joshy.cart2go.backend.CaptureAct;
import com.joshy.cart2go.backend.Inventory;
import com.joshy.cart2go.backend.JoshEncrypter;
import com.joshy.cart2go.backend.Product;
import com.joshy.cart2go.backend.ProductService;
import com.joshy.cart2go.backend.RetrofitClient;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Add_Inventory extends AppCompatActivity {

    String Barcode,Brand,Variant,Volume,Description;
    TextView barcodeINV,brandINV,variantINV,volumeINV,descriptioINV,expiryINV,crateINV,quantityINV;
    ImageView imageINV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);

        //Initiate Vars
        barcodeINV = findViewById(R.id.barcodeINV);
        brandINV = findViewById(R.id.brandINV);
        variantINV = findViewById(R.id.variantINV);
        volumeINV = findViewById(R.id.volumeINV);
        descriptioINV = findViewById(R.id.descriptioINV);
        expiryINV = findViewById(R.id.expiryINV);
        crateINV = findViewById(R.id.crateINV);
        quantityINV = findViewById(R.id.quantityINV);
        imageINV = findViewById(R.id.imageINV);


        findViewById(R.id.branding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        findViewById(R.id.BarcodeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setPrompt("Scan the barcode of the product");
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                options.setCaptureActivity(CaptureAct.class);
                barLauncher.launch(options);
            }
        });
        findViewById(R.id.ExpiryDateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Add_Inventory.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                                expiryINV.setText(selectedDate);
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
        findViewById(R.id.CaseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setPrompt("Scan the QRCode of the crate");
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                options.setCaptureActivity(CaptureAct.class);
                qrLauncher.launch(options);
            }
        });
        findViewById(R.id.Quantitybutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(v.getContext());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getContext());
                builder.setTitle("Enter Quantity:");
                builder.setMessage("Quantity:");
                builder.setView(editText);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String quantity = editText.getText().toString();
                        if (quantity.isEmpty() || !quantity.matches("\\d+")) { // check if empty or not a number
                            Toast.makeText(v.getContext(), "Invalid input. Please enter a valid quantity. 0-9", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        quantityINV.setText(quantity);
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
        findViewById(R.id.addProductButtonINV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = barcodeINV.getText().toString();
                String expiry = expiryINV.getText().toString();
                String crate = crateINV.getText().toString();
                String quantity = quantityINV.getText().toString();
                if(barcodeINV.getText().toString().trim().equals("Barcode") || expiryINV.getText().toString().trim().equals("Expiry Date") || crateINV.getText().toString().trim().equals("Crate") || quantityINV.getText().toString().trim().equals("Quantity") || barcodeINV.getText().toString().trim().isEmpty() || expiryINV.getText().toString().trim().isEmpty() || crateINV.getText().toString().trim().isEmpty() || quantityINV.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill up the information", Toast.LENGTH_LONG).show();
                    return;
                }

                Inventory inventory = new Inventory(barcode,crate,expiry,quantity);
                Retrofit retrofit = RetrofitClient.getClient("4a6991e578554757df7656ac3ac44b73eb9be43a54e9835fdd0444805fd346f29497c5b46a81e484f9598c915200e9fd3fc9b74a6f1e0e0798cdd0879a33439b");
                ProductService service = retrofit.create(ProductService.class);
                Call<Inventory> call = service.addInventory(inventory);
                call.enqueue(new Callback<Inventory>() {
                    @Override
                    public void onResponse(Call<Inventory> call, Response<Inventory> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Product added to inventory", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "(Inventory) Error1:", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Inventory> call, Throwable t) {
                        descriptioINV.setText(t.getMessage());
                        Toast.makeText(getApplicationContext(), "(Inventory) Error2: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
         });

    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
        if(result.getContents() != null){
            Retrofit retrofit = RetrofitClient.getClient("4a6991e578554757df7656ac3ac44b73eb9be43a54e9835fdd0444805fd346f29497c5b46a81e484f9598c915200e9fd3fc9b74a6f1e0e0798cdd0879a33439b");
            ProductService service = retrofit.create(ProductService.class);
            Call<List<Product>> call = service.searchBarcode(result.getContents());
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        List<Product> searchResults = response.body();
                        if (searchResults != null) {
                            if(!searchResults.isEmpty()){
                                Barcode = searchResults.get(0).getBarcode();
                                Brand = searchResults.get(0).getBrand();
                                Variant = searchResults.get(0).getVariant();
                                Volume = searchResults.get(0).getVolume();
                                Description = searchResults.get(0).getDescription();
                                Picasso.with(getApplicationContext())
                                        .load(searchResults.get(0).getImage())
                                        .into(imageINV);

                                barcodeINV.setText(Barcode);
                                brandINV.setText(Brand);
                                variantINV.setText(Variant);
                                volumeINV.setText(Volume);
                                descriptioINV.setText(Description);
                            }else{
                                Toast.makeText(getApplicationContext(), "Product not registered", Toast.LENGTH_SHORT).show();
                                TextView[] textViews = {barcodeINV, brandINV, variantINV, volumeINV, descriptioINV};
                                String[] texts = {"Barcode", "Brand", "Variant", "Volume", "Description"};
                                for (int i = 0; i < textViews.length; i++) {
                                    textViews[i].setText(texts[i]);
                                }
                                return;
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Product not registered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error fetching product", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(getApplicationContext(), "Error! " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("TAG", "Error message: " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    });
    ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(new ScanContract(), result->{
        if(result.getContents() != null){
            try{
                String Text = JoshEncrypter.decode(result.getContents());
                crateINV.setText(Text);
            }catch (Throwable t){
                Toast.makeText(getApplicationContext(), "That's not a crate", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "That's not a crate", Toast.LENGTH_SHORT).show();
        }
    });
}