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
    Bitmap letras, logo;
    int alfa;
    Lienzo lienzo;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lienzo=new Lienzo(this);
        setContentView(lienzo);
        alfa=0;

        tamanoPantalla = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(tamanoPantalla);

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
                c.drawBitmap(logo,tamanoPantalla.x/2-logo.getWidth()/2,tamanoPantalla.y/4+letras.getHeight(),p);
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
            sleep=50;
        }
        public void run(){
            Canvas canvas=null;
            while(repetir){
                if(sleep==1500){
                    repetir=false;
                }
                //Aquí 2do plano
                try{
                    try {
                        sleep(sleep);
                        if(aumentoAlfa){
                            alfa+=5;
                        }else{
                            logo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logopollo), tamanoPantalla.x / 3, tamanoPantalla.x / 3, false);
                            sleep=1500;
                        }
                        if(alfa>=255){
                            aumentoAlfa=false;
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
