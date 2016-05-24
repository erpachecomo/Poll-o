package mx.edu.ittepic.poll_o;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Preguntas extends AppCompatActivity {
    TextView encuesta;
    String titulo_encuesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        encuesta=(TextView)findViewById(R.id.Encuesta_Preguntas);
        titulo_encuesta=getIntent().getStringExtra("seleccion");
        encuesta.setText(titulo_encuesta);
    }
}
