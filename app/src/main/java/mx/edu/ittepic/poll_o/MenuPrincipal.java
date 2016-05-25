package mx.edu.ittepic.poll_o;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MenuPrincipal extends AppCompatActivity {
    ImageButton AcercaDe,Encuestas,Salir,actualizar;
    VerificarConexionWIFI verificadorConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Salir=(ImageButton)findViewById(R.id.salir);
        Encuestas=(ImageButton)findViewById(R.id.realizar_encuesta);
        AcercaDe=(ImageButton)findViewById(R.id.acerca_de);
        actualizar=(ImageButton)findViewById(R.id.Botton_actualizar);
        verificadorConexion=new VerificarConexionWIFI(this);
    }
    /*APARTADO PARA ACTUALIZAR COMPLETAMENTE LA BASE DE DATOS BAJANDO LOS DATOS DEL SERVIDOR*/
    public void Actualizar(View v){
        if(verificadorConexion.estaConectado()) {
            ActualizarBaseDeDatos();
        }
        else{
            Toast.makeText(this,"No tienes conexion a internet",Toast.LENGTH_SHORT).show();
        }
    }
    void ActualizarBaseDeDatos(){

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
