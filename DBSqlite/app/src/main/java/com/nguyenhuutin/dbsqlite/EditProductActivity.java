package com.nguyenhuutin.dbsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProductActivity extends AppCompatActivity {
    EditText edtPName, edtPPrice;
    Button btnConfirm,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        addViews();
        addEvents();
    }

    private void addViews() {
        edtPName = findViewById(R.id.edtPname);
        edtPPrice = findViewById(R.id.edtPprice);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        edtPName.setText(MainActivity.selectedProduct.getProductName());
        edtPPrice.setText(MainActivity.selectedProduct.getProductPrice() +"");
    }

    private void addEvents() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues  values = new ContentValues();
                values.put("ProductName",edtPName.getText().toString());
                values.put("ProductPrice",Double.parseDouble(edtPPrice.getText().toString()));

                int flag = MainActivity.database.update("Product",values,"ProductId=?", new String[]{MainActivity.selectedProduct.getProductID()+ ""});
                if(flag>0){
                    Toast.makeText(EditProductActivity.this,"Update product successful",Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(EditProductActivity.this,"Update product fail",Toast.LENGTH_LONG).show();
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