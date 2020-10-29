package com.example.malikumair.mobileattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.ArrayList;

public class particularStudentsAttendance extends AppCompatActivity {

    Button bt;
    DatabaseOperations dbobj;
    String tableName;
    ListView listView;
    ArrayList serialNoList, dateList, statusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_students_attendance);

        listView = (ListView)findViewById(R.id.listView);
        bt = (Button)findViewById(R.id.heading);


        Intent incomingIntent = this.getIntent();
        tableName = incomingIntent.getStringExtra("tableName");

        dbobj = new DatabaseOperations(getApplicationContext());


    }

    public void selectRollNo(final View view){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Select a Roll No:");
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(1);
        Cursor cr = dbobj.readRecordFromTables(tableName);
        numberPicker.setMaxValue(cr.getColumnCount()-3);
        alert.setView(numberPicker);

        alert.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                updateStudent(numberPicker.getValue());
                bt.setText("Showing results for Roll No "+numberPicker.getValue());
                dialogInterface.cancel();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                bt.setText("Select another Roll Number");
            }
        });

        AlertDialog dilaog = alert.create();
        dilaog.show();

    }

     public void updateStudent(int rollNo){

         serialNoList = new ArrayList();
         dateList = new ArrayList();
         statusList = new ArrayList();

         int prevDaysAttendance, OnDateAttendance = 0;

         String status;

         Cursor cr = dbobj.readRecordFromTables(tableName);

         int rollNoIndex = cr.getColumnIndex("Rollno"+rollNo);
         cr.moveToFirst();
         while (cr.moveToNext()){

             serialNoList.add(cr.getInt(0));
             dateList.add(cr.getString(1));

             prevDaysAttendance = OnDateAttendance;
             OnDateAttendance = cr.getInt(rollNoIndex);

             if((OnDateAttendance-prevDaysAttendance) == 0){

                 status = "Absent";

             }else{

                 status = "Present";
             }

             statusList.add(status);

         }

         customParticularStudentsAttendance cd = new customParticularStudentsAttendance( getApplicationContext(), serialNoList, dateList, statusList);
         listView.setAdapter(cd);


     }


}
