package com.joshy.cart2go;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.joshy.cart2go.backend.LoginRequest;
import com.joshy.cart2go.backend.ProductService;
import com.joshy.cart2go.backend.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckbox;
    private ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean rememberMe = prefs.getBoolean("rememberMe", false);
        usernameEditText = findViewById(R.id.usernamel);
        passwordEditText = findViewById(R.id.passwordl);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);
        productService = RetrofitClient.getClient("4a6991e578554757df7656ac3ac44b73eb9be43a54e9835fdd0444805fd346f29497c5b46a81e484f9598c915200e9fd3fc9b74a6f1e0e0798cdd0879a33439b").create(ProductService.class);
        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Make network request to login endpoint
                Call<LoginRequest.Response> call = productService.login(new LoginRequest.Request(username, password));
                call.enqueue(new Callback<LoginRequest.Response>() {
                    @Override
                    public void onResponse(Call<LoginRequest.Response> call, Response<LoginRequest.Response> response) {
                        if (response.isSuccessful()) {
                            LoginRequest.Response loginResponse = response.body();
                            String message = loginResponse.getMessage();
                            if (message.equals("Login successful")) {
                                LoginRequest.Response.User user = loginResponse.getUser();

                                // Storing user data in SharedPreferences
                                SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                                editor.putInt("AdminCheck", user.getAdmin());
                                editor.putInt("AddProductCheck", user.getAddproduct());
                                editor.putInt("ProductListCheck", user.getProductlist());
                                editor.putInt("AddInventoryCheck", user.getaddInventory());
                                editor.putInt("InventoryCheck", user.getInventory());
                                editor.apply();
                              //usernameEditText.setText(String.valueOf(user.getAdmin()+" "+user.getAddproduct()+" "+user.getProductlist()));
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else if(message.equals("Wrong password")){
                                SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                                editor.putBoolean("rememberMe", false);
                                editor.apply();
                                Toast.makeText(Login.this, "Wrong password1", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                                editor.putBoolean("rememberMe", false);
                                editor.apply();
                                Toast.makeText(Login.this, "User doesn't exist!", Toast.LENGTH_SHORT).show();
                            }

                            if (rememberMeCheckbox.isChecked()) {
                                SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.putBoolean("rememberMe", true);
                                editor.apply();
                            }
                        } else {
                            SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                            editor.putBoolean("rememberMe", false);
                            editor.apply();
                            Toast.makeText(Login.this, "Wrong password or user doesn't exist!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginRequest.Response> call, Throwable t) {
                        Log.e("TAG", "TAG L1.2: " + t.getMessage());
                        Toast.makeText(Login.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (rememberMe) {
            String savedUsername = prefs.getString("username", "");
            String savedPassword = prefs.getString("password", "");
            checkSavedCredentials(savedUsername, savedPassword);
        }
    }

    private void checkSavedCredentials(final String username, final String password) {
        Call<LoginRequest.Response> call = productService.login(new LoginRequest.Request(username, password));
        call.enqueue(new Callback<LoginRequest.Response>() {
            @Override
            public void onResponse(Call<LoginRequest.Response> call, Response<LoginRequest.Response> response) {
                if (response.isSuccessful()) {
                    LoginRequest.Response loginResponse = response.body();
                    String message = loginResponse.getMessage();
                    Log.e("TAG", "Success message: " + response);
                    if (message.equals("Login successful")) {
                        LoginRequest.Response.User user = loginResponse.getUser();
                        // Storing user data in SharedPreferences
                        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                        editor.putInt("AdminCheck", user.getAdmin());
                        editor.putInt("AddProductCheck", user.getAddproduct());
                        editor.putInt("ProductListCheck", user.getProductlist());
                        editor.putInt("AddInventoryCheck", user.getaddInventory());
                        editor.putInt("InventoryCheck", user.getInventory());
                        editor.apply();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                        editor.putBoolean("rememberMe", false);
                        editor.apply();
                        Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                    editor.putBoolean("rememberMe", false);
                    editor.apply();
                    Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginRequest.Response> call, Throwable t) {
                // Network error
                Toast.makeText(Login.this, "Network error2", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
