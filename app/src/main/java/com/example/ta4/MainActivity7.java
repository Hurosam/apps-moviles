package com.example.ta4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity7 extends AppCompatActivity {

    private EditText editTextNombreCategoria;
    private TextView tituloFormulario;
    private Button botonGuardar;
    private Button botonEliminar;

    private int posicionCategoria = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        editTextNombreCategoria = findViewById(R.id.editTextText5);
        tituloFormulario = findViewById(R.id.textView7);
        botonGuardar = findViewById(R.id.button7);
        botonEliminar = findViewById(R.id.button_eliminar_categoria);

        posicionCategoria = getIntent().getIntExtra("CATEGORIA_POSITION", -1);

        if (posicionCategoria != -1) {
            tituloFormulario.setText("Editar Categoría");
            botonGuardar.setText("Actualizar");
            botonEliminar.setVisibility(View.VISIBLE);

            Categoria categoriaAEditar = MainActivity5.listaCategorias.get(posicionCategoria);
            editTextNombreCategoria.setText(categoriaAEditar.getNombre());
        } else {
            tituloFormulario.setText("Crear Nueva Categoría");
            botonGuardar.setText("Guardar");
            botonEliminar.setVisibility(View.GONE);
        }
    }

    public void guardarCategoria(View view) {
        String nombreCategoria = editTextNombreCategoria.getText().toString();

        if (nombreCategoria.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        Categoria categoriaActualizada = new Categoria(nombreCategoria);

        if (posicionCategoria != -1) {
            MainActivity5.listaCategorias.set(posicionCategoria, categoriaActualizada);
        } else {
            MainActivity5.listaCategorias.add(categoriaActualizada);
        }

        finish();
    }

    public void eliminarCategoria(View view) {
        if (posicionCategoria != -1) {
            MainActivity5.listaCategorias.remove(posicionCategoria);
            Toast.makeText(this, "Categoría eliminada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}