package com.trinhthanhnam.de2_c2;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trinhthanhnam.de2_c2.adapter.ProductAdapter;
import com.trinhthanhnam.de2_c2.databinding.ActivityMainBinding;
import com.trinhthanhnam.de2_c2.model.Product;

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
        addEvent();
        preParedb();
        loadData();
        addEvent();
    }

    private void loadData() {
        adapter = new ProductAdapter(MainActivity.this,R.layout.item_list, getDatafromDb());
        binding.lvProduct.setAdapter(adapter);
    }

    private void preParedb() {
        db = new Databases(this);
        db.CreateSampleData();
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

    private void addEvent() {
        binding.lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                opendetailProduct(listProduct.get(position));
                return true;
            }
        });
    }

    private void opendetailProduct(Product p) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_detail);
        TextView editCode =  dialog.findViewById(R.id.edtEditProductCode);
        editCode.setText((p.getProductCode()));
        TextView editName =  dialog.findViewById(R.id.edtEditProductName);
        editName.setText(p.getProductName());
        TextView editPrice =  dialog.findViewById(R.id.edtEditProductPrice);
        editPrice.setText(Double.toString(p.getProductPrice()));
        Button btnSave = dialog.findViewById(R.id.btnDelete);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteData(p.getProductCode());
                loadData();
                Toast.makeText(MainActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();
    }
}