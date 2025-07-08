package com.example.ta4;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Gasto.class, Categoria.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GastoDao gastoDao();
    public abstract CategoriaDao categoriaDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "gastos_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}