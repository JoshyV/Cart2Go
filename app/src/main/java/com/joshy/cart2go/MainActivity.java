package com.joshy.cart2go;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.*;
import androidx.cardview.widget.CardView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    CardView addpbutton, listpbutton, adminpbutton,settingspbutton,addInventorybutton,inventorybutton,Generate_Crate;
    TextView itemscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences userdata = getSharedPreferences("userdata", MODE_PRIVATE);
        int countitem = 1;
        int Admincheck = userdata.getInt("AdminCheck", 0);
        int AddPCheck = userdata.getInt("AddProductCheck", 0);
        int ProdListCheck = userdata.getInt("ProductListCheck", 0);
        int AddInvCheck = userdata.getInt("AddInventoryCheck", 0);
        int InvCheck = userdata.getInt("InventoryCheck", 0);
        int GenCheck = userdata.getInt("GenerateCheck", 0);
        addpbutton = findViewById(R.id.addpbutton);
        listpbutton = findViewById(R.id.listpbutton);
        adminpbutton = findViewById(R.id.adminpbutton);
        settingspbutton = findViewById(R.id.settingspbutton);
        addInventorybutton = findViewById(R.id.addInventorybutton);
        inventorybutton = findViewById(R.id.inventorybutton);
        Generate_Crate = findViewById(R.id.Generate_Crate);
        itemscount = findViewById(R.id.itemscount);


        adminpbutton.setVisibility(Admincheck == 0 ? View.GONE : View.VISIBLE);
        addpbutton.setVisibility(AddPCheck == 0 ? View.GONE : View.VISIBLE);
        listpbutton.setVisibility(ProdListCheck == 0 ? View.GONE : View.VISIBLE);
        addInventorybutton.setVisibility(AddInvCheck == 0 ? View.GONE : View.VISIBLE);
        inventorybutton.setVisibility(InvCheck == 0 ? View.GONE : View.VISIBLE);
        Generate_Crate.setVisibility(InvCheck == 0 ? View.GONE : View.VISIBLE);

        countitem += (Admincheck == 1) ? 1 : 0;
        countitem += (AddPCheck == 1) ? 1 : 0;
        countitem += (ProdListCheck == 1) ? 1 : 0;
        countitem += (AddInvCheck == 1) ? 1 : 0;
        countitem += (InvCheck == 1) ? 1 : 0;
        countitem += (GenCheck == 1) ? 1 : 0;


        itemscount.setText(String.valueOf(countitem) + " Items");

        // Add Product Start
        addpbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Add_Product.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        // Add Product End

        // Product List
        listpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), List_Product.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        // Product List End

        // Admin Panel
        adminpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Admin_Panel.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        // Admin Panel End

        // Settings Panel
        settingspbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings_Panel.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        // Settings End

        // Inventory Panel
        addInventorybutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Add_Inventory.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        // Inventory End

        // Settings Panel
        Generate_Crate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Generate_Crate.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        // Settings End

        // Update layout params based on visibility
        updateLayoutParams(adminpbutton, Admincheck == 0);
        updateLayoutParams(addpbutton, AddPCheck == 0);
        updateLayoutParams(listpbutton, ProdListCheck == 0);
        updateLayoutParams(addInventorybutton, AddInvCheck == 0);
        updateLayoutParams(inventorybutton, InvCheck == 0);
        updateLayoutParams(Generate_Crate, GenCheck == 0);
        updateLayoutParams(settingspbutton, false);

    }

    private void updateLayoutParams(CardView cardView, boolean isGone) {
        GridLayout.LayoutParams params = (GridLayout.LayoutParams) cardView.getLayoutParams();
        if (isGone) {
            params.width = 0;
            params.height = 0;
            params.setMargins(0, 0, 0, 0);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 0);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 0);
        } else {
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(35, 12, 40, 12);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
        }
        cardView.setLayoutParams(params);
    }
}
