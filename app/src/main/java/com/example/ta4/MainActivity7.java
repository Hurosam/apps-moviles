package com.example.ta4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity7 extends AppCompatActivity {

    private EditText editTextNombreCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        editTextNombreCategoria = findViewById(R.id.editTextText5);
    }

    public void guardarCategoria(View view) {
        String nombreCategoria = editTextNombreCategoria.getText().toString();

        if (nombreCategoria.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un nombre para la categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("NUEVA_CATEGORIA_NOMBRE", nombreCategoria);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}