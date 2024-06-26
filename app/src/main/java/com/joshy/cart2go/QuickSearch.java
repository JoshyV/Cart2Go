package com.joshy.cart2go;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.joshy.cart2go.backend.CaptureAct;
import com.joshy.cart2go.backend.Inventory;
import com.joshy.cart2go.backend.InventoryAdapter;
import com.joshy.cart2go.backend.JoshEncrypter;
import com.joshy.cart2go.backend.Product;
import com.joshy.cart2go.backend.ProductService;
import com.joshy.cart2go.backend.RetrofitClient;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuickSearch extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private List<Inventory> InventoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search);

        recyclerView = findViewById(R.id.recyclerViewQS);
        InventoryList = new ArrayList<>();
        adapter = new InventoryAdapter(this, InventoryList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                ScanOptions options = new ScanOptions();
                options.setPrompt("Scan the Code");
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                options.setCaptureActivity(CaptureAct.class);
                qrLauncher.launch(options);
            }
        });


       /* Retrofit retrofit = RetrofitClient.getClient("4a6991e578554757df7656ac3ac44b73eb9be43a54e9835fdd0444805fd346f29497c5b46a81e484f9598c915200e9fd3fc9b74a6f1e0e0798cdd0879a33439b");
        ProductService inventoryAPI = retrofit.create(ProductService.class);
        Call<List<Inventory>> call = inventoryAPI.getInventory();
        call.enqueue(new Callback<List<Inventory>>() {
            @Override
            public void onResponse(Call<List<com.joshy.cart2go.backend.Inventory>> call, Response<List<Inventory>> response) {
                if (response.isSuccessful()) {
                    List<Inventory> inventoryList = response.body();
                    if (inventoryList != null) {
                        // Update RecyclerView with search results
                        InventoryList.clear(); // Clear existing list
                        InventoryList.addAll(inventoryList); // Add search results
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch search results", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<com.joshy.cart2go.backend.Inventory>> call, Throwable t) {
                Log.e("Error", "Error fetching data", t);
            }
        });*/
    }

    ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(new ScanContract(), result->{
        if(result.getContents() != null){
            try{
                Retrofit retrofit = RetrofitClient.getClient("4a6991e578554757df7656ac3ac44b73eb9be43a54e9835fdd0444805fd346f29497c5b46a81e484f9598c915200e9fd3fc9b74a6f1e0e0798cdd0879a33439b");
                ProductService service = retrofit.create(ProductService.class);
                Call<List<Inventory>> call = service.SearchBarcodeInventory(result.getContents());
                call.enqueue(new Callback<List<Inventory>>() {
                    @Override
                    public void onResponse(Call<List<Inventory>> call, Response<List<Inventory>> response) {
                        if (response.isSuccessful()) {
                            List<Inventory> searchResults = response.body();
                            if (searchResults != null) {
                                if(!searchResults.isEmpty()){
                                    InventoryList.clear(); // Clear existing list
                                    InventoryList.addAll(searchResults); // Add updated list
                                    adapter.notifyDataSetChanged();
                                }else{
                                    try{
                                        String ScannedCode = JoshEncrypter.decode(result.getContents());
                                        Call<List<Inventory>> calling = service.SearchCrate(ScannedCode);
                                        calling.enqueue(new Callback<List<Inventory>>() {
                                            @Override
                                            public void onResponse(Call<List<Inventory>> calling, Response<List<Inventory>> responsecrate) {
                                                List<Inventory> InventoryResults = responsecrate.body();
                                                if (responsecrate.isSuccessful()) {
                                                    if(!InventoryResults.isEmpty()) {
                                                        InventoryList.clear(); // Clear existing list
                                                        InventoryList.addAll(InventoryResults); // Add updated list
                                                        adapter.notifyDataSetChanged();
                                                        Toast.makeText(getApplicationContext(), responsecrate.message(), Toast.LENGTH_LONG).show();
                                                    }else{
                                                        Toast.makeText(getApplicationContext(), "No items with that code: 0", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Cart2Go Doesn't support that Barcode/QRCode: 1", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<Inventory>> call, Throwable t) {
                                                Toast.makeText(getApplicationContext(), "Error! " + t.getMessage(), Toast.LENGTH_LONG).show();
                                                Log.e("TAG", "Error message: " + t.getMessage());
                                                t.printStackTrace();
                                            }
                                        });
                                    }catch (Throwable t){
                                        Toast.makeText(getApplicationContext(), "Cart2Go Doesn't support that Barcode/QRCode: 1", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Cart2Go Doesn't support that Barcode/QRCode: 2", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Cart2Go Doesn't support that Barcode/QRCode: 3", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Inventory>> call, Throwable t) {
                        // Handle failure
                        Toast.makeText(getApplicationContext(), "Error! " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("TAG", "Error message: " + t.getMessage());
                        t.printStackTrace();
                    }
                });

            }catch (Throwable t){
                Toast.makeText(getApplicationContext(), "Cart2Go Doesn't support that Barcode/QRCode: 1", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Cart2Go Doesn't support that Barcode/QRCode: 4", Toast.LENGTH_SHORT).show();
        }
    });
}