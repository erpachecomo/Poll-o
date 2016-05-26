package mx.edu.ittepic.poll_o;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Graficar extends AppCompatActivity {
    TextView encuesta;
    Spinner pregunta;
    ConexionBD conexion;
    String titulo_encuesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficar);
        encuesta=(TextView)findViewById(R.id.Encuesta_grafica);
        pregunta = (Spinner) findViewById(R.id.spinner);
        conexion=new ConexionBD(this,"Poll-oB2",null,1);

        titulo_encuesta=getIntent().getStringExtra("seleccion");
        String[] arreglo = titulo_encuesta.split("-");
        int ide = Integer.parseInt(arreglo[0]);

        Toast.makeText(this,"ID: "+arreglo[0],Toast.LENGTH_LONG).show();

        encuesta.setText(titulo_encuesta);
        cargarPreguntasSpinner(ide);


    }

    private void cargarPreguntasSpinner(int id) {

        List<String> lables = conexion.obtenerPreguntas(id);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pregunta.setAdapter(dataAdapter);
    }
}
