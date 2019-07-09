package com.example.piggie.SQLite;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Record.class}, version = 3, exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase {
    public abstract RecordDao recordDao();

    private static volatile RecordDatabase INSTANCE;

    static RecordDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecordDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RecordDatabase.class, "customer_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
