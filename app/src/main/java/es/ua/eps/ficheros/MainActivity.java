package es.ua.eps.ficheros;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import es.ua.eps.ficheros.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {
    private MainActivityBinding binding;
    private static final String FILENAME = "texto.txt";
    private File currentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentFile = new File(getFilesDir(), FILENAME);

        initListeners();
        updateButtonState();
    }

    private void initListeners(){
        binding.btVerEstadoAlmExterno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { irEstado(); }
        });

        binding.btAnyadirFichero.setOnClickListener(v -> {
            String texto = binding.etTextoGuardar.getText().toString();

            if (texto.isEmpty()) {
                Toast.makeText(MainActivity.this,
                        getString(R.string.introduceTextoPrimero),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            anyadeTexto(texto);
            binding.etTextoGuardar.setText(""); // clean EditText
        });

        binding.btVerFichero.setOnClickListener(v -> irContenido());

        binding.btExternalStorage.setOnClickListener(v -> moverExterno());

        binding.btInternalStorage.setOnClickListener(v -> moverInterno());

        binding.btCerrar.setOnClickListener(v -> {
            finishAffinity();
            System.exit(-1);
        });
    }
    private void anyadeTexto(String txt) {
        try {
            FileOutputStream fos = new FileOutputStream(currentFile, true);

            // If file has content: add \n
            if (currentFile.length() > 0) {
                fos.write("\n".getBytes());
            }

            fos.write(txt.getBytes());
            fos.close();

            Log.d("Ficheros", "Texto añadido correctamente a: " + currentFile.getAbsolutePath());
            Toast.makeText(this, "Texto guardado", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){
            Log.d("Ficheros", "Error añadiendo texto", e);
            Toast.makeText(this, "Error guardando texto", Toast.LENGTH_SHORT).show();
        }
    }

    /**********************************
    *  Move from Internal -> External *
    * ********************************/
    private void moverExterno() {
        // Check external storage available
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this,
                    "Almacenamiento externo no disponible",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Check file exists on Internal
        File internalFile = new File(getFilesDir(), FILENAME);
        if (!internalFile.exists()) {
            Toast.makeText(this,
                    "No hay fichero que mover",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // App-specific external directory (without permissions)
        File externalDir = getExternalFilesDir(null);
        if (externalDir == null) {
            Toast.makeText(this,
                    "No se puede acceder al almacenamiento externo",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        File externalFile = new File(externalDir, FILENAME);

        try{
            copiarFichero(internalFile, externalFile);

            // Delete original
            if (internalFile.delete()) {
                currentFile = externalFile;
                updateButtonState();
                Toast.makeText(this,
                        "Fichero movido a almacenamiento externo",
                        Toast.LENGTH_SHORT).show();
                Log.d("Ficheros", "Fichero movido a: " + externalFile.getAbsolutePath());
            } else {
                Log.e("Ficheros", "No se pudo eliminar el fichero original");
            }
        } catch (IOException e) {
            Log.e("Ficheros", "Error moviendo fichero", e);
            Toast.makeText(this, "Error moviendo fichero", Toast.LENGTH_SHORT).show();
        }
    }

    /**********************************
    *  Move from External -> Internal *
    * ********************************/
    private void moverInterno() {
        File externalDir = getExternalFilesDir(null);
        if (externalDir == null) {
            Toast.makeText(this,
                    "Almacenamiento externo no accesible",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Check file exists in external
        File externalFile = new File(externalDir, FILENAME);
        if (!externalFile.exists()) {
            Toast.makeText(this,
                    "No hay fichero que mover",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        File internalFile = new File(getFilesDir(), FILENAME);

        try {
            // Copy content
            copiarFichero(externalFile, internalFile);

            // Delete original
            if (externalFile.delete()) {
                currentFile = internalFile;
                updateButtonState();
                Toast.makeText(this,
                        "Fichero movido a almacenamiento interno",
                        Toast.LENGTH_SHORT).show();
                Log.d("Ficheros", "Fichero movido a: " + internalFile.getAbsolutePath());
            } else {
                Log.e("Ficheros", "No se pudo eliminar el fichero original");
            }

        } catch (IOException e) {
            Log.e("Ficheros", "Error moviendo fichero", e);
            Toast.makeText(this, "Error moviendo fichero", Toast.LENGTH_SHORT).show();
        }
    }

    /**********************************
    *  Copy content from one to other *
    * ********************************/
    private void copiarFichero(File origen, File destino) throws IOException{
        FileInputStream fis = new FileInputStream(origen);
        FileOutputStream fos = new FileOutputStream(destino);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, length);
        }

        fis.close();
        fos.close();
    }

    private void updateButtonState(){
        File internalFile = new File(getFilesDir(), FILENAME);
        File externalDir = getExternalFilesDir(null);
        File externalFile = externalDir != null ? new File(externalDir, FILENAME) : null;

        boolean estaEnInterno = internalFile.exists();
        boolean estaEnExterno = externalFile != null && externalFile.exists();

        // Update currentFile according where it is
        if (estaEnInterno) {
            currentFile = internalFile;
        } else if (estaEnExterno) {
            currentFile = externalFile;
        }

        // Buttons: enable
        binding.btExternalStorage.setEnabled(estaEnInterno);
        binding.btInternalStorage.setEnabled(estaEnExterno);
    }

    /**************
    *  NAVIGATION *
    * ************/

    private void irEstado(){
        Intent ir = new Intent(this, EstadoAlmacenamiento.class);
        startActivity(ir);
    }
    private void irContenido(){
        if (!currentFile.exists()) {
            Toast.makeText(this,
                    "No hay contenido para mostrar",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent ir = new Intent(this, ContenidoFichero.class);
        // send path
        ir.putExtra("filepath", currentFile.getAbsolutePath());
        startActivity(ir);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update buttons each time we come back to Main
        updateButtonState();
    }
}
