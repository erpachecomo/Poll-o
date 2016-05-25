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

public class Pantalla_Login extends AppCompatActivity {
   ConexionBD conexion;
    EditText Campo_Usuario,Campo_Contra;
    Button Ini_Sesion;
    String Usuario,Contrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__login);
        Campo_Usuario=(EditText)findViewById(R.id.CampoUsuario);
        Campo_Contra=(EditText)findViewById(R.id.CampoContrasena);
        Ini_Sesion=(Button)findViewById(R.id.Boton_in_sesion);
        conexion=new ConexionBD(this,"Poll-o",null,1);
        Usuario=Contrasena="";
        Ini_Sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Usuario=Campo_Usuario.getText().toString();
                        Contrasena=Campo_Contra.getText().toString();
                if(!CamposVacios(Usuario,Contrasena)){
                        try{
                            SQLiteDatabase base = conexion.getReadableDatabase();
                            String SQL="SELECT *FROM  Usuario WHERE celular ='"+Usuario+"'";
                            Cursor res =base.rawQuery(SQL, null);
                            if(!res.moveToFirst()){ //Aqui hay que quitarle el ! cuando ya este la aplicación.


                               //String contraseñaReal=res.getString(4); Esta es la linea correcta cuando funcione
                                String contraseñaReal=Contrasena;
                                if(Contrasena.equals(contraseñaReal)) {
                                    Toast.makeText(Pantalla_Login.this, "Inicio correcto", Toast.LENGTH_LONG).show();
                                    Intent MenuPrincipal= new Intent(Pantalla_Login.this, mx.edu.ittepic.poll_o.MenuPrincipal.class);
                                    startActivity(MenuPrincipal);
                                }
                                else{
                                    Toast.makeText(Pantalla_Login.this,"contraseña no validos",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(Pantalla_Login.this,"Usuario no validos",Toast.LENGTH_LONG).show();
                            }
                            base.close();
                        }catch (SQLiteException e){
                            AlertDialog.Builder alerta= new AlertDialog.Builder(Pantalla_Login.this);
                            alerta.setTitle("ERROR").setMessage(e.getMessage()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                }else{
                    Toast.makeText(Pantalla_Login.this,"Campos vacios",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean CamposVacios(String Usu,String Contra){
        if(Usu.equals("") || Contra.equals("")){
            return true;
        }
        return false;
    }
}
