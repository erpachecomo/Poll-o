package mx.edu.ittepic.poll_o;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class Pantalla_Login extends AppCompatActivity {
   ConexionBD conexion;
    EditText Campo_Usuario,Campo_Contra;
    Button Ini_Sesion;
    String Usuario,Contrasena;
    VerificarConexionWIFI verificadorConexion;
    boolean sesion_valida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__login);
        verificadorConexion=new VerificarConexionWIFI(this);
        Campo_Usuario=(EditText)findViewById(R.id.CampoUsuario);
        Campo_Contra=(EditText)findViewById(R.id.CampoContrasena);
        Ini_Sesion=(Button)findViewById(R.id.Boton_in_sesion);
        sesion_valida=false;
        conexion=new ConexionBD(this,"Poll-oB2",null,1);
        Usuario=Contrasena="";
        Ini_Sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario = Campo_Usuario.getText().toString();
                Contrasena = Campo_Contra.getText().toString();
                if (!CamposVacios(Usuario, Contrasena)) {
                    /*LOGIN POR MEDIO DEL SERVIDOR*/
                    if (verificadorConexion.estaConectado()) {
                        //CODIGO LOGEARTE CON INTERNET
                        IniciarSesion(Usuario, Contrasena);

                    } else {
                        //AQUI DEBE IR EL CODIGO PARA MANDAR UN MJS AL SERVIDOR SI NO HAY INTERNET
                    }
                            /*-------------------------------------*/
                } else {
                    Toast.makeText(Pantalla_Login.this, "Campos vacios", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void IniciarSesion(String usuario, String contrasena) {
        try {
            ConexionWeb web = new ConexionWeb(Pantalla_Login.this);
            web.agregarVariables("operacion", "login");
            web.agregarVariables("celular",usuario);
            web.agregarVariables("password", contrasena);
            URL url = new URL("http://poll-o.ueuo.com/basededatos.php");
            web.execute(url);
            Toast.makeText(this, "Iniciando sesión", Toast.LENGTH_SHORT).show();

        }catch (MalformedURLException e) {
            new AlertDialog.Builder(Pantalla_Login.this).setMessage
                    (e.getMessage()).setTitle("Error").show();

        }

    }

    public void sesion_correcta(String Respuesta) {
        if(Respuesta.equals("si")){
            Toast.makeText(Pantalla_Login.this, "Inicio correcto", Toast.LENGTH_LONG).show();
            Intent MenuPrincipal = new Intent(Pantalla_Login.this, mx.edu.ittepic.poll_o.MenuPrincipal.class);
            MenuPrincipal.putExtra("Usuario", Usuario);
            startActivity(MenuPrincipal);

        }else{
            sesion_valida=false;
        }
    }

    private boolean CamposVacios(String Usu,String Contra){
        if(Usu.equals("") || Contra.equals("")){
            //return true; LINEA CORRECTa
            return true;
        }
        return false;
    }
}
