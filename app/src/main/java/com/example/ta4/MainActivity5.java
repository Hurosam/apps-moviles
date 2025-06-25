package com.example.ta4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity5 extends AppCompatActivity {

    RecyclerView recyclerViewCategorias;
    CategoriaAdapter categoriaAdapter;
    List<Categoria> listaCategorias;
    ActivityResultLauncher<Intent> agregarCategoriaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Categorías");

        listaCategorias = new ArrayList<>();
        listaCategorias.add(new Categoria("Comida"));
        listaCategorias.add(new Categoria("Transporte"));
        listaCategorias.add(new Categoria("Ocio"));

        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);
        categoriaAdapter = new CategoriaAdapter(listaCategorias);

        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategorias.setAdapter(categoriaAdapter);

        agregarCategoriaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String nombre = data.getStringExtra("NUEVA_CATEGORIA_NOMBRE");
                            Categoria nuevaCategoria = new Categoria(nombre);
                            listaCategorias.add(nuevaCategoria);
                            categoriaAdapter.notifyItemInserted(listaCategorias.size() - 1);
                        }
                    }
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
        agregarCategoriaLauncher.launch(intent);
    }
}