package mx.edu.ittepic.poll_o;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MenuPrincipal extends AppCompatActivity {
    ImageButton AcercaDe,Encuestas,Salir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Salir=(ImageButton)findViewById(R.id.imag_butt_salir);
        Encuestas=(ImageButton)findViewById(R.id.imag_butt_Encuestas);
        AcercaDe=(ImageButton)findViewById(R.id.imag_butt_Acerca);
    }
    public void Cambiar_Salir(View v){
        //Yo creo que aqui debe mandar el mensaje de desconexion
        //por mientras solo lo mandare a la pagina de login.
        Intent paginaLogin=new Intent(MenuPrincipal.this,Pantalla_Login.class);
        startActivity(paginaLogin);
    }
    public void Cambiar_Acerca(View v){
        //A la pagina de nuestros datos
        Intent paginaAcercaDe=new Intent(MenuPrincipal.this,AcercaDe.class);
        startActivity(paginaAcercaDe);
    }
    public void Cambiar_Encuestas(View v){
        //A la pagina de nuestros datos
        Intent paginaEncuestas=new Intent(MenuPrincipal.this,Encuestas.class);
        startActivity(paginaEncuestas);
    }
}
