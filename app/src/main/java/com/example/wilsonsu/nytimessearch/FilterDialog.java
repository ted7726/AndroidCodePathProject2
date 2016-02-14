package com.example.wilsonsu.nytimessearch;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    @Bind(R.id.etNewsDesk) EditText etNewsDesk;
    @Bind(R.id.datePicker) DatePicker datePicker;
    public static FilterDialog newInstance(String title, Date date) {
        FilterDialog frag = new FilterDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("date", date);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container);
        ButterKnife.bind(this, view);
        return view;
    }

    public interface FilterDialogListener {
        void onFinishFilterDialog(String newsDesk, Date date);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString("title");
        Date date = (Date)getArguments().getSerializable("date");

        etNewsDesk.setText(title);
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
        listener.onFinishFilterDialog(etNewsDesk.getText().toString(), calendar.getTime());
        super.onDismiss(dialog);
    }


}
