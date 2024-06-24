package com.joshy.cart2go.backend;

import android.content.Context;
import android.content.Intent;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<Product> productList;
    private final Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context; this.productList = productList;
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

        Picasso.with(context)
                .load(product.getImage())
                .into(holder.ItemPreview);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView BarcodeDetail,BrandDetail,VairantDetail,VolumeDetail;
        CardView cardView;
        Product clickedProduct;
        ImageView ItemPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            BarcodeDetail = itemView.findViewById(R.id.BarcodeDetail);
            BrandDetail = itemView.findViewById(R.id.BrandDetail);
            VairantDetail = itemView.findViewById(R.id.VariantDetail);
            VolumeDetail = itemView.findViewById(R.id.VolumeDetail);
            ItemPreview = itemView.findViewById(R.id.ItemPreview);
            cardView = itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Handle click event here
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                clickedProduct = productList.get(position);
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("barcode", clickedProduct.getBarcode());
                intent.putExtra("brand", clickedProduct.getBrand());
                intent.putExtra("variant", clickedProduct.getVariant());
                intent.putExtra("volume", clickedProduct.getVolume());
                intent.putExtra("description", clickedProduct.getDescription());
                intent.putExtra("image", clickedProduct.getImage());
                context.startActivity(intent);
            }
        }

        public void bind(Product product){
            BarcodeDetail.setText(product.getBarcode());
            BrandDetail.setText(product.getBrand());
            VairantDetail.setText(product.getVariant());
            VolumeDetail.setText(product.getVolume());
        }
    }


}
