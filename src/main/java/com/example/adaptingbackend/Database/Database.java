package com.example.adaptingbackend.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.adaptingbackend.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//update it where it creates a cart while started up
//SQLiteAssetHelper
public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "cart.db";
    public static final String ID = "ID";
    public static final String ProductName = "ProductName";
    public static final String Quantity = "Quantity";
    public static final String Price = "Price";
    private static final int DB_VER = 2;
    int total = 0;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID", "ProductName", "Quantity", "Price","Product_ID"};

        String sqlTable = "OrderDetail";
        qb.setTables(sqlTable);

        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Product_ID"))));

            } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order Order) {
        String query;
        //I will have to check if the prodcut Id is already in the cart if it is up the amount by 1
        SQLiteDatabase db = getReadableDatabase();

        String Query = "Select * from OrderDetail where ProductName = '" + Order.getProductName() + "';";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            query = String.format("INSERT INTO OrderDetail(ProductName,Quantity,Price,Product_ID) VALUES ('%s','%s','%s','%s');"
                    , Order.getProductName()
                    , Order.getQuantity()
                    , Order.getPrice(),
                    Order.getProduct_id());
            db.execSQL(query);

        } else {
            query = "UPDATE OrderDetail SET Quantity = Quantity + 1 WHERE ProductName LIKE '" + Order.getProductName() + "'";
            db.execSQL(query);
        }

        cursor.close();


    }

    public void addOneToProduct(String product_name) {
        String query;
        //I will have to check if the prodcut Id is already in the cart if it is up the amount by 1
        SQLiteDatabase db = getReadableDatabase();
        String[] args = {product_name};
        Cursor cursor = db.query("OrderDetail", new String[]{"Quantity"},
                "ProductName LIKE " + "'" + product_name + "'", null, null, null, null);
        if (cursor.getColumnCount() > 0) {
//            Toast.makeText(this,Order.get_name(),Toast.LENGTH_SHORT).show();
            query = "UPDATE OrderDetail SET Quantity = Quantity + 1 WHERE ProductName LIKE '" + product_name + "'";
            db.execSQL(query);

        }
    }


    public void minusOneToProduct(String product_name) {
        String query;
        //I will have to check if the prodcut Id is already in the cart if it is up the amount by 1
        SQLiteDatabase db = getReadableDatabase();
        String[] args = {product_name};
        Cursor cursor = db.query("OrderDetail", new String[]{"Quantity"},
                "ProductName LIKE " + "'" + product_name + "'", null, null, null, null);
        if (cursor.getColumnCount() > 0) {
//            Toast.makeText(this,Order.get_name(),Toast.LENGTH_SHORT).show();
            query = "UPDATE OrderDetail SET Quantity = Quantity - 1 WHERE ProductName LIKE '" + product_name + "'";
            db.execSQL(query);

        }
        String Query = "Select * from OrderDetail where ProductName = '" + product_name + "' AND Quantity == 0;";
        Cursor cursor_1 = db.rawQuery(Query, null);
        System.out.println(cursor_1.getCount());
        if (cursor_1.getColumnCount() > 0) {
            query = "DELETE FROM OrderDetail WHERE ProductName LIKE '" + product_name + "'";
            db.execSQL(query);
        }
    }

    public void deleteProduct(String product_name) {
        String query;
        //I will have to check if the prodcut Id is already in the cart if it is up the amount by 1
        SQLiteDatabase db = getReadableDatabase();
        String[] args = {product_name};
        Cursor cursor = db.query("OrderDetail", new String[]{"Quantity"},
                "ProductName LIKE " + "'" + product_name + "'", null, null, null, null);
        if (cursor.getColumnCount() > 0) {
//            Toast.makeText(this,Order.get_name(),Toast.LENGTH_SHORT).show();
            query = "DELETE FROM OrderDetail WHERE ProductName LIKE '" + product_name + "'";
            db.execSQL(query);

        }
    }

    public String getPrice() {
        String query;
        //I will have to check if the prodcut Id is already in the cart if it is up the amount by 1
        List<Order> orders = getCarts();
        for (Order item : orders) {
            double price = Double.parseDouble(item.getPrice());
            int quantity = Integer.parseInt(item.getQuantity());
            total += price * quantity;
        }

        Locale locale = new Locale("en", "IE");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        return String.valueOf(total);
//        txtTotalPrice.setText(fmt.format(total));
    }

    public void clearCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }
}