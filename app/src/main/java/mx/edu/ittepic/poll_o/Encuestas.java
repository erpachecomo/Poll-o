package mx.edu.ittepic.poll_o;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class Encuestas extends AppCompatActivity {
    ListView encuestasdisponibles;
    String encuesta_seleccionada;
    ConexionBD conexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuestas);
        encuesta_seleccionada="";
        conexion=new ConexionBD(this,"Poll-o",null,1);

        encuestasdisponibles=(ListView)findViewById(R.id.encuestas_disponibles);
        encuestasdisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                encuesta_seleccionada=encuestasdisponibles.getItemAtPosition(position).toString();

                Intent PantallaMenuEncuesta=new Intent(Encuestas.this,MenuEncuesta.class);
                PantallaMenuEncuesta.putExtra("seleccion",encuesta_seleccionada);
                startActivity(PantallaMenuEncuesta);

            }
        });
    }
    private void cargarEncuestas() {

        List<String> lables = conexion.obtenerPreguntas();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        encuestasdisponibles.setAdapter(dataAdapter);
    }
}
