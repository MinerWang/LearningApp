package com.example.justloginregistertest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBOpenHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;


    public DBOpenHelper(Context context){
        super(context,"wgy_db",null,1);
        db = getReadableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT," + "truename TEXT,"+ "password TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    public void add(String name,String truename,String password){
        db.execSQL("INSERT INTO user (name,truename,password) VALUES(?,?,?)",new Object[]{name,truename,password});
    }
    public void delete(String name,String truename,String password){

        db.execSQL("DELETE FROM user WHERE name = AND truename = AND password ="+name+truename+password);
    }
    public void updata(String password){
        db.execSQL("UPDATE user SET password = ?",new Object[]{password});
    }


    public ArrayList<User> getAllData(String name1){

        ArrayList<User> list = new ArrayList();
        Cursor cursor = db.query("user",null,"truename =?",new String[]{name1},null,null,"name DESC");
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String trueName = cursor.getString(cursor.getColumnIndex("truename"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            list.add(new User(name,trueName,password));
        }
        return list;
    }
    public ArrayList<User> getAllData(){

        ArrayList<User> list = new ArrayList();
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String trueName = cursor.getString(cursor.getColumnIndex("truename"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            list.add(new User(name,trueName,password));
        }
        return list;
    }
}
