package com.example.malikumair.mobileattendance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.ArrayList;

public class studentsOverallAttendance extends AppCompatActivity {

    DatabaseOperations dbobj;
    ArrayList attendanceList = new ArrayList();
    ArrayList rollNoList = new ArrayList();
    ListView listView;
    Button shortage;
    float shortagePercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_overall_attendance);

        listView  = (ListView)findViewById(R.id.soatt);
        shortage = (Button)findViewById(R.id.shortage);

        Intent intent = this.getIntent();
        Bundle bd = intent.getBundleExtra("bundleKey");
        final String tableName = bd.getString("tableName");
        String incomingIntent = bd.getString("incomingIntent");
         if(incomingIntent.equals("shortage")){

             shortage.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     AlertDialog.Builder alert = new AlertDialog.Builder(getApplication());
                     alert.setTitle("Select shortage percentage: ");
                     final NumberPicker numberPicker = new NumberPicker(getApplicationContext());
                     numberPicker.setMinValue(1);
                     Cursor cr = dbobj.readRecordFromTables(tableName);
                     numberPicker.setMaxValue(100);
                     alert.setView(numberPicker);

                     alert.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {

                             shortage.setText("Selected shortage percent: "+numberPicker.getValue());
                             dialogInterface.cancel();
                         }
                     });

                     alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             dialogInterface.cancel();
                         }
                     });

                     AlertDialog dilaog = alert.create();
                     dilaog.show();
                 }
             });
         }
        else{

             shortage.setClickable(false);
         }

        dbobj = new DatabaseOperations(this);
        Cursor cr = dbobj.readRecordFromTables(tableName);
        int noOfColumns =  cr.getColumnCount();
        String date = new String();
        int TotalNoOfClasses = 0;
        if(cr.moveToLast()){

            date = cr.getString(1);
            for(int i=2; i< (noOfColumns-1); i++ ){
                rollNoList.add(cr.getColumnName(i));
                attendanceList.add(cr.getInt(i));
            }
            TotalNoOfClasses = cr.getInt(noOfColumns-1);
        }
        customOverallAttendanceAdapter sd = new customOverallAttendanceAdapter(this,rollNoList, attendanceList, TotalNoOfClasses, shortagePercent );
        listView.setAdapter(sd);



    }
}

