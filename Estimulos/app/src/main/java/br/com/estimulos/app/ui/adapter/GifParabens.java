package br.com.estimulos.app.ui.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.InputStream;

import br.com.estimulos.app.R;

/**
 * Created by Caio Gustavo on 04/05/2016.
 */
public class GifParabens extends ImageView {

    private InputStream gifInputStream;
    private Movie gifMovie;
    private long movieStart;

    public GifParabens(Context context) {
        super(context);
        init(context);
    }

    public GifParabens(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GifParabens(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    public void init(Context context){

        setFocusable(true);
        //noinspection ResourceType
        gifInputStream = context.getResources().openRawResource(R.drawable.parabens);
        gifMovie = Movie.decodeStream(gifInputStream);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);

        final long now = SystemClock.uptimeMillis();

        if(movieStart == 0){
            movieStart = now;
        }

        if(gifMovie != null){
            int dur = gifMovie.duration();
            if(dur ==  0){
                dur = 1000;
            }

            int relTime = (int) ((now - movieStart) % dur);

            gifMovie.setTime(relTime);

            int x = this.getWidth();
            int y = this.getHeight();

            canvas.setDensity(50);

            //TODO: fazer calculo para desenhar no meio da tela
            gifMovie.draw(canvas, (x * 0.2f), (y * 0.1f));

            setScaleType(ScaleType.FIT_START);
            invalidate();
        }
    }
}
