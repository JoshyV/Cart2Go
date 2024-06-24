package com.joshy.cart2go.backend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.joshy.cart2go.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private final List<Inventory> inventoryList;
    private final Context context;

    public InventoryAdapter(Context context, List<Inventory> inventoryList) {
        this.context = context; this.inventoryList = inventoryList;
    }

    @NonNull
    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inventory, parent, false);
        return new InventoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Inventory inventory = inventoryList.get(position);
        holder.barcodeTextView.setText("Barcode: " + inventory.getBarcode());

        Retrofit retrofit = RetrofitClient.getClient("4a6991e578554757df7656ac3ac44b73eb9be43a54e9835fdd0444805fd346f29497c5b46a81e484f9598c915200e9fd3fc9b74a6f1e0e0798cdd0879a33439b");
        ProductService productAPI = retrofit.create(ProductService.class);
        Call<List<Product>> call = productAPI.searchBarcode(inventory.getBarcode());
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    if (productList!= null &&!productList.isEmpty()) {
                        Product product = productList.get(0);
                        holder.brandTextView.setText("Brand: " + product.getBrand());
                        holder.variantTextView.setText("Variant: " + product.getVariant());
                        holder.volumeTextView.setText("Volume: " + product.getVolume());
                        holder.descriptionTextView.setText("Description: " + product.getDescription());
                    }
                } else {
                    Log.e("Error", "Error fetching product data");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("Error", "Error fetching product data", t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView barcodeTextView;
        public TextView brandTextView;
        public TextView variantTextView;
        public TextView volumeTextView;
        public TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            barcodeTextView = itemView.findViewById(R.id.barcodeTextView);
            brandTextView = itemView.findViewById(R.id.brandTextView);
            variantTextView = itemView.findViewById(R.id.variantTextView);
            volumeTextView = itemView.findViewById(R.id.volumeTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}