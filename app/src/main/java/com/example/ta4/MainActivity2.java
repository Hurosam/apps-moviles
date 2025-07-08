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

public class MainActivity2 extends AppCompatActivity {

    private EditText editTextUsuario;
    private EditText editTextPassword;
    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editTextUsuario = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        db = AppDatabase.getInstance(getApplicationContext());
    }

    public void registrar(View view) {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }

    public void principal2(View view) {
        String usuario = editTextUsuario.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseExecutor.execute(() -> {
            Usuario user = db.usuarioDao().findByCredentials(usuario, password);

            runOnUiThread(() -> {
                if (user != null) {
                    SharedPreferences prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("LOGGED_IN_USER_ID", user.getId());
                    editor.apply();

                    Toast.makeText(MainActivity2.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity2.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}