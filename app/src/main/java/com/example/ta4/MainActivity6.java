package com.example.ta4;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity6 extends AppCompatActivity {

    private EditText editTextMonto;
    private Spinner spinnerCategoria;
    private EditText editTextFecha;
    private EditText editTextDescripcion;
    private EditText editTextEtiqueta;
    private Button botonGuardar;
    private Button botonEliminar;
    private TextView tituloFormulario;

    private int gastoId = -1;
    private int currentUserId = -1;
    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private ArrayAdapter<String> categoriaAdapter;
    private List<String> nombresCategorias = new ArrayList<>();
    private static final String AGREGAR_CATEGORIA_OPCION = "Agregar nueva categoría...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        ImageButton btnBack = findViewById(R.id.btn_back_gasto);
        btnBack.setOnClickListener(v -> onBackPressed());

        SharedPreferences prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        db = AppDatabase.getInstance(getApplicationContext());
        editTextMonto = findViewById(R.id.editTextNumberDecimal);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        editTextFecha = findViewById(R.id.editTextDate);
        editTextDescripcion = findViewById(R.id.editTextText3);
        editTextEtiqueta = findViewById(R.id.editTextText4);
        botonGuardar = findViewById(R.id.button);
        botonEliminar = findViewById(R.id.button_eliminar_gasto);
        tituloFormulario = findViewById(R.id.textView6);

        categoriaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCategorias);
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(categoriaAdapter);

        gastoId = getIntent().getIntExtra("GASTO_ID", -1);

        if (gastoId != -1) {
            tituloFormulario.setText("Editar Gasto");
            botonGuardar.setText("Actualizar Gasto");
            botonEliminar.setVisibility(View.VISIBLE);
        } else {
            tituloFormulario.setText("Agregar Gasto");
            botonGuardar.setText("Guardar Gasto");
            botonEliminar.setVisibility(View.GONE);
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
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCategoriasEnSpinner();
    }

    private void cargarCategoriasEnSpinner() {
        if (currentUserId == -1) return;
        databaseExecutor.execute(() -> {
            List<Categoria> categoriasDesdeDB = db.categoriaDao().getAll(currentUserId);
            runOnUiThread(() -> {
                String seleccionPrevia = null;
                if (spinnerCategoria.getSelectedItem() != null &&
                        !spinnerCategoria.getSelectedItem().toString().equals(AGREGAR_CATEGORIA_OPCION)) {
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
                } else if (seleccionPrevia != null) {
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
        String fechaFormateada = String.format(Locale.getDefault(), "%04d-%02d-%02d", anio, mes + 1, dia);
        editTextFecha.setText(fechaFormateada);
    }

    private void mostrarDialogoFecha() {
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                    editTextFecha.setText(fechaSeleccionada);
                }, anio, mes, dia);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void guardar(View view) {
        String montoStr = editTextMonto.getText().toString();
        if (montoStr.isEmpty()) {
            Toast.makeText(this, "Monto es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        String fechaStr = editTextFecha.getText().toString();
        String descripcionStr = editTextDescripcion.getText().toString();
        String etiquetaStr = editTextEtiqueta.getText().toString();
        String categoriaStr = spinnerCategoria.getSelectedItem().toString();

        double monto = Double.parseDouble(montoStr);
        Gasto gasto = new Gasto(monto, categoriaStr, etiquetaStr, fechaStr, descripcionStr, currentUserId);

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

    public void eliminarGasto(View view) {
        if (gastoId != -1) {
            databaseExecutor.execute(() -> {
                db.gastoDao().deleteById(gastoId);
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Gasto eliminado", Toast.LENGTH_SHORT).show());
                finish();
            });
        }
    }
}