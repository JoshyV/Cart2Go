package com.joshy.cart2go;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joshy.cart2go.backend.AddProductEdit;
import com.joshy.cart2go.backend.CaptureAct;
import com.joshy.cart2go.backend.Product;
import com.joshy.cart2go.backend.ProductService;
import com.joshy.cart2go.backend.RetrofitClient;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Add_Product extends AppCompatActivity {

    Bitmap bitmap,resizedBitmap,rotatedBitmap;
    private static final String TAG = "Add_Product";
    private ImageView imagepreview;
    private TextView barcode, brand, variant, volume, description;
    boolean brandcheck, variantcheck, volumecheck, descriptioncheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        imagepreview = (ImageView) findViewById(R.id.imagepreview);
        barcode = findViewById(R.id.BarcodeDetail);
        brand = findViewById(R.id.BrandDetail);
        variant = findViewById(R.id.VariantDetail);
        volume = findViewById(R.id.VolumeDetail);
        description = findViewById(R.id.DescriptionDetail);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode() == Activity.RESULT_OK) {
                    Intent data = o.getData();
                    Uri imageUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        // Check the image's orientation
                        ExifInterface exif = new ExifInterface(getRealPathFromURI(imageUri));
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                        // Rotate the bitmap if necessary
                        Matrix matrix = new Matrix();
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                matrix.postRotate(180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                matrix.postRotate(270);
                                break;
                        }
                        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        resizedBitmap = resizeBitmap(rotatedBitmap, 800);
                        imagepreview.setImageBitmap(resizedBitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ActivityResultLauncher<Intent> editActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode() == Activity.RESULT_OK) {
                    Intent data = o.getData();
                    if(data != null) {
                        String BrandEdit = data.getStringExtra("BrandEdit");
                        String VariantEdit = data.getStringExtra("VariantEdit");
                        String VolumeEdit = data.getStringExtra("VolumeEdit");
                        String DescriptionEdit = data.getStringExtra("DescriptionEdit");
                        brand.setText(BrandEdit);
                        variant.setText(VariantEdit);
                        volume.setText(VolumeEdit);
                        description.setText(DescriptionEdit);

                        brandcheck = data.getBooleanExtra("BrandC", false);
                        variantcheck = data.getBooleanExtra("isChecked", false);
                        volumecheck = data.getBooleanExtra("isChecked", false);
                        descriptioncheck = data.getBooleanExtra("isChecked", false);
                    }else{

                    }
                }
            }
        });
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
        findViewById(R.id.Editbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Product.this, AddProductEdit.class);
                intent.putExtra("BrandCU", brand.getText().toString());
                intent.putExtra("VariantCU", brand.getText().toString());
                intent.putExtra("VolumeCU", variant.getText().toString());
                intent.putExtra("DescCU", description.getText().toString());
                editActivityResultLauncher.launch(intent);
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
        imagepreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
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
                        if (searchResults.isEmpty()) {
                            barcode.setText(result.getContents());
                        }else{
                            Toast.makeText(getApplicationContext(), "Product already registered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to catch the product", Toast.LENGTH_SHORT).show();
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
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
    private void addProduct() throws IOException {

        if(resizedBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody);

            String barcodeText = barcode.getText().toString();
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
                        TextView[] TextView = {brand, variant, volume, description};
                        Boolean[] Boolean = {brandcheck, variantcheck, volumecheck, descriptioncheck};
                        for (int i = 0; i < TextView.length; i++) {
                            if (!Boolean[i]) {
                                TextView[i].setText(TextView[i].getHint().toString());
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