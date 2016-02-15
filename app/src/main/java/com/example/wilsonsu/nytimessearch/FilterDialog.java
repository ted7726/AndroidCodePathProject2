package com.example.wilsonsu.nytimessearch;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilsonsu on 2/11/16.
 */
public class FilterDialog extends DialogFragment {
    private final String TYPE_ARTS = "Arts";
    private final String TYPE_SPORTS = "Sports";
    private final String TYPE_FASHION = "Fashion";

    @Bind(R.id.cb_arts) CheckBox cbArts;
    @Bind(R.id.cb_sports) CheckBox cbSports;
    @Bind(R.id.cb_fashion) CheckBox cbFashion;
    @Bind(R.id.bt_done) Button btDone;
    @Bind(R.id.datePicker) DatePicker datePicker;
    public static FilterDialog newInstance(String type, Date date) {
        FilterDialog frag = new FilterDialog();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putSerializable("date", date);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container);
        ButterKnife.bind(this, view);
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    public interface FilterDialogListener {
        void onFinishFilterDialog(String newsDesk, Date date);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String type = getArguments().getString("type");
        Date date = (Date)getArguments().getSerializable("date");
        resetCheckBox();
        if (!TextUtils.isEmpty(type)) {
            if (type.contains(TYPE_FASHION)) {
                cbFashion.setChecked(true);
            }
            if (type.contains(TYPE_SPORTS)) {
                cbSports.setChecked(true);
            }
            if (type.contains(TYPE_ARTS)) {
                cbArts.setChecked(true);
            }
        }



        if (date == null) {
            date = new Date();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePicker.updateDate(year, month, day);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        FilterDialogListener listener = (FilterDialogListener) getActivity();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String type = "";

        if (cbArts.isChecked()) {
            type+=TYPE_ARTS;
        }
        if (cbSports.isChecked()) {
            type+=TYPE_SPORTS;
        }
        if (cbFashion.isChecked()) {
            type+=TYPE_FASHION;
        }

        listener.onFinishFilterDialog(type, calendar.getTime());
        super.onDismiss(dialog);
    }

    private void resetCheckBox(){
        cbArts.setChecked(false);
        cbSports.setChecked(false);
        cbFashion.setChecked(false);

    }


}
