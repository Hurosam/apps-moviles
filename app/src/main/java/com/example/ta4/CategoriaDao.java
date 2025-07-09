package com.example.ta4;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface CategoriaDao {
    @Query("SELECT * FROM categorias WHERE usuarioId = :userId ORDER BY nombre ASC")
    List<Categoria> getAll(int userId);

    @Query("SELECT * FROM categorias WHERE id = :categoriaId LIMIT 1")
    Categoria findById(int categoriaId);

    @Insert
    void insert(Categoria categoria);

    @Update
    void update(Categoria categoria);

    @Query("DELETE FROM categorias WHERE id = :categoriaId")
    void deleteById(int categoriaId);
}