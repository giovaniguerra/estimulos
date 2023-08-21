package br.com.estimulos.app.ui.configuracao;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.ReforcadorDAO;
import br.com.estimulos.app.ui.jogo.ReforcadorStartActivity;
import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.Reforcador;
import br.com.estimulos.dominio.Video;

public class ConfReforcadorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener{

    private SeekBar seekBar;
    private int tempoDuracao;
    private TextView segundos;
    private EditText nome;
    // Armazena a enumeracao da operacao feita na tela
    private int operacaoTela;
    private Reforcador reforcador;
    private Button btProximo;
    private Button btPlay;
    private ReforcadorDAO reforcadorDAO;
    private ImageView midia;
    private Uri URIMidia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_reforcador);

        reforcadorDAO = new ReforcadorDAO(BancoDadosHelper.getInstance(this));

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        segundos = (TextView) findViewById(R.id.tvSegundos);
        nome = (EditText) findViewById(R.id.etNomeReforcador);
        btProximo = (Button) findViewById(R.id.btProximo);
        midia = (ImageView) findViewById(R.id.imageMidia);
        btPlay = (Button) findViewById(R.id.btPlay);

        btPlay.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        midia.setOnClickListener(this);
        btProximo.setOnClickListener(this);

        operacaoTela = getIntent().getIntExtra(MenuJogosFragment.OPERACAO_JOGO, 0);

        /**
         * Verifica a operacao a ser realizada
         */
        if(operacaoTela == MenuEstimulosFragment.Operacoes.CADASTRO.getOperacao()){
            reforcador = new Reforcador();
            btProximo.setBackgroundResource(R.drawable.bt_salvar);
            tempoDuracao = 20;
            reforcador.setTempoDuracao(tempoDuracao * 1000);
        }
        else if(operacaoTela == MenuJogosFragment.Operacoes.EDITAR.getOperacao()) {
            reforcador = (Reforcador) getIntent().getSerializableExtra(MenuReforcadoresFragment.TAG_REFORCADOR);
            btProximo.setBackgroundResource(R.drawable.bt_editar);
            setValores();
        }

    }

    private void setValores() {
        nome.setText(reforcador.getNome());
        seekBar.setProgress(reforcador.getTempoDuracao() / 1000);
        segundos.setText(reforcador.getTempoDuracao()/ 1000 + " Segundos");
    }

    private boolean recuperarDados(){
        if(nome.getText().toString().equals("")){
            Toast.makeText(this, "Informe o nome do Reforçador", Toast.LENGTH_LONG).show();
            return false;
        }else{
            reforcador.setNome(nome.getText().toString());
        }
        if(reforcador.getMidia() != null) {
            if (reforcador.getMidia().getUri().isEmpty()) {
                Toast.makeText(this, "Informe uma mídia", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Informe uma mídia", Toast.LENGTH_LONG).show();
            return false;
        }
        if(tempoDuracao < 1){
            Toast.makeText(this, "Informe a duração", Toast.LENGTH_LONG).show();
            return false;

        } else {
            reforcador.setTempoDuracao(tempoDuracao * 1000);
        }

        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tempoDuracao = progress;

        segundos.setText(tempoDuracao + " Segundos");

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if(v == btProximo){
            if(operacaoTela == MenuEstimulosFragment.Operacoes.CADASTRO.getOperacao()){
                if(recuperarDados()){
                    try {
                        reforcadorDAO.salvar(reforcador);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
                    startActivity(intent);
                }
            }
            else if(operacaoTela == MenuJogosFragment.Operacoes.EDITAR.getOperacao()) {
                if(recuperarDados()){
                    try {
                        reforcadorDAO.alterar(reforcador);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
                    startActivity(intent);
                }
            }
        }
        else if(v == midia){
            abrirIntencaoGaleria();

        }
        else if(v== btPlay){

            if(reforcador.getMidia() != null){

                reforcador.setTempoDuracao(tempoDuracao * 1000);
                Intent intent = new Intent(this, ReforcadorStartActivity.class);
                intent.putExtra(ReforcadorStartActivity.REFORCADOR_TAG, reforcador);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Informe uma mídia", Toast.LENGTH_LONG).show();
            }
        }
    }

    /** Cria a intent e salva a imagem na pasta de fotos do Estimulo
     *  requer as permissoes da camera e SD para funcionar propriamente, add no manifest
     *  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     *  <uses-feature android:name="android.hardware.camera" />
     *  <uses-permission android:name="android.permission.CAMERA" />
     */
    private void abrirIntencaoGaleria() {
        // TODO Auto-generated method stub


        // atribui a referencia do arquivo ao Uri
        // criar a lista de intencoes para escolher a origem da imagem
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);	// criar a intencao de tirar uma foto da camera
        final PackageManager packageManager = getPackageManager(); // ???


        // criar a intencao de deixar o usuario escolher de onde ele quer pegar a foto
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");											// pegar apenas o que for do tipo imagem
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);							// criar a intencao para a escolha do usuario
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Escolha a fonte");
        // adicionar a opcao a camera
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{})); // ????

        startActivityForResult(chooserIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){												// pegou alguma imagem?
            if(requestCode == 1) {													// ha algum resultado?

                Uri selectedImageUri;
                selectedImageUri = data == null ? null : data.getData();

                if(selectedImageUri != null){
                    String uri = getRealPathFromURI(selectedImageUri);

                    if(uri.contains("mp3"))
                        reforcador.setMidia(new Audio());

                    else if(uri.contains("mp4"))
                        reforcador.setMidia(new Video());

                    reforcador.getMidia().setUri(uri);

                }

                midia.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_gallery));

            } else {
                Toast.makeText(this, "Nenhum arquivo foi selecionado!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else{
            Toast.makeText(this, "Não foi possível acessar o arquivo. Por favor, tente novamente!",
                    Toast.LENGTH_LONG).show();
            return;
        }

    }

    /**
     * Metodo para obter o endereco real da imagem com base na URI informada
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI( Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = this.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
