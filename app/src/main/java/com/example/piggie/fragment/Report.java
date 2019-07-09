package com.example.piggie.fragment;


import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.piggie.R;
import com.example.piggie.SQLite.Record;
import com.example.piggie.SQLite.RecordDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class Report extends Fragment {
    private LineChartView lineChartViewMongo;
    private LineChartView lineChartViewSql;
    private RecordDatabase db;
    private View vReport;
    private List<Record> mongoData;
    private List<Record> sequelData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vReport = inflater.inflate(R.layout.frag_report, container, false);
        db = Room.databaseBuilder(getActivity().getApplicationContext(), RecordDatabase.class, "RecordDatabase").fallbackToDestructiveMigration().build();

        mongoData = new ArrayList<>();
        sequelData = new ArrayList<>();

        lineChartViewMongo = vReport.findViewById(R.id.report_mongo);
        GetMongoData getMongoData = new GetMongoData();
        getMongoData.execute();
        lineChartViewSql = vReport.findViewById(R.id.report_sequel);
        GetSequelData getSequelData = new GetSequelData();
        getSequelData.execute();

        try {
            Thread.currentThread().sleep(100);
        } catch (Exception e) {
        }

//        display mongo chart
        List<Record> mongoDataSorted = sortList(mongoData);
        List yAxisValuesMongo = new ArrayList();
        List axisValuesMongo = new ArrayList();
        Line lineMongo = new Line(yAxisValuesMongo).setColor(Color.parseColor("#f8d7bd"));
        for (int i = 0; i < mongoDataSorted.size(); i++) {
            String recordDate = mongoDataSorted.get(i).getDate();
            Date date = new Date();
            DateFormat recordFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                date = recordFormat.parse(recordDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat displayFormat = new SimpleDateFormat("ddMMMyy");
            String displayDate = displayFormat.format(date);
            axisValuesMongo.add(i, new AxisValue(i).setLabel(displayDate));
            yAxisValuesMongo.add(new PointValue(i, mongoDataSorted.get(i).getWeight()));
        }
        List linesMongo = new ArrayList();
        linesMongo.add(lineMongo);
        LineChartData dataMongo = new LineChartData();
        dataMongo.setLines(linesMongo);
        Axis axisMongo = new Axis();
        axisMongo.setValues(axisValuesMongo);
        dataMongo.setAxisXBottom(axisMongo);
        axisMongo.setTextColor(Color.parseColor("#bad8df"));
        Axis yAxisMongo = new Axis();
        dataMongo.setAxisYLeft(yAxisMongo);
        yAxisMongo.setTextColor(Color.parseColor("#bad8df"));
        yAxisMongo.setName("Weight(g)");

        lineChartViewMongo.setLineChartData(dataMongo);

//        display sequel chart
        List<Record> sequelDataSorted = sortList(sequelData);
        List yAxisValuesSequel = new ArrayList();
        List axisValuesSequel = new ArrayList();
        Line lineSequel = new Line(yAxisValuesSequel).setColor(Color.parseColor("#fdd327"));
        for (int i = 0; i < sequelDataSorted.size(); i++) {
            String recordDate = sequelDataSorted.get(i).getDate();
            Date date = new Date();
            DateFormat recordFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                date = recordFormat.parse(recordDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat displayFormat = new SimpleDateFormat("ddMMMyy");
            String displayDate = displayFormat.format(date);
            axisValuesSequel.add(i, new AxisValue(i).setLabel(displayDate));
            yAxisValuesSequel.add(new PointValue(i, sequelDataSorted.get(i).getWeight()));
        }
        List linesSequel = new ArrayList();
        linesSequel.add(lineSequel);
        LineChartData dataSequel = new LineChartData();
        dataSequel.setLines(linesSequel);
        Axis axisSequel = new Axis();
        axisSequel.setValues(axisValuesSequel);
        dataSequel.setAxisXBottom(axisSequel);
        axisSequel.setTextColor(Color.parseColor("#b5e3b5"));
        Axis yAxisSequel = new Axis();
        dataSequel.setAxisYLeft(yAxisSequel);
        yAxisSequel.setTextColor(Color.parseColor("#b5e3b5"));
        yAxisSequel.setName("Weight(g)");

        lineChartViewSql.setLineChartData(dataSequel);

        return vReport;
    }

    private class GetMongoData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mongoData = db.recordDao().findByName("Mongo");
            return null;
        }
    }

    private class GetSequelData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            sequelData = db.recordDao().findByName("Sequel");
            return null;
        }
    }

    public List<Record> sortList(List<Record> recordList) {
//        selection sort by date
        for (int i = 0; i < recordList.size() - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < recordList.size(); j++) {
                int mid_index_date = Integer.parseInt(recordList.get(min_idx).getDate());
                int jDate = Integer.parseInt(recordList.get(j).getDate());
                if (jDate < mid_index_date) {
                    min_idx = j;
                }
                Record rTemp = recordList.get(min_idx);
                recordList.set(min_idx, recordList.get(i));
                recordList.set(i, rTemp);
            }
        }
        return recordList;
    }

}
