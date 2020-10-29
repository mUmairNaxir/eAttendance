package com.example.malikumair.mobileattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Malik Umair on 1/11/2017.
 */

public class customOverallAttendanceAdapter extends ArrayAdapter{

    Context context;
    ArrayList rollNoList, attendanceList;
    int noOfClasses;
    float shortagePercent;


    public customOverallAttendanceAdapter(Context context , ArrayList rollNoList, ArrayList attendanceList, int noOfClasses,float shortagePercent) {
        super(context, R.layout.customoverallattendance, rollNoList);

        this.context = context;
        this.rollNoList = rollNoList;
        this.attendanceList = attendanceList;
        this.noOfClasses = noOfClasses;
        this.shortagePercent = shortagePercent;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cv = layoutInflater.inflate(R.layout.customoverallattendance, null);

        Button rollNo = (Button)cv.findViewById(R.id.doattendance);
        rollNo.setText(rollNoList.get(position).toString());
        Button attendance = (Button)cv.findViewById(R.id.overallbutton);
        attendance.setText(attendanceList.get(position).toString()+"/"+noOfClasses);
        Button percentage = (Button)cv.findViewById(R.id.parbutton);

        float percent = (((int)attendanceList.get(position)) *100) / noOfClasses;
        percentage.setText(""+percent);
        return cv;

    }
}
