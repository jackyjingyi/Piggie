package com.example.piggie.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.piggie.Adapter.RecordAdapter;
import com.example.piggie.R;
import com.example.piggie.SQLite.Record;
import com.example.piggie.SQLite.RecordDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Sequel extends Fragment {
    private View vSequel;
    private TextInputEditText weight;
    private TextInputEditText date;
    private Record record;
    private RecordDatabase db;
    private RecyclerView recyclerView;
    private List<Record> recordList;
    private String recordDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vSequel = inflater.inflate(R.layout.frag_sequel, container, false);
        db = Room.databaseBuilder(getActivity().getApplicationContext(), RecordDatabase.class, "RecordDatabase").fallbackToDestructiveMigration().build();
        recyclerView = vSequel.findViewById(R.id.sequel_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(vSequel.getContext()));
        ReadRecord readRecord = new ReadRecord();
        readRecord.execute();

        date = vSequel.findViewById(R.id.sequel_date);
        Date today = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        recordDate = df.format(today);
        date.setText(dateFormat.format(today));
        TextView sequel_date_text = vSequel.findViewById(R.id.sequel_date_text);
        sequel_date_text.setOnClickListener(new ChooseDate());

        weight = vSequel.findViewById(R.id.sequel_weight);
        Button submit = vSequel.findViewById(R.id.sequel_submit);
        submit.setOnClickListener(new Submit());
        return vSequel;
    }

    public class ChooseDate implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month += 1;
//                    convert to string
                    String yearStr = Integer.toString(year);
                    String monthStr = Integer.toString(month);
                    String dayStr = Integer.toString(dayOfMonth);
                    if (month < 10) {
                        monthStr = "0" + monthStr;
                    }
                    if (dayOfMonth < 10) {
                        dayStr = "0" + dayStr;
                    }
                    recordDate = yearStr + monthStr + dayStr;
                    String dateStr = dayStr + "/" + monthStr + "/" + yearStr;
                    date.setText(dateStr);
                }
            };

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(vSequel.getContext(), R.style.Theme_choose_date, dateSetListener, year, month, day);
            datePickerDialog.show();
        }
    }

    private class Submit implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (weight.getText().toString().isEmpty()) {
                weight.setError("Required");
            } else {
                int weightInt = Integer.parseInt(weight.getText().toString());
                record = new Record("Sequel", recordDate, weightInt);
                InsertRecord insertRecord = new InsertRecord();
                insertRecord.execute();
            }
        }
    }

    private class InsertRecord extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            long id = db.recordDao().insert(record);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            weight.getText().clear();
            ReadRecord readRecord = new ReadRecord();
            readRecord.execute();
        }
    }

    private class ReadRecord extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            recordList = db.recordDao().findByName("Sequel");
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            RecordAdapter recordAdapter = new RecordAdapter(vSequel.getContext(), recordList);
            recyclerView.setAdapter(recordAdapter);
        }
    }
}
