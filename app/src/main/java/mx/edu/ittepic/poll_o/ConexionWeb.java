package mx.edu.ittepic.poll_o;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConexionWeb extends AsyncTask<URL,String,String> {
    List<String[]> variables;
    MenuPrincipal form;
    Pantalla_Login form_login;
    boolean error;
    Encuestas form_Encuestas;
    Inicio form_ini;
    int operacion; //0 es cerrar sesion, 1 Actualizar base de datos

    public ConexionWeb(Inicio f){
        variables = new ArrayList<String[]>();
        form_login = null;
        form=null;
        error = true;
        form_Encuestas=null;
        form_ini=f;
    }
    public ConexionWeb(MenuPrincipal f,int n){
        variables = new ArrayList<String[]>();
        form = f;
        operacion=n;
        form_login=null;
        error = true;
        form_Encuestas=null;
    }
    public ConexionWeb(Pantalla_Login f,int operac){
        variables = new ArrayList<String[]>();
        operacion=operac;
        form_login = f;
        error = true;
        form=null;
        form_Encuestas=null;
    }
    public ConexionWeb(Encuestas f){
        variables = new ArrayList<String[]>();
        form_login = null;
        error = true;
        form=null;
        form_Encuestas=f;
    }
    public ConexionWeb(Encuestas f, int valor){
        variables = new ArrayList<String[]>();
        form_login = null;
        error = true;
        form=null;
        form_Encuestas=f;
        operacion=valor;
    }
    public void agregarVariables(String id, String dato){
        String[] temp = {id, dato};
        variables.add(temp);
    }
    @Override
    protected String doInBackground(URL... params) {
        HttpURLConnection conexion = null;
        String datosPost = "";
        String respuesta="";

        try{

            for(int i=0; i<variables.size(); i++){
                datosPost += URLEncoder.encode(variables.get(i)[0], "UTF-8") +"="+ URLEncoder.encode(variables.get(i)[1],"UTF-8")+" ";
            }

            datosPost = datosPost.trim(); //Quita espacios antes y despues de la cadena
            datosPost = datosPost.replaceAll(" ", "&"); //Deja un solo espacio entre palabras
            //datosPost = datosPost.substring(0, datosPost.length()-2);


            // Establecer la conexion
            conexion = (HttpURLConnection) params[0].openConnection();
            conexion.setDoOutput(true);
            conexion.setFixedLengthStreamingMode(datosPost.length());
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //Flujo de salida de datos
            OutputStream out = new BufferedOutputStream(conexion.getOutputStream());
            out.write(datosPost.getBytes());
            out.flush();
            out.close();

            if(conexion.getResponseCode()==200){ //Si el recibio los datos enviados y proceso respuesta
                //Toast.makeText(ConexionWeb.this,"Entro al gerResponse",Toast.LENGTH_LONG).show();
                //respuesta ="entro a esa wea";

                InputStreamReader in = new InputStreamReader(conexion.getInputStream(), "UTF-8");
                BufferedReader lector = new BufferedReader(in);

                String linea = "";
                do{
                    linea = lector.readLine();

                    if(linea!=null){
                        respuesta += linea;
                    }
                }while (linea!=null);
            }
            else{
                respuesta="Error_404";
            }
        }catch(UnknownHostException uhe){
            return "Error_404_1";
            //publishProgress("Error:"+uhe.getMessage());
        }catch(IOException ioe){
            return "Error_404_2 "+ioe.getMessage();
            //publishProgress("Error:"+ioe.getMessage());
        }finally {
            if(conexion!=null){
                conexion.disconnect();
                //publishProgress("Desconectado");
            }
        }


        return respuesta;
    }

    protected void onPostExecute(String res){

        if(form_ini!=null) {
            //Toast.makeText(form,res,Toast.LENGTH_SHORT).show();
            String SQL = "";
            if (res.contains("=") && res.contains(";")) {

                String[] filas = res.split(";");
                String[] columnas;

                for (int i = 0; i < filas.length; i++) {

                    columnas = filas[i].split("=");
                    // Toast.makeText(form, columnas.length + "", Toast.LENGTH_SHORT).show();


                    switch (columnas.length) {
                        case 2:
                            //Tabla Empleado encuesta
                            SQL = "INSERT INTO Empleado_Encuesta (fk_celular,fk_idencuesta) VALUES ('" + columnas[0] + "'," + columnas[1] + ");";
                            break;
                        case 3:
                            //Tabla Respuestas
                            SQL = "INSERT INTO Respuestas (fk_idpregunta,valor) VALUES (" + columnas[1] + ",'" + columnas[2] + "');";
                            //Toast.makeText(form,SQL,Toast.LENGTH_SHORT).show();
                            break;
                        case 5:
                            //Tabla pregunta
                            SQL = "INSERT INTO Pregunta (idpregunta,fk_idencuesta,pregunta,tipo,respuestas) VALUES (" + columnas[0] + "," + columnas[1] + ",'" + columnas[2] + "','" + columnas[3] + "','" + columnas[4] + "');";
                            break;
                        case 6:
                            //ENcuestas
                            SQL = "INSERT INTO Encuesta (idencuesta,compania,nombre,estado,fecha_expiracion, cantidad) VALUES (" + columnas[0] + ",'" + columnas[1] + "'" +
                                    ",'" + columnas[2] + "','" + columnas[3] + "','" + columnas[4] + "'," + columnas[5] + ");";
                            break;

                        default:
                            //Toast.makeText(form_ini, "No encontrado: COLUMNAS: "+columnas.length, Toast.LENGTH_SHORT).show();
                            error = false;
                            break;
                    }
                    if(error){
                        form_ini.InsertarEnBaseDeDatos(SQL);
                    }


                }
                //Toast.makeText(form,SQL,Toast.LENGTH_SHORT).show();


            } else {

            }
        }
        if(form!=null && operacion==1) {
            //Toast.makeText(form,res,Toast.LENGTH_SHORT).show();
            String SQL = "";
            if (res.contains("=") && res.contains(";")) {

                String[] filas = res.split(";");
                String[] columnas;

                for (int i = 0; i < filas.length; i++) {

                    columnas = filas[i].split("=");
                   // Toast.makeText(form, columnas.length + "", Toast.LENGTH_SHORT).show();


                    switch (columnas.length) {
                        case 2:
                            //Tabla Empleado encuesta
                            SQL = "INSERT INTO Empleado_Encuesta (fk_celular,fk_idencuesta) VALUES ('" + columnas[0] + "'," + columnas[1] + ");";
                            break;
                        case 3:
                            //Tabla Respuestas
                            SQL = "INSERT INTO Respuestas (fk_idpregunta,valor) VALUES (" + columnas[1] + ",'" + columnas[2] + "');";
                            //Toast.makeText(form,SQL,Toast.LENGTH_SHORT).show();
                            break;
                        case 5:
                            //Tabla pregunta
                            SQL = "INSERT INTO Pregunta (idpregunta,fk_idencuesta,pregunta,tipo,respuestas) VALUES (" + columnas[0] + "," + columnas[1] + ",'" + columnas[2] + "','" + columnas[3] + "','" + columnas[4] + "');";
                            break;
                        case 6:
                            //ENcuestas
                            SQL = "INSERT INTO Encuesta (idencuesta,compania,nombre,estado,fecha_expiracion, cantidad) VALUES (" + columnas[0] + ",'" + columnas[1] + "'" +
                                    ",'" + columnas[2] + "','" + columnas[3] + "','" + columnas[4] + "'," + columnas[5] + ");";
                            break;

                        default:
                            //Toast.makeText(form, "No encontrado: COLUMNAS: "+columnas.length, Toast.LENGTH_SHORT).show();
                            error=false;
                            break;
                    }
                    if(error){
                        form.InsertarEnBaseDeDatos(SQL);
                    }

                }
                //Toast.makeText(form,SQL,Toast.LENGTH_SHORT).show();

            } else {

            }
        }
        if(form!=null && operacion==0) {
            //Toast.makeText(form,res,Toast.LENGTH_SHORT).show();
            form.sesion_cerrada(res);
        }
        if (form_login!=null && operacion==0){
            //Toast.makeText(form_login,res,Toast.LENGTH_SHORT).show();
            form_login.sesion_correcta(res);
        }
        if (form_Encuestas!=null && operacion!=1){
            //Toast.makeText(form_login,res,Toast.LENGTH_SHORT).show();
            form_Encuestas.mostrarREspuesta(res);
        }
        if (form_Encuestas!=null && operacion==1){
            //Toast.makeText(form_login,res,Toast.LENGTH_SHORT).show();
            form_Encuestas.sesion_cerrada(res);

        }
        if (form_login!=null && operacion==1){
            //Toast.makeText(form_login,res,Toast.LENGTH_SHORT).show();
            String SQL = "";
            if (res.contains("=") && res.contains(";")) {

                String[] filas = res.split(";");
                String[] columnas;

                for (int i = 0; i < filas.length; i++) {

                    columnas = filas[i].split("=");
                    //Toast.makeText(form_login, columnas.length + "", Toast.LENGTH_SHORT).show();


                    switch (columnas.length) {
                        case 6:
                            //ENcuestas
                            SQL = "INSERT INTO Encuesta (idencuesta,compania,nombre,estado,fecha_expiracion, cantidad) VALUES (" + columnas[0] + ",'" + columnas[1] + "'" +
                                    ",'" + columnas[2] + "','" + columnas[3] + "','" + columnas[4] + "'," + columnas[5] + ");";
                            break;
                    }
                    form_login.InsertarEnBaseDeDatos(SQL);
                }
                //Toast.makeText(form_login,SQL,Toast.LENGTH_SHORT).show();

            } else {

            }
        }
    }
}
