package mx.edu.ittepic.poll_o;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ConexionBD extends SQLiteOpenHelper{

    public ConexionBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sql) {

//        sql.execSQL("delete from respuestas");

        sql.execSQL("CREATE TABLE Usuario (celular VARCHAR(10) PRIMARY KEY ,nombre VARCHAR(100),tipo VARCHAR(10),domicilio VARCHAR(255), password varchar(15))");

        sql.execSQL("CREATE TABLE Encuesta (idencuesta INTEGER PRIMARY KEY,compania VARCHAR(50), nombre VARCHAR(50), estado VARCHAR(15),fecha_expiracion DATE, cantidad INTEGER)");

        sql.execSQL("CREATE TABLE Empleado_Encuesta (fk_celular VARCHAR(10),fk_idencuesta INTEGER," +
                "    FOREIGN KEY (fk_celular) REFERENCES Usuario (celular), FOREIGN KEY (fk_idencuesta) REFERENCES Encuesta (idencuesta))");

        sql.execSQL("CREATE TABLE Pregunta (idpregunta INTEGER ,fk_idencuesta INTEGER,pregunta VARCHAR(255),tipo VARCHAR(15), respuestas VARCHAR(1000)," +
                "   FOREIGN KEY (fk_idencuesta) REFERENCES Encuesta (idencuesta) )");


        sql.execSQL("CREATE TABLE Respuestas (idrespuesta INTEGER PRIMARY KEY AUTOINCREMENT,fk_idpregunta INTEGER,valor VARCHAR (10000)," +
                "   FOREIGN KEY (fk_idpregunta) REFERENCES Pregunta (idpregunta) )");

        //Usuarios

        sql.execSQL("delete from respuestas ");

        //sql.execSQL("insert into usuario values('3111126818','Armando','encuestador','Calle Falsa #123','hola123')");
        //sql.execSQL("insert into usuario values('3111234567','Paloma','encuestador','Calle Libano #507','hola123')");
        //Encuesta
        //sql.execSQL("insert into Encuesta values(1,'Coca-Cola','Sabor que mas te gusta','Aprobada','2016-06-24',80)");
        // sql.execSQL("insert into Encuesta values(2,'Chivas','Refuerzo que te gustaria','Aprobada','2016-06-12',50)");
        //Empleado_encuesta
        // sql.execSQL("insert into Empleado_Encuesta values('3111126818',1)");
        // sql.execSQL("insert into Empleado_Encuesta values('3111234567',2)");
        //Pregunta
        //sql.execSQL("insert into Pregunta values(1,1,'¿Que tamaño de refresco consume regularmente?','opcion multiple','Botella 2 litros')");
        //sql.execSQL("insert into Pregunta values(2,1,'¿Que refresco es el que consume mas?','opcion multiple','Coca-Cola')");

        //sql.execSQL("insert into Pregunta values(10,2,'¿Qué horario prefiere para ver los partidos?','opcion multiple','21:00 pm')");
        //sql.execSQL("insert into Pregunta values(11,2,'¿Qué dia prefiere para ver los partidos?','opcion multiple','Sabado')");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public List<String> obtenerPreguntas(int id){
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM Pregunta where fk_idencuesta = "+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0)+"-"+cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return labels;
    }

    public ArrayList<Respuestas> obtenerRespuestas(int id){
        ArrayList<Respuestas> labels = new ArrayList<Respuestas>();
        String selectQuery = "SELECT valor FROM Respuestas WHERE fk_idpregunta= " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Respuestas respuestas = new Respuestas();
                respuestas.setNombre(cursor.getString(0));
                String Sql_No_registros = "SELECT COUNT( valor ) AS total FROM Respuestas  WHERE valor ='" + cursor.getString(0)+"'";
                Cursor cursor1 = db.rawQuery(Sql_No_registros, null);
                cursor1.moveToFirst();
                int total = cursor1.getInt(cursor1.getColumnIndexOrThrow("total"));
                respuestas.setValor(total);
                labels.add(respuestas);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return labels;

    }


    public List<String> obtenerEncuestas(){
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM Encuesta";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0)+"-"+cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return labels;
    }
    public ArrayList<Encuesta_detalle> obtenerEnc(){
        ArrayList<Encuesta_detalle> labels = new ArrayList<Encuesta_detalle>();
        String selectQuery = "SELECT  * FROM Encuesta";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Encuesta_detalle encuestini = new Encuesta_detalle();
                encuestini.setId(Integer.parseInt(cursor.getString(0)));
                encuestini.setCompania(cursor.getString(1));
                encuestini.setNombre(cursor.getString(2));
                labels.add(encuestini);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return labels;

    }

    public float getTotalRespuestas(int id) {
        String selectQuery="SELECT COUNT(valor) AS total FROM Respuestas WHERE fk_idpregunta= "+id;
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor= db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
        cursor.close();
        db.close();
        return total;
    }
}