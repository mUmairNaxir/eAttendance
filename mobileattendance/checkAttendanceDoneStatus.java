package com.example.malikumair.mobileattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class checkAttendanceDoneStatus extends AppCompatActivity {

    TextView status;
    Intent intent;
    DatabaseOperations dbobj;
    String tableName;
    int sno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance_done_status);

        status = (TextView)findViewById(R.id.textView);

        Intent incomingIntent = this.getIntent();
        int totalStudents = incomingIntent.getIntExtra("totalStudents", 1);
        int  totalStudentsCalled = incomingIntent.getIntExtra("totalStudentsCalled", 1);
        tableName = incomingIntent.getStringExtra("tableName");
        sno = incomingIntent.getIntExtra("sno", 1);

         if((totalStudents - totalStudentsCalled) != 0){

             status.setText("You haven't completed your Attendance yet! Go back to Attendance or Cancel it here");

         }
        else{

             intent = new Intent(getApplicationContext(),clickedClassOperations.class);
             intent.putExtra("tableName",tableName);
             startActivity(intent);
         }

    }

    public  void cancelAttendance(View view){

        dbobj = new DatabaseOperations(getApplicationContext());
        int r = dbobj.deleteTodaysRecord(tableName, sno);
         if(r>0){

             Toast.makeText(getApplicationContext(),"Attendance cancelled successfully", Toast.LENGTH_LONG);

             intent = new Intent(getApplicationContext(),clickedClassOperations.class);
             intent.putExtra("tableName",tableName);
             startActivity(intent);

         }

    }
}
