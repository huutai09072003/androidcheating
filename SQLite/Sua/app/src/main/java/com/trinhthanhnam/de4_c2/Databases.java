package com.trinhthanhnam.de4_c2;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.util.Log;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class Databases extends SQLiteOpenHelper {
    public static final String DB_NAME  = "product.sqlite";
    public static final int DB_VERSION  = 1;
    public static final String TBL_NAME  = "Product";
    public static final String COL_CODE  = "ProductCode";
    public static final String COL_NAME  = "ProductName";
    public static final String COL_PRICE  = "ProductPrice";


    public Databases(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public Databases(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public Databases(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " +TBL_NAME + "( " +COL_CODE + " VARCHAR(100) PRIMARY KEY, " + COL_NAME + " VARCHAR(100)," + COL_PRICE + " REAL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TBL_NAME);
        onCreate(db);
    }

    //SELECT
    public Cursor queryData(String sql){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql , null);
    }

    //insert,...
    public void execSql(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public boolean insertData(String code , String name , double price){
        try{
            SQLiteDatabase db = getWritableDatabase();
            String sql ="INSERT INTO " + TBL_NAME + " VALUES (?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(1, code);
            statement.bindString(2, name);
            statement.bindDouble(3, price);
            statement.executeInsert();
            return true;
        }catch (Exception e){
            return  false;
        }

    }

    public boolean codeExists(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT count(*) FROM " + TBL_NAME + " WHERE " + COL_CODE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { code });
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            db.close();
            return count > 0;
        }
        return false;
    }


    public int getNumOfRow(){
        Cursor cursor = queryData("SELECT * FROM "+ TBL_NAME);
        int row = cursor.getCount();
        cursor.close();
        return row;
    }

    public void CreateSampleData(){
        if(getNumOfRow() == 0) {
            try {
                execSql("INSERT INTO " + TBL_NAME + " VALUES('PR123', 'Thuốc trừ sâu', 20000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('PR234', 'Phân bón', 20000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('PR345', 'Thuốc diệt chuột', 20000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('PR567', 'Thuốc diệt gián', 20000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('PR678', 'Thuốc diệt cỏ dại', 20000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('PR789', 'Bình tưới', 20000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('PR890', 'Phân lân', 20000)");
            } catch (Exception e) {
                Log.e("error: ", e.getMessage().toString());
            }
        }
    }
}
