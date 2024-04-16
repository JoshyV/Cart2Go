package com.joshy.cart2go;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ProductService {
    @GET("/products")
    Call<List<Product>> getProducts();

    @POST("/product")
    Call<Void> addProduct(@Body Product product);

    @PUT("/products/{id}")
    Call<Void> updateProduct(@Path("id") String id, @Body Product product);

    @DELETE("/products/{id}")
    Call<Void> deleteProduct(@Path("id") String id);

    @GET("/getUpdates")
    Call<List<Product>> getUpdates();

    @GET("/search")
    Call<List<Product>> searchProducts(@Query("brand") String brand);
}
