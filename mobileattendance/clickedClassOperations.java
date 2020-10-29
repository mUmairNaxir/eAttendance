package com.example.malikumair.mobileattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class clickedClassOperations extends AppCompatActivity {

    DatabaseOperations dbobj;
    String tbname;
    Button doAttendance, overallAttendance, particularStudentsAttendance, shortageStudents;
    Bundle bd=new Bundle();
    Intent intent, intent1, intent2, intent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_class_operations);

        dbobj=new DatabaseOperations(this);
        Intent incomingintent=this.getIntent();
        tbname=incomingintent.getStringExtra("tableName");

        doAttendance = (Button)findViewById(R.id.doattendance);
        overallAttendance = (Button)findViewById(R.id.overallbutton);
        particularStudentsAttendance = (Button)findViewById(R.id.parbutton);
        shortageStudents = (Button)findViewById(R.id.shortage);

        doAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                java.util.Date dateobj= new java.util.Date();
                String today=df.format(dateobj);
                long r=dbobj.addRecordinTables(tbname,today);
                int sno=(int)r;
                // Toast.makeText(getApplicationContext(),"value of r "+sno,Toast.LENGTH_LONG).show();
                bd.putString("tableName",tbname);
                bd.putInt("serialno",sno);
                bd.putString("date",today);
                intent=new Intent(getApplicationContext(),DoAttendance.class);
                intent.putExtra("bundleKey",bd);
                startActivity(intent);

            }
        });
        overallAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent1 = new Intent(getApplicationContext(), studentsOverallAttendance.class);
                bd.putString("tableName",tbname);
                bd.putString("incomingIntent", "overallattendance");
                intent1.putExtra("bundleKey", bd);
                startActivity(intent1);

            }
        });

        particularStudentsAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent2 = new Intent(getApplicationContext(), particularStudentsAttendance.class);
                intent2.putExtra("tableName", tbname);
                startActivity(intent2);


            }
        });

        shortageStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent3 = new Intent(getApplicationContext(), studentsOverallAttendance.class);
                bd.putString("tableName",tbname);
                bd.putString("incomingIntent", "shortage");
                intent3.putExtra("bundleKey", bd);
                startActivity(intent3);
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}
