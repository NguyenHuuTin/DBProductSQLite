package com.nguyenhuutin.dbsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity {
    EditText edtproductname, edtprice;
    Button btnOk,btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        addViews();
        addEvents();
    }

    private void addViews() {
        edtproductname = findViewById(R.id.edtproductname);
        edtprice = findViewById(R.id.edtprice);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

    }

    private void addEvents() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pName = edtproductname.getText().toString();
                Double pPrice = Double.parseDouble((edtprice.getText().toString()));

                ContentValues values = new ContentValues();
                values.put("ProductName",pName);
                values.put("ProductPrice",pPrice);

                long flag = MainActivity.database.insert("Product",null,values);
                if(flag>0){
                    Toast.makeText(AddProductActivity.this,"Add Product Successful!",Toast.LENGTH_LONG).show();
                }
                else  Toast.makeText(AddProductActivity.this, "Add Product Fail!",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}