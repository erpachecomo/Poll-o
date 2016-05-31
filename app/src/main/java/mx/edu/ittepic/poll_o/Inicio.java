package mx.edu.ittepic.poll_o;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.ImageView;

public class Inicio extends AppCompatActivity {

    Point tamanoPantalla;
    Bitmap letras, logo,pollito;
    int alfa;
    float progreso,tope,aumento;
    Lienzo lienzo;
    /*ACTUALIZAR AUTOMATICAMENTE SI HAY INTERNET*/
    ConexionBD conexion;
    VerificarConexionWIFI verificadorConexion;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lienzo=new Lienzo(this);
        setContentView(lienzo);
        alfa=0;
        /*VARIABLES PARA ACTUALIZAR BASE DE DATOS*/
        conexion=new ConexionBD(this,"Poll-oB2",null,1);
        verificadorConexion=new VerificarConexionWIFI(this);

        /*--------------------------------*/
        tamanoPantalla = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(tamanoPantalla);
        tope = 2*tamanoPantalla.x/3-100;
        aumento=tope/400;
        progreso=0;
        pollito = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.pollito1),100,100,false);
        logo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logosinguino),tamanoPantalla.x / 3,tamanoPantalla.x / 3,false);
        Bitmap letraspollo=BitmapFactory.decodeResource(getResources(), R.drawable.letraspollo);
        letras = Bitmap.createScaledBitmap(letraspollo,letraspollo.getWidth() /3,letraspollo.getHeight()/3,false);

    }

    public class Lienzo extends SurfaceView implements SurfaceHolder.Callback{
        Hilo h;
        public Lienzo(Context c){
            super(c);
            getHolder().addCallback(this);
        }

        public boolean onTouchEvent(MotionEvent e){
            return true;
        }

        public void onDraw(Canvas c) {
            try{
                Paint p = new Paint();
                c.drawColor(Color.WHITE);
                p.setAlpha(alfa);
                c.drawBitmap(letras, tamanoPantalla.x / 2 - letras.getWidth() / 2, tamanoPantalla.y / 5, p);
                p.setAlpha(255);
                c.drawBitmap(logo, tamanoPantalla.x / 2 - logo.getWidth() / 2, tamanoPantalla.y / 4 + letras.getHeight(), p);
                Paint p2 = new Paint();
                p2.setColor(Color.rgb(121, 179, 225));
                p2.setStyle(Paint.Style.FILL);
                c.drawCircle(tamanoPantalla.x / 6 + 50, tamanoPantalla.y / 4 + letras.getHeight() + logo.getHeight() + 20 + 50, 50, p2);


                //c.drawCircle(tamanoPantalla.x/6+50+progreso,tamanoPantalla.y / 4 + letras.getHeight() + logo.getHeight() + 20+50,50,p2);
                c.drawRect(tamanoPantalla.x / 6+50, tamanoPantalla.y / 4 + letras.getHeight() + logo.getHeight() + 20, tamanoPantalla.x / 6 + progreso+50, tamanoPantalla.y / 4 + letras.getHeight() + logo.getHeight()+120,p2);
                c.drawBitmap(pollito,tamanoPantalla.x/6+progreso,tamanoPantalla.y / 4 + letras.getHeight() + logo.getHeight() + 20,p);
            }catch(Exception e){}
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            h = new Hilo(this);
            h.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            h.repetir=false;
            try {
                h.join();
            } catch (InterruptedException e) {

            }
        }

    }
    public class Hilo extends Thread{
        boolean repetir;
        Lienzo puntero;
        int sleep;
        boolean aumentoAlfa;
        public Hilo(Lienzo ref){
            repetir=true;
            aumentoAlfa=true;
            puntero=ref;
            sleep=1; //modificar este a 10
        }
        public void run(){
            Canvas canvas=null;
            while(repetir){
                if(sleep==1000){
                    repetir=false;
                }
                //Aquí 2do plano
                try{
                    try {
                        sleep(sleep);
                        if(aumentoAlfa){
                            alfa+=1;
                            progreso+=aumento;
                        }else{
                            logo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logopollo), tamanoPantalla.x / 3, tamanoPantalla.x / 3, false);
                            sleep=1000;
                        }
                        if(alfa>=255){
                            if(progreso>=aumento*400){
                                aumentoAlfa=false;
                            }
                            alfa=255;
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    canvas = puntero.getHolder().lockCanvas();
                    synchronized (puntero.getHolder()){
                        puntero.onDraw(canvas);
                    }
                }finally{
                    //Se ejecuta cuando el recurso se libera
                    if(canvas!=null) {
                        puntero.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
            }
            puntero.getContext().startActivity(new Intent(Inicio.this,Pantalla_Login.class));
        }
    }

}
