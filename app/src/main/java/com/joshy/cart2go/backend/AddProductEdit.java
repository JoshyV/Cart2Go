package com.joshy.cart2go.backend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.joshy.cart2go.R;

public class AddProductEdit extends AppCompatActivity {
    Button CancelButton,ConfirmEditButton;
    EditText Brand,Variant,Volume,Description;
    CheckBox BrandRepBox,VariantRepBox,VolumeRepBox,DescRepBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_edit);

        Bundle bundle = getIntent().getExtras();
        String BrandCU = bundle.getString("BrandCU");
        String VariantCU = bundle.getString("VariantCU");
        String VolumeCU = bundle.getString("VolumeCU");
        String DescCU = bundle.getString("DescCU");

        Brand = (EditText) findViewById(R.id.ap_brand);
        Variant = (EditText) findViewById(R.id.ap_variant);
        Volume = (EditText) findViewById(R.id.ap_volume);
        Description = (EditText) findViewById(R.id.ap_description);

        BrandRepBox = (CheckBox) findViewById(R.id.BrandRepBox);
        VariantRepBox = (CheckBox) findViewById(R.id.BrandRepBox);
        VolumeRepBox = (CheckBox) findViewById(R.id.VolumeRepBox);
        DescRepBox = (CheckBox) findViewById(R.id.DescRepBox);

        ConfirmEditButton = (Button) findViewById(R.id.ConfirmEditButton);
        CancelButton = (Button) findViewById(R.id.CancelButton);

        ConfirmEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("BrandEdit", Brand.getText().toString());
                intent.putExtra("VariantEdit", Variant.getText().toString());
                intent.putExtra("VolumeEdit", Volume.getText().toString());
                intent.putExtra("DescriptionEdit", Description.getText().toString());
                intent.putExtra("BrandCheck", BrandRepBox.isChecked());
                intent.putExtra("VariantCheck", VariantRepBox.isChecked());
                intent.putExtra("VolumeCheck", VolumeRepBox.isChecked());
                intent.putExtra("DescriptionCheck", DescRepBox.isChecked());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}