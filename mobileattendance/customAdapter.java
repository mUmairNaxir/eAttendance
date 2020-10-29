package com.example.malikumair.mobileattendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Malik Umair on 12/20/2016.
 */

public class customAdapter extends ArrayAdapter {
    ArrayList subList;
    ArrayList semList;
    ArrayList branchList;
    Context context;
    public customAdapter(Context context, ArrayList subList, ArrayList semList, ArrayList branchList) {
        super(context, R.layout.custom, subList);
        this.subList= subList;
        this.semList = semList;
        this.branchList = branchList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cv = layoutInflater.inflate(R.layout.custom, null);
        TextView sub = (TextView)cv.findViewById(R.id.textView);
        TextView sem = (TextView)cv.findViewById(R.id.textView2);
        TextView branch = (TextView)cv.findViewById(R.id.textView3);
        sub.setText("Subject: " + subList.get(position).toString());
        sem.setText("Semester: " + semList.get(position).toString());
        branch.setText("Branch: " + branchList.get(position).toString());
        return cv;

    }
}
