package com.example.malikumair.mobileattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Malik Umair on 12/14/2016.
 */

public class DatabaseOperations extends SQLiteOpenHelper {
    static String dbname =  "eattendance";
    SQLiteDatabase db;
    public DatabaseOperations(Context context){
        super(context, dbname, null, 1);
        db = context.openOrCreateDatabase(dbname, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        String query2 = "CREATE TABLE IF NOT EXISTS TABLES (TABLENAMES TEXT PRIMARY KEY,CLASS_ROLL INTEGER,SUB TEXT,SEM TEXT, BRANCH TEXT  )";
        db.execSQL(query2);
    };
    public DatabaseOperations(Context context, String tableName, String etsub, String etsem, String etbranch, int classRoll) throws IOException {
        super(context, dbname, null, 1);
        int serial=1, classes=0;
        String rollno = "";
        for(int i=1;i<=classRoll;i++)
        {
            rollno =rollno+"Rollno"+i+" INTEGER, ";

        }

//        String link =
//                "http://localhost/createTable.php";
//
//        String data = URLEncoder.encode("tablename", "UTF-8")
//                + "=" + URLEncoder.encode(tableName, "UTF-8");
//        data += "&" + URLEncoder.encode("rollno", "UTF-8")
//                + "=" + URLEncoder.encode(rollno, "UTF-8");
//        URL url = new URL(link);
//        URLConnection conn = url.openConnection();
//        conn.setDoOutput(true);
//        OutputStreamWriter wr = new OutputStreamWriter
//                (conn.getOutputStream());
//        wr.write( data );
//        wr.flush();
//        BufferedReader reader = new BufferedReader
//                (new InputStreamReader(conn.getInputStream()));
//        StringBuilder sb = new StringBuilder();
//        String line = null;
//
//        // Read Server Response
//        while((line = reader.readLine()) != null)
//        {
//            sb.append(line);
//            break;
//        }b\

        db = context.openOrCreateDatabase(dbname, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        String query = "CREATE TABLE IF NOT EXISTS "+tableName+ " (SERIAL_NO INTEGER PRIMARY KEY,DATE TEXT, "+rollno+"CLASSES INTEGER)";
        db.execSQL(query);
         addzeroindex(tableName,classRoll,serial, classes);
        String query2 = "CREATE TABLE IF NOT EXISTS TABLES (TABLENAMES TEXT PRIMARY KEY,CLASS_ROLL INTEGER,SUB TEXT,SEM TEXT, BRANCH TEXT  )";
        db.execSQL(query2);
        addRecordTables(tableName, classRoll, etsub,etsem,etbranch);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long addzeroindex(String tablename,int classroll,int i, int classes)
    {

        db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("SERIAL_NO",i);
        cv.put("CLASSES",classes);
        for(int j=1;j<=classroll;j++ )
        {
            cv.put("Rollno"+j+"",0);

        }
        long r = db.insert(tablename, null, cv);
        return  r;

    }

    public  long addRecordTables (String t, int  c, String etsub, String etsem, String etbranch){

        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TABLENAMES", t);
        cv.put("CLASS_ROLL", c);
        cv.put("SUB",etsub);
        cv.put("SEM",etsem);
        cv.put("BRANCH",etbranch);
        long r = db.insert("TABLES", null, cv);
        return r;
    }

    public  long addRecordinTables (String tablename,String date){

        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("DATE",date);
        long r = db.insert(tablename, null, cv);
        return r;
    }

    public int deleteRecord(String u){ //deleting a class
        db = this.getWritableDatabase();
        int r = db.delete("TABLES", "TABLENAMES='"+u+"'",null);
        db.execSQL("DROP TABLE IF EXISTS "+u);
        return r;

    }
     public  int deleteTodaysRecord(String tableName, int serialNo){
        db = this.getWritableDatabase();
         int r = db.delete(tableName,"SERIAL_NO ="+serialNo, null );
         return  r;
     }

    public ArrayList searchPreviousCf_classes(int sno,String table,String rollno)
    {
       db=this.getWritableDatabase();
        String qry = "SELECT Rollno"+rollno+", CLASSES FROM "+table+" WHERE SERIAL_NO="+sno+"";
        Cursor cr=db.rawQuery(qry,null);
        ArrayList al=new ArrayList();
        if(cr.moveToNext())
        {

            al.add(cr.getString(0));
            al.add(cr.getString(1));
            al.add(cr.getColumnCount());

        }

return al;

    }

    public int updateRecordTable(String tName,int rollNo,int cfValue, String date,int sno, int classes){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Rollno"+rollNo+"", cfValue);
        cv.put("SERIAL_NO",sno);
        cv.put("CLASSES",classes);
        int r = db.update(tName, cv,"DATE='"+date+"'", null);
        return r;

    }

    public int updateRecordOfTable(String tName, int  classRoll, String etsub, String etsem, String etbranch){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CLASS_ROLL", classRoll);
        cv.put("SUB",etsub);
        cv.put("SEM",etsem);
        cv.put("BRANCH",etbranch);
        int r = db.update("TABLES", cv ,"TABLENAMES='"+tName+"'" , null);
        return r;

    }

    public Cursor readRecordTables(){
        db = this.getReadableDatabase();
        String qry = "SELECT * FROM TABLES";
        Cursor cr = db.rawQuery(qry, null);


        return cr;

    }

    public Cursor readParticularRecordTables(String tableName){
        db = this.getReadableDatabase();
        String qry = "SELECT * FROM TABLES WHERE TABLENAMES='"+tableName+"'";
        Cursor cr = db.rawQuery(qry, null);


        return cr;

    }

    public Cursor readRecordFromTables(String tableName){
        db = this.getReadableDatabase();
        String qry = "SELECT * FROM "+tableName;
        Cursor cr = db.rawQuery(qry, null);


        return cr;

    }


}
