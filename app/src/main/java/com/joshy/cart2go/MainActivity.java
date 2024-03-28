package com.joshy.cart2go;

import android.os.*;
import android.view.View;
import android.view.animation.Animation;
import android.widget.*;
import androidx.appcompat.app.*;
import androidx.cardview.widget.CardView;

import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    CardView addpbutton,listpbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Add Product Start
        addpbutton = findViewById(R.id.addpbutton);
        addpbutton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent = new Intent(getApplicationContext(), Add_Product.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        //Add Product End

        //Product List
        listpbutton = findViewById(R.id.listpbutton);
        listpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), List_Product.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        //Product List End

    }
}