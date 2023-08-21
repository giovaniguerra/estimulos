package br.com.estimulos.app.ui.configuracao;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.JogoDAO;
import br.com.estimulos.app.core.dao.NivelArrastarDAO;
import br.com.estimulos.app.core.dao.NivelTocarDAO;
import br.com.estimulos.app.core.factory.DAOFactory;
import br.com.estimulos.app.teste.CriadorRegistrosSQLite;
import br.com.estimulos.dominio.FaseArrastar;
import br.com.estimulos.dominio.FaseTocar;
import br.com.estimulos.dominio.Grupo;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.NivelTocar;
import br.com.estimulos.dominio.Paciente;

/**
 * Esta activity recebe os dados do formulario de cadastro de um Jogo
 *
 * Created by Caio Cruz on 02/03/2016.
 */
public class ConfDadosJogoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener{

    // Objetos de views criados no layout
    private EditText tema;
    private EditText nomePaciente;
    private TextView data;
    private ImageView imgJogo;
    private ImageButton btProximo;

    // Ojeto a ser persistido por esta activity
    private Jogo jogo;

    // URI da imagem selecionada
    private Uri URIImage;

    // DAO para persistir o objeto Jogo no banco de dados
    private JogoDAO jogoDAO;

    // Armazena o id da operacacao realizada na tela
    private int operacaoTela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_jogo);

        /**
         * Instancia o DAO responsavel por persistir o jogo
         */

        jogoDAO = (JogoDAO) DAOFactory.getDAO(this, Jogo.class);

        /**
         * Obtem a referencia dos objetos de Layout
         */
        data = (TextView) findViewById(R.id.tvExibeData);
        tema = (EditText) findViewById(R.id.etTema);
        nomePaciente = (EditText) findViewById(R.id.etPacienteJogo);
        imgJogo = (ImageView) findViewById(R.id.imageJogo);
        btProximo = (ImageButton) findViewById(R.id.btProximo);

        /**
         * Seta os objetos que podem ser clicados
         */
        imgJogo.setOnClickListener(this);
        btProximo.setOnClickListener(this);
        data.setOnClickListener(this);

        /**
         * Recupera as intencoes
         */
        jogo = (Jogo) getIntent().getSerializableExtra(MenuJogosFragment.TAG_JOGO);
        operacaoTela =  getIntent().getIntExtra(MenuJogosFragment.OPERACAO_JOGO, 0);

        /**
         * Verifica a operacao a ser realizada
         */
        if(operacaoTela == MenuJogosFragment.Operacoes.CADASTRO.getOperacao()){
           // Instancia um novo objeto de jogo para o cadastro
            jogo = new Jogo();
            jogo.setImg(new Imagem());
            jogo.setPaciente(new Paciente());

            btProximo.setBackgroundResource(R.drawable.seta_direita);
        }
        else if(operacaoTela == MenuJogosFragment.Operacoes.EDITAR.getOperacao()) {
            // Chama o metodo para setar os dados no layout
            setarValores();
            btProximo.setBackgroundResource(R.drawable.bt_editar);
        }
    }

    @Override
    public void onClick(View v) {
        /**
         * se clicou na imagem
         * vai abrir as opcoes de escolha
         */
        if(v == imgJogo){
            abrirIntencaoImagem();
        }
        /**
         * Se clicou no botao proximo ou salvar
         */
        else if(v == btProximo){
            // Esta cadastrando um novo jogo
            // Deve ir para a proxima tela de escolher estimulos
            if(operacaoTela == MenuJogosFragment.Operacoes.CADASTRO.getOperacao()){
                if(obterValores()){
                    try {
                        jogo.setID(Integer.valueOf(String.valueOf(jogoDAO.salvar(jogo))));
                        CriadorRegistrosSQLite.criarNiveisArrastarPara(this, (NivelArrastarDAO) DAOFactory.getDAO(this, FaseArrastar.class), jogo.getID());
                        CriadorRegistrosSQLite.criarNiveisTocarPara(this, (NivelTocarDAO) DAOFactory.getDAO(this, FaseTocar.class), jogo.getID());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, ConfVinculaEstimulos.class);
                    intent.putExtra(MenuJogosFragment.TAG_JOGO, jogo.getID());
                    intent.putExtra(MenuJogosFragment.OPERACAO_JOGO, operacaoTela);
                    startActivity(intent);
                }

            }
            /**
             * Esta editando um jogo
             * Recupera os novos dados e volta para a tela inicial
             */
            else if(operacaoTela == MenuJogosFragment.Operacoes.EDITAR.getOperacao()) {
                if(obterValores()){
                    try {
                        jogoDAO.alterar(jogo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
                    startActivity(intent);
                }
            }
        }
        /**
         * Clicou na data
         * abre a dialog de input para a data
         */
        else if(v == data) {
            showDatePickerDialog(v);
        }
    }

    /**
     * Metodo que abre o dialog seletor de datas
     */
    public void showDatePickerDialog(View view) {
        DialogFragment picker = new DatePickerFragment();
        picker.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Metodo que recuperar o valor da data inserida
     *
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Process the selected date (month is zero-indexed)
        TextView text = (TextView) findViewById(R.id.tvExibeData);
        String message = String.format(" %d/%d/%d", day, month + 1, year);
        text.setText(message);
    }

    /**
     * Metodo responsavel por obter os dados do objeto
     * e preencher nas views para exibir na tela
     */
    private void setarValores(){
        tema.setText(jogo.getTema());
        nomePaciente.setText(jogo.getPaciente().getNome());


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        data.setText(formatter.format(jogo.getPaciente().getDtNascimento()));

        if(jogo.getImagem() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(jogo.getImagem().getUri(), options);
            imgJogo.setImageBitmap(bm);
        }
    }

    /**
     * Metodo para recuperar os valores do formulario e setar no objeto de Jogo
     * @return - TRUE - todas informacoes foram inseridas
     *         - FALSE - caso algum dados nao seja informado
     */
    private boolean obterValores() {

        // Nao informou o tema do jogo
        if (tema.getText().toString().equals("")) {
            Toast.makeText(this, "Digite um tema para o Jogo", Toast.LENGTH_LONG).show();
            return false;
        } else
            jogo.setTema(tema.getText().toString());

        // Nao informou um nome do paciente
        if (nomePaciente.getText().toString().equals("")){
            Toast.makeText(this, "Digite o nome do Paciente", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            jogo.getPaciente().setNome(nomePaciente.getText().toString());

        // Nao informou uma data de nascimento
        if(data.getText().toString().equals("    /   /  ")){
            Toast.makeText(this, "Informe a data de nascimento do aluno", Toast.LENGTH_LONG).show();
            return false;
        }
        jogo.getPaciente().setDtNascimento(converterStringDate(data.getText().toString()));



        return true;

    }

    private Date converterStringDate(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
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

        if(jogo.getImagem() == null)
            jogo.setImg(new Imagem());
        jogo.getImagem().setUri(URIImage.getPath());
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
                    jogo.getImagem().setUri(URIImage.getPath());
                    Toast.makeText(ConfDadosJogoActivity.this, URIImage.getPath(), Toast.LENGTH_SHORT).show();
                    Bitmap bm = null;
                    bm = BitmapFactory.decodeFile(URIImage.getPath());
                    imgJogo.setImageBitmap(bm);
                } else{
                    selectedImageUri = data == null ? null : data.getData();
                    if(selectedImageUri != null){

                        Toast.makeText(this, selectedImageUri.getPath(), Toast.LENGTH_SHORT).show();
                        jogo.getImagem().setUri(getRealPathFromURI(selectedImageUri));

                        Bitmap bm = null;
                        bm = BitmapFactory.decodeFile(getRealPathFromURI(selectedImageUri));
                        imgJogo.setImageBitmap(bm);
                    }
                }
            } else {
                Toast.makeText(this, "Nenhuma imagem foi selecionada!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else{
            Toast.makeText(this, "Não foi possível acessar o arquivo. Por favor, tente novamente!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(jogo.getImagem().getUri(), options);
        imgJogo.setImageBitmap(bm);
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
