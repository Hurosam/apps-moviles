package com.example.ta4;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity6 extends AppCompatActivity {

    private EditText editTextMonto;
    private EditText editTextCategoria;
    private EditText editTextFecha;
    private EditText editTextDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        editTextMonto = findViewById(R.id.editTextNumberDecimal);
        editTextCategoria = findViewById(R.id.editTextText2);
        editTextFecha = findViewById(R.id.editTextDate);
        editTextDescripcion = findViewById(R.id.editTextText3);
    }

    public void guardar(View view) {
        String montoStr = editTextMonto.getText().toString();
        String categoriaStr = editTextCategoria.getText().toString();
        String fechaStr = editTextFecha.getText().toString();
        String descripcionStr = editTextDescripcion.getText().toString();

        if (montoStr.isEmpty() || categoriaStr.isEmpty() || fechaStr.isEmpty()) {
            Toast.makeText(this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double monto = Double.parseDouble(montoStr);
        Gasto nuevoGasto = new Gasto(monto, categoriaStr, fechaStr, descripcionStr);

        MainActivity3.listaGastos.add(nuevoGasto);

        finish();
    }
}