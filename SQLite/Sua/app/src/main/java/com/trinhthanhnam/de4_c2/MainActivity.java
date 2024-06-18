package com.trinhthanhnam.de4_c2;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trinhthanhnam.de4_c2.adapter.ProductAdapter;
import com.trinhthanhnam.de4_c2.databinding.ActivityMainBinding;
import com.trinhthanhnam.de4_c2.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Databases db;
    ProductAdapter adapter;
    ArrayList<Product> listProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preParedb();
        loadData();
        addEvent();
    }

    private void addEvent() {

        binding.lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                openAddDialog();
                return true;
            }
        });
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_layout);

        EditText editCode = dialog.findViewById(R.id.edtProductCode);
        EditText editName = dialog.findViewById(R.id.edtProductName);
        EditText editPrice = dialog.findViewById(R.id.edtProductPrice);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String code = editCode.getText().toString().trim();
                    String name = editName.getText().toString().trim();
                    double price  = Double.parseDouble(editPrice.getText().toString().trim());

                    if (code.isEmpty() || name.isEmpty() || price<0){
                        Toast.makeText(MainActivity.this, "Please fill in all information.", Toast.LENGTH_SHORT).show();
                    } else {
                        if(!db.codeExists(code)){
                            db.insertData(code, name ,price);
                            loadData();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this, "ProductCode already exists.", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(MainActivity.this, "Add success!", Toast.LENGTH_SHORT).show();
                    }
                }catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button btnBack = dialog.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void loadData() {
        adapter = new ProductAdapter(MainActivity.this,R.layout.item_list, getDatafromDb());
        binding.lvProduct.setAdapter(adapter);
    }

    private List<Product> getDatafromDb() {
        listProduct = new ArrayList<>();
        Cursor cursor = db.queryData("SELECT * FROM " + Databases.TBL_NAME);
        while (cursor.moveToNext()){
            listProduct.add(new Product(cursor.getString(0), cursor.getString(1), cursor.getDouble(2)));
        }
        cursor.close();
        return  listProduct;
    }

    private void preParedb() {
        db = new Databases(this);
        db.CreateSampleData();
    }
}