package com.example.malikumair.mobileattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class DoAttendance extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    DatabaseOperations dbobj;
    RadioGroup choice;
    RadioButton onehour, oneandhalf, twohour, twoandhalf, three;
    TextView status;
    Button present, absent, btRollNo;
    int thisRollno = 1, tempRollNo = 0;
    int noOfRollNumbers = 1000;
    String tableName; //for getting the bundle
    int  sno; //for getting the bundle
    String date;//for getting the bundle
    String presentOrAbsent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_attendance);

        tts = new TextToSpeech(this, this);
        dbobj = new DatabaseOperations(this);
        choice= (RadioGroup)findViewById(R.id.radioGroup);
        onehour = (RadioButton)findViewById(R.id.radioButton);
        twohour = (RadioButton)findViewById(R.id.radioButton3);
        status = (TextView)findViewById(R.id.status);
        present = (Button)findViewById(R.id.doattendance);
        absent = (Button)findViewById(R.id.overallbutton);
        btRollNo = (Button)findViewById(R.id.btRollNo);

        Intent incomingintent=this.getIntent();
        Bundle bd=incomingintent.getBundleExtra("bundleKey");
        sno=bd.getInt("serialno");
        date = bd.getString("date");
        tableName=bd.getString("tableName");


        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onehour.isChecked())
                {

                    attendance_func(1, 1);
                }
                else
                {
                    attendance_func(2, 2);
                }

                speakOut();



            }
        });
        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onehour.isChecked())
                {

                    attendance_func(0, 1);
                }
                else
                {
                    attendance_func(0, 2);
                }
                speakOut();
            }
        });

    }

    public void setRollNo(View view){

        present.setEnabled(true);
        absent.setEnabled(true);

        if(tempRollNo == 0){

            status.setText("No Roll Number has been attended yet!");
        }
        else{

            final NumberPicker picker = new NumberPicker(this);
            picker.setMinValue(1);
            picker.setMaxValue(thisRollno-1);

            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Select a Roll Number:");
            alert.setView(picker);
            alert.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    btRollNo.setText(""+picker.getValue()+"");

                    ArrayList al = dbobj.searchPreviousCf_classes(sno, tableName,""+picker.getValue());
                    String cfToday = al.get(0).toString();
                    sno--;
                    ArrayList alPrev = dbobj.searchPreviousCf_classes(sno, tableName,""+picker.getValue());
                    String cfPrev = alPrev.get(0).toString();
                    if ( ( (Integer.parseInt(cfToday))- (Integer.parseInt(cfPrev)) ) > 0){

                        presentOrAbsent = "Present";
                    }
                    else {

                        presentOrAbsent = "Absent";
                    }
                    status.setText("Roll No. '"+picker.getValue()+"' was declared "+presentOrAbsent);
                    sno = sno + 1;
                    dialogInterface.cancel();

                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog dialog = alert.create();
            dialog.show();

        }

    }
    public void attendance_func(int classes, int classesForStudent){

        tempRollNo = thisRollno; //check whether roll no has been set manually
        thisRollno = Integer.parseInt(btRollNo.getText().toString());

        presentOrAbsent = new String ();

        if(classes > 0){

            presentOrAbsent = "Present";

        }
        else {

            presentOrAbsent = "Absent";
        }
        status.setText("Roll No. '"+thisRollno+"' is declared "+presentOrAbsent);

        sno--;
        ArrayList al = dbobj.searchPreviousCf_classes(sno, tableName,""+thisRollno);
        String cf = al.get(0).toString();
        String prevClasses = al.get(1).toString();

        Cursor cr = dbobj.readRecordFromTables(tableName);
        noOfRollNumbers = cr.getColumnCount()-3;

        int totalClasses = Integer.parseInt(prevClasses);
        int cfValue = Integer.parseInt(cf);
        cfValue = cfValue + classes;
        totalClasses = totalClasses + classesForStudent ;
        sno = sno + 1;
        int r = dbobj.updateRecordTable(tableName, thisRollno, cfValue, date, sno, totalClasses);





        if(tempRollNo > thisRollno) {

            btRollNo.setText("" + tempRollNo);
            thisRollno = Integer.parseInt(btRollNo.getText().toString());
        }

        if (thisRollno == noOfRollNumbers){

            status.append(". You have completed your attendance!");
        }


        else{

            thisRollno = thisRollno + 1;
            btRollNo.setText("" + thisRollno + "");
        }

    }

    private void speakOut() {
        String text = btRollNo.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //buttonSpeak.setEnabled(true);
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
    @Override
    public void onDestroy() {
// Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
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

        Intent intent = new Intent(getApplicationContext(), checkAttendanceDoneStatus.class);
        intent.putExtra("totalStudentsCalled", tempRollNo);
        intent.putExtra("totalStudents", noOfRollNumbers);
        intent.putExtra("tableName", tableName);
        intent.putExtra("sno",sno);
        startActivity(intent);
    }



}
