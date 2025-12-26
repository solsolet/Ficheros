package es.ua.eps.ficheros;

import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import es.ua.eps.ficheros.databinding.EstadoAlmacenamientoBinding;

public class EstadoAlmacenamiento extends AppCompatActivity {
    private EstadoAlmacenamientoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = EstadoAlmacenamientoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Estado de Almacenamiento");
        }

        mostrarEstado();

        binding.btVolver.setOnClickListener(v -> finish());
    }

    /**
     * Show state from environmental variables
     */
    private void mostrarEstado() {
        StringBuilder info = new StringBuilder();

        // General State external storage
        String estado = Environment.getExternalStorageState();
        info.append("ESTADO GENERAL:\n");
        info.append("getExternalStorageState(): ").append(estado).append("\n\n");

        // State interpretation
        info.append("SIGNIFICADO:\n");
        switch (estado) {
            case Environment.MEDIA_MOUNTED:
                info.append("✅ Montado y disponible (lectura/escritura)\n");
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                info.append("⚠️ Montado en solo lectura\n");
                break;
            case Environment.MEDIA_UNMOUNTED:
                info.append("❌ No montado\n");
                break;
            case Environment.MEDIA_CHECKING:
                info.append("🔄 Verificando estado...\n");
                break;
            case Environment.MEDIA_NOFS:
                info.append("❌ Sin sistema de archivos\n");
                break;
            case Environment.MEDIA_REMOVED:
                info.append("❌ Tarjeta SD eliminada\n");
                break;
            case Environment.MEDIA_SHARED:
                info.append("⚠️ Compartido por USB\n");
                break;
            case Environment.MEDIA_BAD_REMOVAL:
                info.append("❌ Eliminación incorrecta\n");
                break;
            case Environment.MEDIA_UNMOUNTABLE:
                info.append("❌ No se puede montar\n");
                break;
            default:
                info.append("Estado desconocido\n");
        }

        info.append("\n");

        // Additional info
//        info.append("DIRECTORIOS PÚBLICOS:\n");
//        info.append("DIRECTORY_MUSIC: ").append(Environment.DIRECTORY_MUSIC).append("\n");
//        info.append("DIRECTORY_PODCASTS: ").append(Environment.DIRECTORY_PODCASTS).append("\n");
//        info.append("DIRECTORY_RINGTONES: ").append(Environment.DIRECTORY_RINGTONES).append("\n");
//        info.append("DIRECTORY_ALARMS: ").append(Environment.DIRECTORY_ALARMS).append("\n");
//        info.append("DIRECTORY_NOTIFICATIONS: ").append(Environment.DIRECTORY_NOTIFICATIONS).append("\n");
//        info.append("DIRECTORY_PICTURES: ").append(Environment.DIRECTORY_PICTURES).append("\n");
//        info.append("DIRECTORY_MOVIES: ").append(Environment.DIRECTORY_MOVIES).append("\n");
//        info.append("DIRECTORY_DOWNLOADS: ").append(Environment.DIRECTORY_DOWNLOADS).append("\n");
//        info.append("DIRECTORY_DCIM: ").append(Environment.DIRECTORY_DCIM).append("\n");
//        info.append("DIRECTORY_DOCUMENTS: ").append(Environment.DIRECTORY_DOCUMENTS).append("\n");

        info.append("\n");

        // Real paths
        info.append("RUTAS DE ESTA APP:\n");
        info.append("Almacenamiento interno:\n");
        info.append(getFilesDir().getAbsolutePath()).append("\n\n");

        info.append("Almacenamiento externo (app-specific):\n");
        if (getExternalFilesDir(null) != null) {
            info.append(Objects.requireNonNull(getExternalFilesDir(null)).getAbsolutePath()).append("\n");
        } else {
            info.append("(No disponible)\n");
        }

        binding.tvEstado.setText(info.toString());
    }
}
