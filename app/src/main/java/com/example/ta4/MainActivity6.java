package com.example.ta4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity6 extends AppCompatActivity {

    private EditText editTextMonto;
    private Spinner spinnerCategoria;
    private EditText editTextFecha;
    private EditText editTextDescripcion;
    private EditText editTextEtiqueta;
    private Button botonGuardar;
    private TextView tituloFormulario;

    private int gastoId = -1;
    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private ArrayAdapter<String> categoriaAdapter;
    private List<String> nombresCategorias = new ArrayList<>();
    private static final String AGREGAR_CATEGORIA_OPCION = "Agregar nueva categoría...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        db = AppDatabase.getInstance(getApplicationContext());

        editTextMonto = findViewById(R.id.editTextNumberDecimal);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        editTextFecha = findViewById(R.id.editTextDate);
        editTextDescripcion = findViewById(R.id.editTextText3);
        editTextEtiqueta = findViewById(R.id.editTextText4);
        botonGuardar = findViewById(R.id.button);
        tituloFormulario = findViewById(R.id.textView6);

        categoriaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCategorias);
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(categoriaAdapter);

        gastoId = getIntent().getIntExtra("GASTO_ID", -1);

        if (gastoId != -1) {
            tituloFormulario.setText("Editar Gasto");
            botonGuardar.setText("Actualizar Gasto");
        } else {
            tituloFormulario.setText("Agregar Gasto");
            botonGuardar.setText("Guardar Gasto");
            ponerFechaActual();
        }

        editTextFecha.setOnClickListener(v -> mostrarDialogoFecha());

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = (String) parent.getItemAtPosition(position);
                if (AGREGAR_CATEGORIA_OPCION.equals(seleccion)) {
                    Intent intent = new Intent(MainActivity6.this, MainActivity7.class);
                    startActivity(intent);
                    spinnerCategoria.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCategoriasEnSpinner();
    }



    private void cargarCategoriasEnSpinner() {
        databaseExecutor.execute(() -> {
            List<Categoria> categoriasDesdeDB = db.categoriaDao().getAll();
            runOnUiThread(() -> {
                String seleccionPrevia = null;
                if (spinnerCategoria.getSelectedItem() != null) {
                    seleccionPrevia = spinnerCategoria.getSelectedItem().toString();
                }

                nombresCategorias.clear();
                for (Categoria cat : categoriasDesdeDB) {
                    nombresCategorias.add(cat.getNombre());
                }
                nombresCategorias.add(AGREGAR_CATEGORIA_OPCION);
                categoriaAdapter.notifyDataSetChanged();

                if (gastoId != -1) {
                    cargarDatosDelGasto();
                } else if (seleccionPrevia != null && !AGREGAR_CATEGORIA_OPCION.equals(seleccionPrevia)) {
                    int oldPosition = categoriaAdapter.getPosition(seleccionPrevia);
                    if (oldPosition >= 0) {
                        spinnerCategoria.setSelection(oldPosition);
                    }
                }
            });
        });
    }


    private void cargarDatosDelGasto() {
        databaseExecutor.execute(() -> {
            Gasto gastoAEditar = db.gastoDao().findById(gastoId);
            runOnUiThread(() -> {
                if (gastoAEditar != null) {
                    editTextMonto.setText(String.valueOf(gastoAEditar.getMonto()));
                    editTextFecha.setText(gastoAEditar.getFecha());
                    editTextDescripcion.setText(gastoAEditar.getDescripcion());
                    editTextEtiqueta.setText(gastoAEditar.getEtiqueta());

                    int spinnerPosition = categoriaAdapter.getPosition(gastoAEditar.getCategoria());
                    if (spinnerPosition >= 0) {
                        spinnerCategoria.setSelection(spinnerPosition);
                    }
                }
            });
        });
    }

    private void ponerFechaActual() {
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        editTextFecha.setText(dia + "/" + (mes + 1) + "/" + anio);
    }

    private void mostrarDialogoFecha() {
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    editTextFecha.setText(fechaSeleccionada);
                }, anio, mes, dia);
        datePickerDialog.show();
    }

    public void guardar(View view) {
        String montoStr = editTextMonto.getText().toString();
        String fechaStr = editTextFecha.getText().toString();
        String descripcionStr = editTextDescripcion.getText().toString();
        String etiquetaStr = editTextEtiqueta.getText().toString();

        if (spinnerCategoria.getSelectedItem() == null) {
            Toast.makeText(this, "No hay categorías disponibles. Por favor, cree una primero.", Toast.LENGTH_LONG).show();
            return;
        }

        String categoriaStr = spinnerCategoria.getSelectedItem().toString();

        if (AGREGAR_CATEGORIA_OPCION.equals(categoriaStr)) {
            Toast.makeText(this, "Por favor, seleccione una categoría válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (montoStr.isEmpty() || categoriaStr.isEmpty()) {
            Toast.makeText(this, "Monto y Categoría son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double monto = Double.parseDouble(montoStr);
        Gasto gasto = new Gasto(monto, categoriaStr, etiquetaStr, fechaStr, descripcionStr);

        databaseExecutor.execute(() -> {
            if (gastoId != -1) {
                gasto.id = gastoId;
                db.gastoDao().update(gasto);
            } else {
                db.gastoDao().insert(gasto);
            }
            finish();
        });
    }
}