package com.example.ta4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsuario;
    private EditText editTextPassword;
    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsuario = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        db = AppDatabase.getInstance(getApplicationContext());
    }

    public void login(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }

    public void principal1(View view) {
        String usuario = editTextUsuario.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseExecutor.execute(() -> {
            if (db.usuarioDao().findByUsername(usuario) != null) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show());
                return;
            }

            Usuario nuevoUsuario = new Usuario(usuario, password);
            long newUserId = db.usuarioDao().insert(nuevoUsuario);

            if (newUserId > 0) {
                String[] defaultCategories = {"Comida", "Transporte", "Salud", "Compras", "Ocio", "Educación", "Hogar", "Otros"};
                for (String catName : defaultCategories) {
                    Categoria categoria = new Categoria(catName, (int) newUserId);
                    db.categoriaDao().insert(categoria);
                }

                runOnUiThread(() -> {
                    SharedPreferences prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("LOGGED_IN_USER_ID", (int) newUserId);
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Registro exitoso. ¡Bienvenido!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show());
            }
        });
    }
}