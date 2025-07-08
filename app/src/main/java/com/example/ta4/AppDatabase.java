package com.example.ta4;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Gasto.class, Categoria.class, Usuario.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GastoDao gastoDao();
    public abstract CategoriaDao categoriaDao();
    public abstract UsuarioDao usuarioDao();

    private static volatile AppDatabase INSTANCE;

    private static final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "gastos_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                CategoriaDao dao = INSTANCE.categoriaDao();
                dao.insert(new Categoria("Comida"));
                dao.insert(new Categoria("Transporte"));
                dao.insert(new Categoria("Salud"));
                dao.insert(new Categoria("Compras"));
                dao.insert(new Categoria("Ocio"));
                dao.insert(new Categoria("Educación"));
                dao.insert(new Categoria("Hogar"));
                dao.insert(new Categoria("Otros"));
            });
        }
    };
}