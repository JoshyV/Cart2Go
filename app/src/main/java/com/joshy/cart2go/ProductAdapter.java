package com.joshy.cart2go;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Update ViewHolder class in ProductAdapter.java
    // Update ViewHolder class in ProductAdapter.java
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textBrandVariant;
        CardView cardView;
        Product clickedProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            textBrandVariant = itemView.findViewById(R.id.text_brand_variant);
            cardView = itemView.findViewById(R.id.card_view);

            // Set click listener for the CardView
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Handle click event here
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                clickedProduct = productList.get(position);
                // Open ProductDetailActivity or ProductDetailDialogFragment and pass product details
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("barcode", clickedProduct.getBarcode());
                intent.putExtra("brand", clickedProduct.getBrand());
                intent.putExtra("variant", clickedProduct.getVariant());
                intent.putExtra("volume", clickedProduct.getVolume());
                intent.putExtra("description", clickedProduct.getDescription());
                context.startActivity(intent);
            }
        }

        public void bind(Product product) {
            // Bind product details to TextViews inside CardView
            textBrandVariant.setText(product.getBrand() + " - " + product.getVariant());
            // Bind more details if needed
        }
    }


}
