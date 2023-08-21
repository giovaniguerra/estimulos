package br.com.estimulos.app.ui.jogo;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.io.IOException;

import br.com.estimulos.app.R;
import br.com.estimulos.app.ui.adapter.GifAplauso;
import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.Reforcador;
import br.com.estimulos.dominio.Video;

public class ReforcadorStartActivity extends Activity implements View.OnClickListener{

	// tag para passagem de parametro
	public static final String REFORCADOR_TAG = "reforcador";
	// tag para log
	private static final String LOG_TAG = "AudioPrototipo";
	// recebera o nome do arquivo do audio a ser gravado
    private static String mFileName = null;
    // para receber a instancia da Media de player
    private static MediaPlayer   mPlayer = null;
	// executar videos
	private VideoView videoView;
	// reforcador
	private Reforcador reforcador;

	// tempo reproducao
	private long delay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		reforcador = (Reforcador) intent.getSerializableExtra(REFORCADOR_TAG);
		mFileName = reforcador.getMidia().getUri();
		delay = reforcador.getTempoDuracao().longValue();

		if(reforcador.getMidia() instanceof Audio){

			setContentView(R.layout.activity_exibe_gif);
			LinearLayout ll = (LinearLayout) findViewById(R.id.layoutGifs);
			ll.setOnClickListener(this);
			startPlayingAudio();
		}else if(reforcador.getMidia() instanceof Video){

			setContentView(R.layout.activity_exibe_video);
			LinearLayout ll = (LinearLayout) findViewById(R.id.layoutVideos);
			ll.setOnClickListener(this);
			startPlayingVideo();
		}
	}

	private void startPlayingVideo() {

		videoView = (VideoView) findViewById(R.id.video);
		videoView.setVideoURI(Uri.parse(mFileName));
		videoView.start();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				stopPlayingVideo();
			}
		}, delay);
	}

	 private void startPlayingAudio() {
		// recebe uma nova instancia de Media
		mPlayer = new MediaPlayer();
		try {
			// recebe o nome do audio que sera reproduzido
			mPlayer.setDataSource(mFileName);
			// prepara a media
			mPlayer.prepare();
			// executa o audio
			mPlayer.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		 Handler handler = new Handler();
		 handler.postDelayed(new Runnable() {
			 public void run() {
				 stopPlaying();
			 }
		 }, delay);

	}

	private void stopPlayingVideo(){
		if(videoView.isPlaying()) {
			videoView.stopPlayback();
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
			super.onBackPressed();
		}
	}

	private void stopPlaying() {
		// o audio esta sendo executado
		// libera o recurso, parando a reproducao
		if(mPlayer != null){
			mPlayer.release();
			mPlayer = null;
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
			super.onBackPressed();
		}
	}

	public static void playAudio(String uri, long time){

		mFileName = uri;
		// recebe uma nova instancia de Media
		mPlayer = new MediaPlayer();
		try {
			// recebe o nome do audio que sera reproduzido
			mPlayer.setDataSource(mFileName);
			// prepara a media
			mPlayer.prepare();
			// executa o audio
			mPlayer.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if(mPlayer != null) {
					mPlayer.release();
					mPlayer = null;
				}
			}
		}, time);

	}

	@Override
	public void onBackPressed() {

		if(reforcador.getMidia() instanceof Audio){
			stopPlaying();
		}else if(reforcador.getMidia() instanceof Video){
			stopPlayingVideo();
		}
	}


	@Override
	public void onClick(View v) {
		if(reforcador.getMidia() instanceof Audio){
			stopPlaying();
		}else if(reforcador.getMidia() instanceof Video){
			stopPlayingVideo();
		}
	}
}
