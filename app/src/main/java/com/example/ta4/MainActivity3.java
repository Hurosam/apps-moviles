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

public class MainActivity3 extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<Gasto> listaGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Mis Gastos");

        listaGastos = new ArrayList<>();
        listaGastos.add(new Gasto(20.50, "Comida", "24/05/2024", "Almuerzo"));
        listaGastos.add(new Gasto(18.00, "Transporte", "24/05/2024", "Taxi al trabajo"));
        listaGastos.add(new Gasto(50.00, "Ocio", "23/05/2024", "Entrada al cine"));

        recyclerView = findViewById(R.id.recyclerViewGastos);
        recyclerAdapter = new RecyclerAdapter(listaGastos);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
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
            Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_graficos) {
            Intent intent = new Intent(this, MainActivity4.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_categorias) {
            Intent intent = new Intent(this, MainActivity5.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_logout) {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void gastos(View view) {
        Intent intent = new Intent(MainActivity3.this, MainActivity6.class);
        startActivity(intent);
    }
}