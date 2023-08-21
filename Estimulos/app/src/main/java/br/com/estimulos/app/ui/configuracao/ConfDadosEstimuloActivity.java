package br.com.estimulos.app.ui.configuracao;

import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.CategoriaEstimuloDAO;
import br.com.estimulos.app.core.dao.EstimuloDAO;
import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Imagem;

/**
 * Esta activity recebe os dados do formulario de cadastro de Estimulos
 *
 * Created by Caio Cruz on 02/03/2016.
 */
public class ConfDadosEstimuloActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Tag para passagem de atributos via Intent
    public static final String IMAGEM_TAG = "IMAGEM";

    // Constante para a chamar e receber o resultado onActivityResult
    // de ConfImagemActivity
    public static final int RESULTADO_ACTIVITY_IMG = 1;

    // String para a primeira opcao da spCategoria de categoria
    public static final String SELECIONE = "Selecione";

    // Armazena a enumeracao da operacao feita na tela
    private int operacaoTela;

    // Instancia do DAO de estimulo
    private EstimuloDAO estimuloDAO;

    // Instancia do DAO para categoria
    private CategoriaEstimuloDAO categoriaDAO;

    // Lista de categorias para a spCategoria
    private List<String> categorias;

    // Lista de generos
    private List<String> generos;

    // Objeto a ser criado ou editado
    private Estimulo estimulo;

    // Instancia das views contidas no layout desta activity
    private Button btSalvar;
    private ImageView imagemEstimulo;
    private ImageButton addCategoria;
    private EditText nomeEstimulo;
    private Spinner spCategoria;
    private Spinner spGenero;
    private Uri URIImage;

    // Para mapear as categorias
    private Map<String, CategoriaEstimulo> mapCategorias;

    // Adapter para lista de categorias
    private ArrayAdapter adpterArrayCategoria;
    private ArrayAdapter adpterArrayGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_dados_estimulo);

        /**
         * Recupera o botao do layout
         */
        btSalvar = (Button) findViewById(R.id.btSalvarEstimulo);
        imagemEstimulo = (ImageView) findViewById(R.id.imageEstimulo);
        nomeEstimulo = (EditText) findViewById(R.id.etNomeEstimulo);
        addCategoria = (ImageButton) findViewById(R.id.addAbrirDialog);

        /**
         * Indica que estas views podem ser tocadas
         * o código de tratamento do click esta nesta classe
         */
        imagemEstimulo.setOnClickListener(this);
        btSalvar.setOnClickListener(this);
        addCategoria.setOnClickListener(this);

        try {
            /**
             * Faz instancia dos DAOs necessarios para as consultas desta activity
             */
            categoriaDAO = new CategoriaEstimuloDAO(BancoDadosHelper.getInstance(this));
            estimuloDAO = new EstimuloDAO(BancoDadosHelper.getInstance(this));

            /**
             * Recupera a intent que contem o id do jogo selecionado
             */
            estimulo = (Estimulo) getIntent().getSerializableExtra(MenuEstimulosFragment.TAG_ESTIMULO);
            operacaoTela = getIntent().getIntExtra(MenuJogosFragment.OPERACAO_JOGO, 0);

            /**
             * Array para exibir a lista de Categorias
             */
            categorias = new ArrayList<>();
            categorias.add(SELECIONE);

            /**
             * Para para auxiliar a escolha da Categoria
             * na hora de exibir os estimulosFiltrados
             */
            mapCategorias = new HashMap<>();
            mapCategorias.put(SELECIONE, null);

            /**
             * Loop para armazenar a categoria e
             * mapear o nome como chave e seu id como valor
             */
            for (CategoriaEstimulo c : categoriaDAO.consultarTodos()) {
                categorias.add(c.getNome());
                mapCategorias.put(c.getNome(), c);
            }

            generos = new ArrayList<>();
            generos.add("Selecione");
            generos.add("Feminino");
            generos.add("Masculino");

            /**
             * Codigo responsavel por montar a spCategoria para listar as categorias
             */
            spCategoria = (Spinner) findViewById(R.id.spCategoriaEsti);
            adpterArrayCategoria = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias);
            adpterArrayCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCategoria.setAdapter(adpterArrayCategoria);
            spCategoria.setOnItemSelectedListener(this);


            /**
             * Codigo responsavel por montar a spCategoria para listar as categorias
             */
            spGenero = (Spinner) findViewById(R.id.spGenero);
            adpterArrayGenero = new ArrayAdapter(this, android.R.layout.simple_spinner_item, generos);
            adpterArrayGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spGenero.setAdapter(adpterArrayGenero);
            spGenero.setOnItemSelectedListener(this);


            /**
             * Verifica a operacao a ser realizada
             */
            if(operacaoTela == MenuEstimulosFragment.Operacoes.CADASTRO.getOperacao()){
                // Instancia um novo objeto de estimulo para o cadastro
                estimulo = new Estimulo();
                estimulo.setImagem(new Imagem());
                btSalvar.setBackgroundResource(R.drawable.bt_salvar);
            }
            else if(operacaoTela == MenuJogosFragment.Operacoes.EDITAR.getOperacao()) {
                /**
                 * Recupera os todos os dados do Jogo
                 */
                setarValores();
                btSalvar.setBackgroundResource(R.drawable.bt_editar);
            }

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     * Caso a operacao seja de editar
     * este metodo recupera os valores para exibir na tela
     */
    private void setarValores() {
        // A imagem do estimulo nao eh nula
        if(estimulo.getImagem() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(estimulo.getImagem().getUri(), options);
            imagemEstimulo.setImageBitmap(bm);
        }
        // seta o nome do estimulo na editText
        nomeEstimulo.setText(estimulo.getNome());

        // seta uma categoria na spCategoria
        if(!estimulo.getCategoria().getNome().equals(null)){
            int position = adpterArrayCategoria.getPosition(estimulo.getCategoria().getNome());
            spCategoria.setSelection(position);
        }
        if(!estimulo.getGenero().equals(null)){

            int position;
            if(estimulo.getGenero().equals("a"))
                position = adpterArrayGenero.getPosition("Feminino");
            else
                position = adpterArrayGenero.getPosition("Masculino");
            spGenero.setSelection(position);
        }
    }

    /**
     * Metodo recupera os dados da tela para e seta no objeto de estimulo
     * @return - TRUE - se os dados nao estao em branco
     *         - FALSE - se alguma informacao nao foi inserida
     */
    private boolean recuperarDados(){
        // o nome esta em branco
        if(nomeEstimulo.getText().toString().equals("")) {
            Toast.makeText(this, "Digite o nome do estímulo", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            estimulo.setNome(nomeEstimulo.getText().toString());

        // nao selecionou a categoria
        if(estimulo.getCategoria() == null) {
            Toast.makeText(this, "Selecione uma Categoria", Toast.LENGTH_LONG).show();
            return false;
        }
        if(estimulo.getImagem() == null) {
            Toast.makeText(this, "Insira as imagens", Toast.LENGTH_LONG).show();
            return false;
        }
        if(estimulo.getGenero() == null){
            Toast.makeText(this, "Selecione um gênero", Toast.LENGTH_LONG).show();
            return false;
        }

        estimulo.getImagem().setUriMascara(estimulo.getImagem().getUri());
        // dados ok
        return true;
    }

    /**
     * Trata os clicks na tela
     * @param v - view tocada na tela
     */
    @Override
    public void onClick(View v) {
        /**
         * Clicou no botao de salvar
         */
        if(v == btSalvar) {
            if(recuperarDados()) {
                /**
                 * Verifica a operacao a ser realizada
                 */
                if (operacaoTela == MenuEstimulosFragment.Operacoes.CADASTRO.getOperacao()) {

                    /**
                     * Salva o estimulo no banco de dados
                     */
                    try {
                        estimuloDAO.salvar(estimulo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Retorna para a activity que chamou
                    Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
                    startActivity(intent);

                } else if (operacaoTela == MenuJogosFragment.Operacoes.EDITAR.getOperacao()) {

                    /**
                     * Faz o update do estimulo
                     */
                    try {
                        estimuloDAO.alterar(estimulo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // retorna para a activity que chamou
                    Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
                    startActivity(intent);
                }
            }
        }
        /**
         * Clicou no botao para adicionar uma nova categoria
         */
        else if (v == addCategoria) {
            // chama o dialog para cadastrar
            abrirDialog();
        }
        /**
         * Clicou na imagem para inserir as imagens do estimulo
         */
        else if(v == imagemEstimulo){
            abrirIntencaoImagem();

            /**
             * Faz a chamada da Activity ConfImagensEstimulo que retorna o resultado para esta activity
             * o RESULTADO_ACTITIVY_IMG eh o identificador do retorno desta intent
             */
//            Intent intent = new Intent(this, ConfImagensEstimulo.class);
//            // Passa a imagem do estimulo
//            intent.putExtra(IMAGEM_TAG, estimulo.getImagem());
//            // Passa a operacao do menu na tela
//            intent.putExtra(MenuJogosFragment.OPERACAO_JOGO, operacaoTela);
//
//            startActivityForResult(intent, RESULTADO_ACTIVITY_IMG);


        }
    }

    /**
     * Medodo para abrir o Dialog para criar uma nova categoria
     */
    private void abrirDialog() {

        // Declara o objeto de Dialog
        final Dialog dialog = new Dialog(this);

        // Seta o titulo da dialog
        dialog.setTitle("Nova Categoria");

        // Adiciona o layout
        dialog.setContentView(R.layout.nova_categoria_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.color.cinzaTransparente);

        dialog.setCanceledOnTouchOutside(true);

        // Exibe o dialog
        dialog.show();

        // Recupera as view do layout do dialog
        final EditText etCategoria = (EditText) dialog.findViewById(R.id.etNomeCategoria);
        final Button btAddCategoria = (Button) dialog.findViewById(R.id.btNovaCategoria);
        final Button btCancelar = (Button) dialog.findViewById(R.id.btCancelar);

        // Seta o clique no botao de adicionar
        btAddCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoNome = etCategoria.getText().toString();
                categorias.add(novoNome);
                CategoriaEstimulo novaCategoriaEstimulo = new CategoriaEstimulo();
                novaCategoriaEstimulo.setNome(novoNome);
                mapCategorias.put(novoNome, novaCategoriaEstimulo);
                estimulo.setCategoria(novaCategoriaEstimulo);

                if(!novoNome.equals(null)){
                    int position = adpterArrayCategoria.getPosition(novoNome);
                    spCategoria.setSelection(position);
                }
                dialog.cancel();
            }
        });
        // Seta o clique no botao de cancelar
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        int idSpinner = parent.getId();
        switch(idSpinner) {
            case R.id.spGenero:
                if(generos.get(position).equals("Feminino"))
                    estimulo.setGenero("a");
                else
                    estimulo.setGenero("o");
                break;
            case R.id.spCategoriaEsti:
                // Recupera a categoria no map de categorias
                estimulo.setCategoria(mapCategorias.get(categorias.get(position)));
                break;
        }

    }

    /**
     * Metodo que abre o dialog seletor de datas
     */
    public void showDatePickerDialog(View view) {
        DialogFragment picker = new DatePickerFragment();
        picker.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Metodo para receber um resultado de uma activity
     * @param requestCode - Codigo identificador do retorno
     * @param resultCode - Codigo de status do resultado
     * @param data - Dado de retorno
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        /**
//         * Eh o retorna da Actitity de imagem
//         */
//        if(requestCode == RESULTADO_ACTIVITY_IMG){
//            // O resultado esta ok
//            if(resultCode == RESULT_OK){
//
//                // Seta a nova imagem no estimulo
//                estimulo.setImagem((Imagem) data.getSerializableExtra(IMAGEM_TAG));
//
//                // Exibe a imagem no formulario de estimulo
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;
//                Bitmap bm = BitmapFactory.decodeFile(estimulo.getImagem().getUri(), options);
//                imagemEstimulo.setImageBitmap(bm);
//            }
//        }
//    }

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

        if(estimulo.getImagem() == null) {
            estimulo.setImagem(new Imagem());
            estimulo.getImagem().setUri(URIImage.getPath());
        }
        else
            estimulo.getImagem().setUri(URIImage.getPath());

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

                        estimulo.getImagem().setUri(URIImage.getPath());

                    Bitmap bm = null;
                    bm = BitmapFactory.decodeFile(URIImage.getPath());

                    imagemEstimulo.setImageBitmap(bm);

                } else{
                    selectedImageUri = data == null ? null : data.getData();
                    if(selectedImageUri != null){

                        estimulo.getImagem().setUri(getRealPathFromURI(selectedImageUri));
                        Bitmap bm = null;
                        bm = BitmapFactory.decodeFile(getRealPathFromURI(selectedImageUri));

                        imagemEstimulo.setImageBitmap(bm);
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

        Bitmap bm = BitmapFactory.decodeFile(estimulo.getImagem().getUri(), options);

        imagemEstimulo.setImageBitmap(bm);


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
