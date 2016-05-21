package mx.edu.ittepic.poll_o;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MenuEncuesta extends AppCompatActivity {
    String encuesta_a_Realizar;
    TextView Encuesta;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_encuesta);
        encuesta_a_Realizar=getIntent().getStringExtra("seleccion");
        Encuesta=(TextView)findViewById(R.id.Encuesta_a_Realizar);
        Encuesta.setText(encuesta_a_Realizar);

    }


}
