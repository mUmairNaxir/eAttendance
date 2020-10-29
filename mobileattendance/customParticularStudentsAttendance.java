package com.example.malikumair.mobileattendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Malik Umair on 1/20/2017.
 */

public class customParticularStudentsAttendance extends ArrayAdapter {

    Context context;
    ArrayList snoList, dateList, statusList;

    public customParticularStudentsAttendance(Context context,ArrayList snoList, ArrayList dateList, ArrayList statusList) {
        super(context, R.layout.customparticularstudent, snoList);

        this.context = context;
        this.snoList = snoList;
        this.dateList = dateList;
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cv = layoutInflater.inflate(R.layout.customparticularstudent, null);

        Button sno = (Button)cv.findViewById(R.id.button);
        sno.setText(((int)snoList.get(position)-1)+"");
        Button date = (Button)cv.findViewById(R.id.button2);
        date.setText(dateList.get(position)+"");
        Button status = (Button)cv.findViewById(R.id.button3);
        status.setText(statusList.get(position).toString());


        return cv;
    }
}
