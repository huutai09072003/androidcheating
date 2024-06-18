package com.trinhthanhnam.de2_c2;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class Databases extends SQLiteOpenHelper {

    public static  final String DB_NAME = "product.sqlite";
    public static  final int DB_VERSION = 1;
    public static  final String TBL_NAME = "Product";
    public static  final String COL_NAME = "ProductName";
    public static  final String COL_CODE = "ProductCode";
    public static  final String COL_PRICE = "ProductPrice";

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
        String sql = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " (" +COL_CODE + " VARCHAR(100) PRIMARY KEY , " +COL_NAME + " VARCHAR(100), " + COL_PRICE + " REAL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }


    //SELECT
    public Cursor queryData(String sql){
        SQLiteDatabase db =getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    //INSERT, UPDATE , DELETE
    public void execSql(String sql){
        SQLiteDatabase db =getWritableDatabase();
        db.execSQL(sql);
    }

    public int getNumbOfRow(){
        Cursor cursor = queryData("SELECT * FROM " + TBL_NAME);
        int numRow = cursor.getCount();
        cursor.close();
        return numRow;
    }

    public boolean deleteData(String code ){
        try{
            SQLiteDatabase db = getWritableDatabase();
            String sql ="DELETE FROM " + Databases.TBL_NAME + " WHERE " + COL_CODE + "= ?" ;
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(1, code);
            statement.executeUpdateDelete();
            return true;
        }catch (Exception e){
            return  false;
        }

    }

    public void CreateSampleData(){
        if (getNumbOfRow() == 0){
            try {
                execSql("INSERT INTO " + TBL_NAME + " VALUES('SP-123', 'Vertu Constellation',19000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('SP-234', 'Iphone5S',15000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('SP-345', 'Nokia Lumia 925',20000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('SP-456', 'SamSung Galaxy S4',16000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('SP-567', 'HTC Desire 600',17000)");
                execSql("INSERT INTO " + TBL_NAME + " VALUES('SP-678', 'HKPhone Revo LEAD',14000)");
            }catch(Exception e){
                Log.e("error: ",e.getMessage().toString());
            }

        }
    }
}
