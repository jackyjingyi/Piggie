package com.example.piggie.Adapter;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piggie.R;
import com.example.piggie.SQLite.Record;
import com.example.piggie.SQLite.RecordDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private Context context;
    private List<Record> recordList;
    private int chosedId;
    private RecordDatabase db;

    public RecordAdapter(Context context, List<Record> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.frag_record_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Record record = recordList.get(i);
        String date = record.getDate();
        Date recordDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            recordDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String displayDate = df.format(recordDate);
        viewHolder.dateTV.setText(displayDate);
        viewHolder.weightTV.setText(Float.toString(record.getWeight()));
    }

    @Override
    public int getItemCount() {
        if (recordList == null) {
            return 0;
        } else {
            return recordList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView dateTV;
        public TextView weightTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            db = Room.databaseBuilder(itemView.getContext(), RecordDatabase.class, "RecordDatabase").fallbackToDestructiveMigration().build();
            itemView.setOnClickListener(this);

            dateTV = itemView.findViewById(R.id.list_date);
            weightTV = itemView.findViewById(R.id.list_weight);
        }

        @Override
        public void onClick(View view) {
//            get the position of row
            final int position = getAdapterPosition();
//            display alertdialog
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setTitle("Alert");
            alertDialogBuilder.setMessage("Are you sure to delete this record?");
            alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Record record = recordList.get(position);
                    chosedId = record.getId();
                    DeleteById deleteById = new DeleteById();
                    deleteById.execute();
                    recordList.remove(position);
                    notifyDataSetChanged();
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        }
    }

    private class DeleteById extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db.recordDao().deleteById(chosedId);
            return null;
        }
    }
}
