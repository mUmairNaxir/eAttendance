package com.example.malikumair.mobileattendance;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class addNewClass extends AppCompatActivity {

    TextView heading;
    EditText sem,branch,subject,classroll;
    Button save;
    DatabaseOperations dbobj;
    String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_class);

        dbobj = new DatabaseOperations(getApplicationContext());
        Intent incomingIntent = this.getIntent();
        tableName = incomingIntent.getStringExtra("tableName");

        heading = (TextView)findViewById(R.id.heading);
        sem = (EditText) findViewById(R.id.editText);
        branch = (EditText) findViewById(R.id.editText2);
        subject = (EditText) findViewById(R.id.editText3);
        classroll= (EditText)findViewById(R.id.editText4);
        save = (Button)findViewById(R.id.doattendance);

        if(!tableName.equals("unCreatedTable")){

            heading.setText("Edit the Class");

            Cursor cr = dbobj.readParticularRecordTables(tableName);    /*TABLENAMES TEXT PRIMARY KEY,CLASS_ROLL INTEGER,SUB TEXT,SEM TEXT, BRANCH TEXT*/
            if(cr.moveToNext()){

                classroll.setText(""+cr.getInt(1));
                subject.setText(cr.getString(2).toString());
                sem.setText(cr.getString(3).toString());
                branch.setText(cr.getString(4).toString());

            }


        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String etsem, etbranch, etsubject, etclassrollText;
                int etclassroll;

                etsem = sem.getText().toString();
                String []tsem = etsem.split(" ");
                etbranch = branch.getText().toString();
                String []tbranch = etbranch.split(" ");
                etsubject = subject.getText().toString();
                String []tsubject = etsubject.split(" ");
                etclassrollText = classroll.getText().toString();


                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                if((!etsem.equals("")&&!etbranch.equals("")&&!etsubject.equals("")&& !etclassrollText.equals(""))){   //intenting for adding a new case

                    etclassroll =Integer.parseInt(etclassrollText);
                    if(tableName.equals("unCreatedTable"))
                    {
                        String tablename = tbranch[0]+tsem[0]+tsubject[0];
                        try {
                            dbobj = new DatabaseOperations(getApplicationContext(),tablename,etsubject,etsem,etbranch,etclassroll);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        startActivity(intent);

                    }

                    else {

                        int r = dbobj.updateRecordOfTable(tableName, etclassroll, etsubject, etsem, etbranch);

                        if(r>0){

                            Toast.makeText(getApplicationContext(),"Class updated successsfully!",Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        }

                    }


                }

                else
                    Toast.makeText(getApplicationContext(),"Please fill the empty fields!",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
