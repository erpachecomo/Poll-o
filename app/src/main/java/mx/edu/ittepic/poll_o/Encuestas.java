package mx.edu.ittepic.poll_o;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Encuestas extends AppCompatActivity {
    ListView encuestasdisponibles;
    ConexionWeb cw;
    Button btnSubirServer;
    String encuesta_seleccionada;
    ConexionBD conexion;
    int tipo_usuario;//Variable para saber que tipo de usuario es
    String Usuario_Logeado;
    String[] respuestas;
    String reg="";
    VerificarConexionWIFI verificadorConexion;
    //ConexionBD bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuestas);
        encuestasdisponibles=(ListView)findViewById(R.id.encuestas_disponibles);
        btnSubirServer=(Button) findViewById(R.id.subirServer);
        verificadorConexion=new VerificarConexionWIFI(this);
        encuesta_seleccionada="";
        conexion=new ConexionBD(this,"Poll-oB2",null,1);
        //bd=new ConexionBD(this,"Poll-oB2",null,1);
        tipo_usuario=Integer.parseInt(getIntent().getStringExtra("Tipo"));
        Usuario_Logeado=getIntent().getStringExtra("Usuario");

        //Toast.makeText(Encuestas.this, ""+itemsEncuesta.size(), Toast.LENGTH_SHORT).show();
        consultaRespuestas();
        ArrayList <Encuesta_detalle> itemsEncuesta=obtenerItems();
        final ItemCompraAdapter Adapter= new ItemCompraAdapter(this,itemsEncuesta);
        encuestasdisponibles.setAdapter(Adapter);
        if(tipo_usuario==1){
            btnSubirServer.setVisibility(View.INVISIBLE);
        }
        else{
            btnSubirServer.setVisibility(View.VISIBLE);
        }
        btnSubirServer.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
              try{

                  for(int i = 0; i<respuestas.length;i++) {
                      cw = new ConexionWeb(Encuestas.this);
                      respuestas = reg.split("-o-");
                      cw.agregarVariables("respuestas", respuestas[i]);
                      cw.agregarVariables("operacion", "subir_todas_respuestas");
                      URL url = new URL("http://poll-o.ueuo.com/basededatos.php");
                      cw.execute(url);
                      //Toast.makeText(Encuestas.this,"xfsdfsdf: "+respuestas[0],Toast.LENGTH_LONG).show();
                  }
                }
                catch (MalformedURLException e){
                    AlertDialog.Builder alertaMesta = new  AlertDialog.Builder(Encuestas.this);
                    alertaMesta.setTitle("Error");
                    alertaMesta.setMessage(e.getMessage());
                    alertaMesta.show();
                }

            }
        });

        //cargarEncuestas();

        encuestasdisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                encuesta_seleccionada= Adapter.getItemId(position)+"-"+Adapter.getItemCompania(position);
                //encuesta_seleccionada = encuestasdisponibles.getItemAtPosition(position).toString();

                Intent PantallaMenuEncuesta = new Intent(Encuestas.this, MenuEncuesta.class);
                PantallaMenuEncuesta.putExtra("seleccion",encuesta_seleccionada);
                PantallaMenuEncuesta.putExtra("tipo",tipo_usuario+"");
                startActivity(PantallaMenuEncuesta);

            }
        });

    };
    private ArrayList<Encuesta_detalle> obtenerItems() {
        ArrayList<Encuesta_detalle> lables=conexion.obtenerEnc();
        Toast.makeText(Encuestas.this, lables.size()+" ", Toast.LENGTH_SHORT).show();
        return lables;
    }
    @Override protected void onStart() {
        super.onStart();
        ///Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        ArrayList <Encuesta_detalle> itemsEncuesta=obtenerItems();
        final ItemCompraAdapter Adapter= new ItemCompraAdapter(this,itemsEncuesta);
        encuestasdisponibles.setAdapter(Adapter);
    }
    public boolean onCreateOptionsMenu(Menu m){
        //cuando se crea el menu contextual
        this.getMenuInflater().inflate(R.menu.menu_login, m);
        return super.onCreateOptionsMenu(m);
    }
    public boolean onOptionsItemSelected(MenuItem mi){
        //se ejecuta cuanso se toca on item del menu conceptual
        switch (mi.getItemId()){
            case R.id.actualiza:
                ArrayList <Encuesta_detalle> itemsEncuesta=obtenerItems();
                final ItemCompraAdapter Adapter= new ItemCompraAdapter(this,itemsEncuesta);
                encuestasdisponibles.setAdapter(Adapter);

                break;

            default:
        }
        return true;
    }
    public void consultaRespuestas() {
        try {

            SQLiteDatabase base = conexion.getReadableDatabase();
            String SQL = "Select*from rrespuestas";
            Cursor res = base.rawQuery(SQL, null);

            if (res.moveToFirst()) {
                do {
                    reg = reg +"("+res.getString(1) + ",'" + res.getString(2)+"')-o-";
                } while (res.moveToNext());
                respuestas = reg.split("-o-");
                Toast.makeText(this,"Res: "+reg,Toast.LENGTH_LONG).show();



            } else {
                Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_LONG).show();
            }

            base.close();
        }
        catch (SQLiteException e) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("Error").setTitle(e.getMessage()).show();
        }
    }
    public  void mostrarREspuesta(String res){
        Toast.makeText(Encuestas.this,res, Toast.LENGTH_LONG).show();
            if (res.contains("si")) {
                Toast.makeText(Encuestas.this, "Almacenado en servidor exitosamente", Toast.LENGTH_LONG).show();
                eliminarRespuestas();
            }
            if (res.contains("no")) {
                //Toast.makeText(Encuestas.this, "No se pudo almacenar en servidor", Toast.LENGTH_LONG).show();
            }
    }
    public void eliminarRespuestas() {
        try {

            SQLiteDatabase base = conexion.getWritableDatabase();
            String SQL = "delete from rrespuestas";
            base.execSQL(SQL);
            base.close();
        }
        catch (SQLiteException e) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("Error").setTitle(e.getMessage()).show();
        }
    }

    public void mostrar(String resultado){

        Toast.makeText(this,resultado,Toast.LENGTH_LONG).show();

        if(resultado.startsWith("Error_404_1")){
            resultado="Hosting no encontrado";
        }
        if(resultado.startsWith("Error_404_2")){
            resultado="Error al ENVIAR/RECIBIR informacion con el PHP";
        }
        if(resultado.startsWith("Error_404")){
            resultado="Al parecer no existe el PHP buscado";
        }

        if(resultado.equals("si")){
            Toast.makeText(this,"Si se pudo",Toast.LENGTH_LONG).show();
        }
        if(resultado.equals("no")){
            Toast.makeText(this,"No se pudo",Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this,resultado,Toast.LENGTH_LONG).show();
    }
    private void cargarEncuestas() {

        List<String> lables = conexion.obtenerEncuestas();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        encuestasdisponibles.setAdapter(dataAdapter);
    }
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        if (verificadorConexion.estaConectado() && tipo_usuario==1) {
            //CODIGO LOGEARTE CON INTERNET
            Cerrar_Sesion(Usuario_Logeado);

        } else {
            //AQUI DEBE IR EL CODIGO PARA MANDAR UN MJS AL SERVIDOR SI NO HAY INTERNET
        }
    }
    private void Cerrar_Sesion(String usuario) {
        try {
            ConexionWeb web = new ConexionWeb(Encuestas.this,0);
            web.agregarVariables("operacion", "logout");
            web.agregarVariables("celular",usuario);
            URL url = new URL("http://poll-o.ueuo.com/basededatos.php");
            web.execute(url);
           // Toast.makeText(this, "cerrar sesion", Toast.LENGTH_SHORT).show();

        }catch (MalformedURLException e) {
            new AlertDialog.Builder(Encuestas.this).setMessage
                    (e.getMessage()).setTitle("Error").show();

        }
    }
    public void sesion_cerrada(String Respuesta) {
        if(Respuesta.contains("si")){
            Toast.makeText(Encuestas.this, "Sesion cerrada", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Encuestas.this, "no se pudo", Toast.LENGTH_LONG).show();
        }
    }

}