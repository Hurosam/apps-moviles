package com.example.ta4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity6 extends AppCompatActivity {

    private EditText editTextMonto;
    private EditText editTextCategoria;
    private EditText editTextFecha;
    private EditText editTextDescripcion;
    private Button botonGuardar;
    private TextView tituloFormulario;

    private int posicionGasto = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        editTextMonto = findViewById(R.id.editTextNumberDecimal);
        editTextCategoria = findViewById(R.id.editTextText2);
        editTextFecha = findViewById(R.id.editTextDate);
        editTextDescripcion = findViewById(R.id.editTextText3);
        botonGuardar = findViewById(R.id.button);
        tituloFormulario = findViewById(R.id.textView6);

        posicionGasto = getIntent().getIntExtra("GASTO_POSITION", -1);

        if (posicionGasto != -1) {
            tituloFormulario.setText("Editar Gasto");
            botonGuardar.setText("Actualizar Gasto");
            cargarDatosDelGasto();
        } else {
            tituloFormulario.setText("Agregar Gasto");
            botonGuardar.setText("Guardar Gasto");
        }
    }

    private void cargarDatosDelGasto() {
        Gasto gastoAEditar = MainActivity3.listaGastos.get(posicionGasto);
        editTextMonto.setText(String.valueOf(gastoAEditar.getMonto()));
        editTextCategoria.setText(gastoAEditar.getCategoria());
        editTextFecha.setText(gastoAEditar.getFecha());
        editTextDescripcion.setText(gastoAEditar.getDescripcion());
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
        Gasto gastoActualizado = new Gasto(monto, categoriaStr, fechaStr, descripcionStr);

        if (posicionGasto != -1) {
            MainActivity3.listaGastos.set(posicionGasto, gastoActualizado);
        } else {
            MainActivity3.listaGastos.add(gastoActualizado);
        }

        finish();
    }
}