package com.fyp.shaun.medistorageapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp.shaun.medistorageapp.models.PatientModel;

import java.util.List;

/**
 * This class is the CustomerAdapter for the ListView on the REST Page
 */
class CustomAdapter extends ArrayAdapter<PatientModel> {
    private static final String TAG = "shauns.tag";

    CustomAdapter(Context context, List<PatientModel> patientModelList) {
        super(context, R.layout.custom_row, patientModelList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.custom_row, parent, false);

        PatientModel patient = getItem(position);

        ImageView iv_patient_photo = (ImageView) customView.findViewById(R.id.iv_patient_photo);
        TextView tv_patient_name = (TextView) customView.findViewById(R.id.tv_patient_name);
        TextView tv_ppsn = (TextView) customView.findViewById(R.id.tv_ppsn);
        TextView tv_condition = (TextView) customView.findViewById(R.id.tv_condition);
        TextView tv_address_line1 = (TextView) customView.findViewById(R.id.tv_address_line1);
        TextView tv_address_line2 = (TextView) customView.findViewById(R.id.tv_address_line2);
        TextView tv_city = (TextView) customView.findViewById(R.id.tv_city);
        TextView tv_county = (TextView) customView.findViewById(R.id.tv_county);
        TextView tv_country = (TextView) customView.findViewById(R.id.tv_country);
        TextView tv_postcode = (TextView) customView.findViewById(R.id.tv_postcode);

        tv_patient_name.setText("Name: " + patient.getName());
        tv_ppsn.setText("PPSN: " + patient.get_id());
        tv_condition.setText("Condition: " + patient.getIllness());
        tv_address_line1.setText(patient.getAddress().getAddressLine1());
        tv_address_line2.setText(patient.getAddress().getAddressLine2());
        tv_city.setText(patient.getAddress().getCity());
        tv_county.setText(patient.getAddress().getCounty());
        tv_country.setText(patient.getAddress().getCountry());
        tv_postcode.setText(patient.getAddress().getPostCode());
        iv_patient_photo.setImageResource(R.drawable.no_image);

        return customView;
    }
}
