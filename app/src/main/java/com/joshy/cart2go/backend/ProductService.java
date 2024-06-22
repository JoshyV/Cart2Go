package com.joshy.cart2go.backend;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProductService {
    @GET("/products")
    Call<List<Product>> getProducts();

    @Multipart
    @POST("/product")
    Call<Void> addProduct(@Part("product")RequestBody product, @Part MultipartBody.Part image);

    @PUT("/products/{id}")
    Call<Void> updateProduct(@Path("id") String id, @Body Product product);

    @DELETE("/products/{id}")
    Call<Void> deleteProduct(@Path("id") String id);

    @GET("/getUpdates")
    Call<List<Product>> getUpdates();

    @GET("/searchbrand")
    Call<List<Product>> searchProducts(@Query("brand") String brand);

    @GET("/searchbarcode")
    Call<List<Product>> searchBarcode(@Query("barcode") String barcode);

    @POST("/login")
    Call<LoginRequest.Response> login(@Body LoginRequest.Request request);
}
