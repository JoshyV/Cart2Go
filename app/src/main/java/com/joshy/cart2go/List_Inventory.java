package com.joshy.cart2go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.joshy.cart2go.backend.Inventory;
import com.joshy.cart2go.backend.InventoryAdapter;
import com.joshy.cart2go.backend.Product;
import com.joshy.cart2go.backend.ProductAdapter;
import com.joshy.cart2go.backend.ProductService;
import com.joshy.cart2go.backend.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class List_Inventory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private List<Inventory> InventoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_inventory);

        recyclerView = findViewById(R.id.recyclerViewINV);
        InventoryList = new ArrayList<>();
        adapter = new InventoryAdapter(this, InventoryList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.postDelayed(pollingRunnable, 1000);

        findViewById(R.id.backButtonINV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        Retrofit retrofit = RetrofitClient.getClient("4a6991e578554757df7656ac3ac44b73eb9be43a54e9835fdd0444805fd346f29497c5b46a81e484f9598c915200e9fd3fc9b74a6f1e0e0798cdd0879a33439b");
        ProductService inventoryAPI = retrofit.create(ProductService.class);
        Call<List<com.joshy.cart2go.backend.Inventory>> call = inventoryAPI.getInventory();
        call.enqueue(new Callback<List<com.joshy.cart2go.backend.Inventory>>() {
            @Override
            public void onResponse(Call<List<com.joshy.cart2go.backend.Inventory>> call, Response<List<com.joshy.cart2go.backend.Inventory>> response) {
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
        });
    }
    private Runnable pollingRunnable = new Runnable() {
        @Override
        public void run() {
            pollForUpdates();
            recyclerView.postDelayed(this, 5000); // Poll every 5 seconds (adjust as needed)
        }
    };
    private void pollForUpdates() {
        Retrofit retrofit = RetrofitClient.getClient("4a6991e578554757df7656ac3ac44b73eb9be43a54e9835fdd0444805fd346f29497c5b46a81e484f9598c915200e9fd3fc9b74a6f1e0e0798cdd0879a33439b");
        ProductService service = retrofit.create(ProductService.class);
        Call<List<Inventory>> call = service.getInventory();
        call.enqueue(new Callback<List<Inventory>>() {
            @Override
            public void onResponse(Call<List<Inventory>> call, Response<List<Inventory>> response) {
                if (response.isSuccessful()) {
                    List<Inventory> Inventoryresponse = response.body();
                    if (Inventoryresponse != null) {
                        InventoryList.clear(); // Clear existing list
                        InventoryList.addAll(Inventoryresponse); // Add updated list
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Inventory>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error! " + t, Toast.LENGTH_LONG).show();
                Log.e("TAG", "Error message: " + t.getMessage()); // This logs an error message with the exception's message
                t.printStackTrace();
            }
        });
    }
}