package com.example.ta4;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity5 extends AppCompatActivity {

    private RecyclerView recyclerViewCategorias;
    private CategoriaAdapter categoriaAdapter;
    private List<Categoria> listaCategorias = new ArrayList<>();
    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        db = AppDatabase.getInstance(getApplicationContext());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Categorías");

        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);
        categoriaAdapter = new CategoriaAdapter(listaCategorias);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategorias.setAdapter(categoriaAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCategorias();
    }

    private void cargarCategorias() {
        databaseExecutor.execute(() -> {
            List<Categoria> categoriasDesdeDB = db.categoriaDao().getAll();
            runOnUiThread(() -> {
                listaCategorias.clear();
                listaCategorias.addAll(categoriasDesdeDB);
                categoriaAdapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_inicio) {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_graficos) {
            Intent intent = new Intent(this, MainActivity4.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_categorias) {
            Toast.makeText(this, "Ya estás en Categorías", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_logout) {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void crearCategoria(View view) {
        Intent intent = new Intent(this, MainActivity7.class);
        startActivity(intent);
    }
}