package com.nguyenhuutin.dbsqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nguyenhuutin.model.product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "product_db.db";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    ListView lvProduct;
    ArrayAdapter<product> adapter;

    public  static product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CopyDatabase();
        addView();
        addEvents();
//        loadDataFromDB();
    }

    private void addEvents() {
        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProduct = adapter.getItem(position);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnaddproduct){
           Intent intent = new Intent(MainActivity.this,AddProductActivity.class);
           startActivity(intent);
        }
        else if (item.getItemId()==R.id.mninfo);
        return super.onOptionsItemSelected(item);
    }

    private void addView() {
        lvProduct = findViewById(R.id.lvproduct);
        adapter = new ArrayAdapter<product>(MainActivity.this, android.R.layout.simple_list_item_1);
        lvProduct.setAdapter(adapter);
        registerForContextMenu(lvProduct);

    }

    private void CopyDatabase() {
        try {
            File bdFile = getDatabasePath(DATABASE_NAME);
            if (!bdFile.exists()){
                if(CopyDBFromAssets()){
                    Toast.makeText(MainActivity.this,"Copy Database Successful!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"Copy Database fail!", Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            Log.e("Error",e.toString());
        }

    }

    private boolean CopyDBFromAssets() {
        String dbPath = getApplicationInfo().dataDir + DB_PATH_SUFFIX +DATABASE_NAME;
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024]; int length;
            while ((length=inputStream.read(buffer))>0){
                outputStream.write(buffer,0,length);

            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    private void loadDataFromDB() {
        database = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
//        Cursor cursor = database.rawQuery(
//                "select * from Product where ProductId = ? or ProductId =?",
//                new String[] {"2","3"});
        Cursor cursor = database.query("Product",null,null,null, null, null,null);

        adapter.clear();
        int ProductId;
        String ProductName;
        double ProductPrice;
        while (cursor.moveToNext()){
            ProductId = cursor.getInt(0);
            ProductName = cursor.getString(1);
            ProductPrice = cursor.getDouble(2);
            product p = new product(ProductId,ProductName,ProductPrice);
            adapter.add(p);
        }
        cursor.close();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnedit){
            if (selectedProduct!=null){
                Intent intent = new Intent(MainActivity.this,EditProductActivity.class);
                startActivity(intent);
            }

        }else if(item.getItemId()==R.id.mndelete){
            if(selectedProduct!=null){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setMessage("Are you sure you want to delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int flag = database.delete("Product","ProductId=?",new String[] {MainActivity.selectedProduct.getProductID()+ ""});
                        if(flag >0){
                            Toast.makeText(MainActivity.this,"Delete product successful",Toast.LENGTH_LONG).show();
                            loadDataFromDB();
                        }
                        else Toast.makeText(MainActivity.this,"Delete product fail",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDB();
    }
}