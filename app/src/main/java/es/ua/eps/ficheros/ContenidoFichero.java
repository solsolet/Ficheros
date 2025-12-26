package es.ua.eps.ficheros;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import es.ua.eps.ficheros.databinding.ContenidoFicheroBinding;

public class ContenidoFichero extends AppCompatActivity {
    private ContenidoFicheroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ContenidoFicheroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Contenido del Fichero");
        }

        // get file path from Intent
        String filepath = getIntent().getStringExtra("filepath");

        if (filepath != null) {
            cargarContenido(filepath);
        } else {
            binding.tvContenido.setText(getString(R.string.errorNoRutaFichero));
        }

        binding.btVolver.setOnClickListener(v -> finish());
    }

    /**
     * Read file content and show it
     */
    private void cargarContenido(String filepath) {
        File file = new File(filepath);

        if (!file.exists()) {
            binding.tvContenido.setText(getString(R.string.ficheroNoExiste));
            return;
        }

        try {
            StringBuilder contenido = new StringBuilder();
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }

            reader.close();
            fis.close();

            if (contenido.isEmpty()) {
                binding.tvContenido.setText(getString(R.string.ficheroVacio));
            } else {
                binding.tvContenido.setText(contenido.toString());
            }

        } catch (IOException e) {
            binding.tvContenido.setText(getString(R.string.errorLecturaFichero) + e.getMessage());
            Toast.makeText(this, "Error leyendo fichero", Toast.LENGTH_SHORT).show();
        }
    }
}
