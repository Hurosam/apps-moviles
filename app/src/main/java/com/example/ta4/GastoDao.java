package com.example.ta4;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface GastoDao {
    @Query("SELECT * FROM gastos WHERE usuarioId = :userId ORDER BY id DESC")
    List<Gasto> getAll(int userId);

    @Query("SELECT * FROM gastos WHERE id = :gastoId LIMIT 1")
    Gasto findById(int gastoId);

    @Insert
    void insert(Gasto gasto);

    @Update
    void update(Gasto gasto);

    @Delete
    void delete(Gasto gasto);
}