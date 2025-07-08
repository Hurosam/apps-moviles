package com.example.ta4;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDao {
    @Insert
    long insert(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario LIMIT 1")
    Usuario findByUsername(String usuario);

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario AND password = :password LIMIT 1")
    Usuario findByCredentials(String usuario, String password);
}