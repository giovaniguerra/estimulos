package br.com.estimulos.app.ui.configuracao;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.dominio.Imagem;

public class ConfImagensEstimulo extends AppCompatActivity implements View.OnClickListener{

    private ImageView imagemPrincipal;
    private ImageView mascara;
    private ImageView resultado;
    private int operacaoTela;
    private Uri URIImage;
    private Imagem imagem;
    private boolean flgMascara;
    private Button btAddImgPrincipal;
    private Button btAddMascara;
    private Button btSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_imagens_estimulo);

        imagemPrincipal = (ImageView) findViewById(R.id.imgPrincipal);
        mascara = (ImageView) findViewById(R.id.imgMascara);
        resultado = (ImageView) findViewById(R.id.imgResult);

        btAddImgPrincipal = (Button) findViewById(R.id.btAddImagemPrincipal);
        btAddMascara = (Button) findViewById(R.id.btAddMascara);
        btSalvar = (Button) findViewById(R.id.btResult);

        btAddImgPrincipal.setOnClickListener(this);
        btAddMascara.setOnClickListener(this);
        btSalvar.setOnClickListener(this);

        operacaoTela = getIntent().getIntExtra(MenuJogosFragment.OPERACAO_JOGO, 0);

        /**
         * Verifica a operacao a ser realizada
         */
        if(operacaoTela == MenuEstimulosFragment.Operacoes.CADASTRO.getOperacao()){
            // Instancia um novo objeto de estimulo para o cadastro
            imagem = new Imagem();
            imagem.setNome("");
            imagem.setAltura(0);
            imagem.setLargura(0);
        }
        else if(operacaoTela == MenuJogosFragment.Operacoes.EDITAR.getOperacao()) {
            /**
             * Recupera os todos os dados do Jogo
             */
            imagem = (Imagem) getIntent().getSerializableExtra(ConfDadosEstimuloActivity.IMAGEM_TAG);
            setarImagens();
        }
    }

    private void setarImagens() {
        if(imagem.getUri() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(imagem.getUri(), options);
            imagemPrincipal.setImageBitmap(bm);
        }
        if(imagem.getUriMascara() != null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(imagem.getUriMascara(), options);
            mascara.setImageBitmap(bm);
            mostrarResultado(imagemPrincipal, mascara);
        }

    }

    @Override
    public void onClick(View v) {
        if(v == btAddImgPrincipal){
            flgMascara = false;
            abrirIntencaoImagem();
        }
        else if(v == btAddMascara){
            flgMascara = true;
            abrirIntencaoImagem();

        }
        else if(v == btSalvar){
            if(!imagem.getUriMascara().equals("") && !imagem.getUri().equals("")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(ConfDadosEstimuloActivity.IMAGEM_TAG, imagem);
                setResult(RESULT_OK, returnIntent);
                finish();
                super.onBackPressed();
            }
            else{
                Toast.makeText(this, "Insira as imagens", Toast.LENGTH_LONG);
            }
        }
    }

    private void mostrarResultado(ImageView viewOriginal, ImageView mascara){
        // Criar a copia com a area da mascara << Loop linha x colunas (X,Y)
        Bitmap bitmapOriginal = ((BitmapDrawable)viewOriginal.getDrawable()).getBitmap();
        Bitmap bitmapMask = ((BitmapDrawable)mascara.getDrawable()).getBitmap();
        // instancia de um novo bitmap com as mesmas dimensoes do bitmap original
        Bitmap novoBitmap = Bitmap.createBitmap(
                bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        // canvas para desenhar dentro do novo bitmap
        Canvas canvas = new Canvas(novoBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        // paint para escolher as cores do pincel
        Paint paint = new Paint();
        // guardar largura e altura para loop (linhas e colunas)
        int l = bitmapOriginal.getWidth();
        int c = bitmapOriginal.getHeight();
        // for para percorrer as linhas do bitmap
        for(int i = 0; i < l - 1; i++){
            // for para percorrer as colunas do bitmap
            for(int j = 0; j < c - 1; j++){
                // encontrou ponto preto na mascara?
                if(bitmapMask.getPixel(i, j) == Color.BLACK){
                    // recuperar cor no bitmapOriginal e colocar no pincel
                    paint.setColor(bitmapOriginal.getPixel(i, j));
                    // pintar o pixel com a cor original com o canvas
                    canvas.drawPoint(i, j, paint);
                }
            } // fim J
        } // fim I

        resultado.setImageBitmap(novoBitmap);
    }



    /** Cria a intent e salva a imagem na pasta de fotos do Estimulo
     *  requer as permissoes da camera e SD para funcionar propriamente, add no manifest
     *  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     *  <uses-feature android:name="android.hardware.camera" />
     *  <uses-permission android:name="android.permission.CAMERA" />
     */
    private void abrirIntencaoImagem() {
        // TODO Auto-generated method stub
        // testar o estado do cartao SD antes com .. if(Environment.getExternalStorageState())

        final File imageDirectory = new File(										// para receber o local publico para salvar as imagens usadas no Estimulos
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Estimulos");
        imageDirectory.mkdirs();													// cria todas as pastas nao existentes neste caminho
        // criar a formatacao do nome da foto
        String imageName = "Image_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                + ".jpg";
        final File imageFile = new File(imageDirectory, imageName); 				// cria o arquivo indicando o diretorio e seu nome
        URIImage = Uri.fromFile(imageFile);

        // atribui a referencia do arquivo ao Uri
        // criar a lista de intencoes para escolher a origem da imagem
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);	// criar a intencao de tirar uma foto da camera
        final PackageManager packageManager = getPackageManager(); // ???
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0); // pegar a lista de apps que aceitam esta Intent
        for(ResolveInfo res: listCam){
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);						// criar uma intencao de tirar uma foto da camera
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name)); // especificar o componente que tratara esta intent
            intent.setPackage(packageName); 										// atribuir o nome do pacote que cuidara desta intent
            intent.putExtra(MediaStore.EXTRA_OUTPUT, URIImage);						// adicionar a intencao onde deve ser retornado o Uri
            cameraIntents.add(intent);												// adicionar esta intencao a lista de intencoes da camera
        }
        // criar a intencao de deixar o usuario escolher de onde ele quer pegar a foto
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");											// pegar apenas o que for do tipo imagem
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);							// criar a intencao para a escolha do usuario
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Escolha a fonte");
        // adicionar a opcao a camera
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{})); // ????

        if(imagem == null)
            imagem = new Imagem();
        if(flgMascara)
            imagem.setUriMascara(URIImage.getPath());
        else
            imagem.setUri(URIImage.getPath());

        startActivityForResult(chooserIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){												// pegou alguma imagem?
            if(requestCode == 1) {													// ha algum resultado?
                final boolean isCamera;
                if (data == null){
                    isCamera = true;
                } else{
                    final String action = data.getAction();
                    if(action == null){
                        isCamera = false;
                    } else {
                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                Uri selectedImageUri;
                if(isCamera){
                    selectedImageUri = URIImage;


                    if(flgMascara)
                        imagem.setUriMascara(URIImage.getPath());
                    else
                        imagem.setUri(URIImage.getPath());

                    Bitmap bm = null;
                    bm = BitmapFactory.decodeFile(URIImage.getPath());

                    if(flgMascara)
                        mascara.setImageBitmap(bm);
                    else
                        imagemPrincipal.setImageBitmap(bm);
                } else{
                    selectedImageUri = data == null ? null : data.getData();
                    if(selectedImageUri != null){


                        if(flgMascara)
                            imagem.setUriMascara(getRealPathFromURI(selectedImageUri));
                        else
                            imagem.setUri(getRealPathFromURI(selectedImageUri));


                        Bitmap bm = null;
                        bm = BitmapFactory.decodeFile(getRealPathFromURI(selectedImageUri));

                        if(flgMascara)
                            mascara.setImageBitmap(bm);
                        else
                            imagemPrincipal.setImageBitmap(bm);
                    }
                }
            } else {
                Toast.makeText(this,  "Nenhuma imagem foi selecionada!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else{
            Toast.makeText(this,
                    "Não foi possível acessar o arquivo. Por favor, tente novamente!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Bitmap bm;
        if(flgMascara)
            bm = BitmapFactory.decodeFile(imagem.getUriMascara(), options);
        else
            bm = BitmapFactory.decodeFile(imagem.getUri(), options);

        if(flgMascara) {
            mascara.setImageBitmap(bm);
            mostrarResultado(imagemPrincipal, mascara);
        }
        else{
            imagemPrincipal.setImageBitmap(bm);
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
