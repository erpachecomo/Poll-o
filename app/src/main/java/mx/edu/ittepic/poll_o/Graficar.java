package mx.edu.ittepic.poll_o;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
        conexion=new ConexionBD(this,"Poll-o",null,1);

        titulo_encuesta=getIntent().getStringExtra("seleccion");
        encuesta.setText(titulo_encuesta);
        cargarPreguntasSpinner();


    }

    private void cargarPreguntasSpinner() {

        List<String> lables = conexion.obtenerPreguntas();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pregunta.setAdapter(dataAdapter);
    }
}
