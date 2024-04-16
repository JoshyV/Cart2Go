package com.joshy.cart2go;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class List_Product extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private SearchView searchView;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Start polling for updates
        recyclerView.postDelayed(pollingRunnable, 1000);

        // Set up search functionality
        setupSearchView();
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                recyclerView.removeCallbacks(pollingRunnable);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Start polling for updates when search bar becomes empty
                    recyclerView.removeCallbacks(pollingRunnable); // Remove any pending callbacks
                    recyclerView.postDelayed(pollingRunnable, 1000); // Start polling
                }else{
                    searchProducts(newText);
                    recyclerView.removeCallbacks(pollingRunnable);
                }
                return false;
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

    private void searchProducts(String brand) {
        // Retrofit setup...
        // Create Retrofit instance with appropriate JWT token
        Retrofit retrofit = RetrofitClient.getClient();
        ProductService service = retrofit.create(ProductService.class);

        // Make network request to search products by brand name
        Call<List<Product>> call = service.searchProducts(brand);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> searchResults = response.body();
                    if (searchResults != null) {
                        // Update RecyclerView with search results
                        productList.clear(); // Clear existing list
                        productList.addAll(searchResults); // Add search results
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(getApplicationContext(), "Failed to fetch search results", Toast.LENGTH_SHORT).show();
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
    private void pollForUpdates() {
        Retrofit retrofit = RetrofitClient.getClient();
        ProductService service = retrofit.create(ProductService.class);
        Call<List<Product>> call = service.getUpdates();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> updates = response.body();
                    if (updates != null) {
                        productList.clear(); // Clear existing list
                        productList.addAll(updates); // Add updated list
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error! " + t, Toast.LENGTH_LONG).show();
                Log.e("TAG", "Error message: " + t.getMessage()); // This logs an error message with the exception's message
                t.printStackTrace();
            }
        });
    }
}

