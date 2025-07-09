package com.example.ta4;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity7 extends AppCompatActivity {

    private EditText editTextNombreCategoria;
    private TextView tituloFormulario;
    private Button botonGuardar;
    private Button botonEliminar;

    private int categoriaId = -1;
    private int currentUserId = -1;
    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        ImageButton btnBack = findViewById(R.id.btn_back_categoria);
        btnBack.setOnClickListener(v -> onBackPressed());

        SharedPreferences prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Error de sesión.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = AppDatabase.getInstance(getApplicationContext());
        editTextNombreCategoria = findViewById(R.id.editTextText5);
        tituloFormulario = findViewById(R.id.textView7);
        botonGuardar = findViewById(R.id.button7);
        botonEliminar = findViewById(R.id.button_eliminar_categoria);

        categoriaId = getIntent().getIntExtra("CATEGORIA_ID", -1);

        if (categoriaId != -1) {
            tituloFormulario.setText("Editar Categoría");
            botonGuardar.setText("Actualizar");
            botonEliminar.setVisibility(View.VISIBLE);
            cargarDatosDeCategoria();
        } else {
            tituloFormulario.setText("Crear Nueva Categoría");
            botonGuardar.setText("Guardar");
            botonEliminar.setVisibility(View.GONE);
        }
    }

    private void cargarDatosDeCategoria() {
        databaseExecutor.execute(() -> {
            Categoria categoriaAEditar = db.categoriaDao().findById(categoriaId);
            runOnUiThread(() -> {
                if (categoriaAEditar != null) {
                    editTextNombreCategoria.setText(categoriaAEditar.getNombre());
                }
            });
        });
    }

    public void guardarCategoria(View view) {
        String nombreCategoria = editTextNombreCategoria.getText().toString().trim();

        if (nombreCategoria.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseExecutor.execute(() -> {
            if (categoriaId != -1) {
                Categoria categoria = db.categoriaDao().findById(categoriaId);
                if (categoria != null) {
                    categoria.setNombre(nombreCategoria);
                    db.categoriaDao().update(categoria);
                }
            } else {
                Categoria nuevaCategoria = new Categoria(nombreCategoria, currentUserId);
                db.categoriaDao().insert(nuevaCategoria);
            }
            finish();
        });
    }

    public void eliminarCategoria(View view) {
        if (categoriaId != -1) {
            databaseExecutor.execute(() -> {
                db.categoriaDao().deleteById(categoriaId);
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Categoría eliminada", Toast.LENGTH_SHORT).show());
                finish();
            });
        }
    }
}