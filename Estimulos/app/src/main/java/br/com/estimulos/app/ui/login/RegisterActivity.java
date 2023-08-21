package br.com.estimulos.app.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.TerapeutaDAO;
import br.com.estimulos.app.core.util.Util;
import br.com.estimulos.app.core.servicos.webservice.UsuarioWebService;
import br.com.estimulos.dominio.Terapeuta;
import br.com.estimulos.dominio.Usuario;
import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Error Msg TextView Object
    private TextView errorMsg;
    // Name Edit View Object
    private EditText nome;
    // Data de Nascimento View Object
    private EditText dataNascimento;
    // Email Edit View Object
    private EditText email;
    // Passwprd Edit View Object
    private EditText senha;

    private UsuarioWebService usuarioWebService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usuarioWebService = new UsuarioWebService(getApplicationContext());

        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.register_error);
        // Find Name Edit View control by ID
        nome = (EditText)findViewById(R.id.registerName);
        // Find Data Nascimento pelo ID
        dataNascimento = (EditText)findViewById(R.id.registerDataNascimento);
        // Find Email Edit View control by ID
        email = (EditText)findViewById(R.id.registerEmail);
        // Find Password Edit View control by ID
        senha = (EditText)findViewById(R.id.registerPassword);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Aguarde...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void registerUser(final View view){
        prgDialog.show();
        // Get NAme ET control value
        String name = nome.getText().toString();
        // Get Data nascimento value
        String dataNascimento = this.dataNascimento.getText().toString();
        // Get Email ET control value
        String email = this.email.getText().toString();
        // Get Password ET control value
        String password = senha.getText().toString();

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setNome(name);
        terapeuta.setUsuario(new Usuario());
        terapeuta.getUsuario().setLogin(email);
        terapeuta.getUsuario().setSenha(password);

        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if(Util.isNotNull(name) && Util.isNotNull(email) && Util.isNotNull(password)){
            // When Email entered is Valid
            if(Util.validateEmail(email)){
                // TODO: VALIDAR MELHOR A DATA AQUI, HARDCODED
                String[] datas = dataNascimento.split("/");
                Terapeuta t = new Terapeuta();
                t.setNome(name);
                t.setDtNascimento(new Date(Integer.valueOf(datas[2]), Integer.valueOf(datas[1]), Integer.valueOf(datas[0])));
                t.setDataCriacao(new Date());
                t.setUltimaAtualizacao(new Date());
                t.setUsuario(new Usuario());
                t.getUsuario().setLogin(email);
                t.getUsuario().setSenha(password);
                t.getUsuario().setUltimaAtualizacao(new Date());
                t.getUsuario().setDataCriacao(new Date());

               usuarioWebService.registrar(t, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "Sucesso! Status = " + statusCode, Toast.LENGTH_LONG).show();
                        // Converter dados
                        Terapeuta t;
                        try {
                            t = readBytes(responseBody);
                            // Gravar terapeuta no banco de dados
                            salvarTerapeuta(t);
                        } catch (Exception e) {
                            Log.d("Usuario REST", e.getMessage());
                            // TODO : o que fazer se nao conseguir salvar o terapeuta?
                        }
                        navigatetoLoginActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "Falhou! Status = " + statusCode, Toast.LENGTH_LONG).show();

                        // TODO : testar possiveis tipos de falha com status.
                    }
                });
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }
        // When any of the Edit View control left blank
        else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    private Terapeuta readBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        // Criar o arquivo temporario para guardar os dados vindo do array de bytes
        File file = File.createTempFile("tmp", ".tmp", this.getFilesDir());
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        // Criar ZipInput para receber os dados que vieram zipados do stream
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ZipInputStream zipis = new ZipInputStream(bais);
        ZipEntry entry;
        // Rodar enquanto houver Entries
        while((entry = zipis.getNextEntry()) != null){
            int lenght;
            byte[] b = new byte[1024];
            // Gravar no arquivo temporario
            while((lenght = zipis.read(b)) != -1){
                bos.write(b, 0, lenght);
            }
        }
        // Fechar conexões
        bos.close();
        zipis.close();
        // Abrir leitura do arquivo temporario
        FileInputStream fis = this.openFileInput(file.getName());
        ObjectInputStream ois = new ObjectInputStream(fis);
        // Ler objeto
        Terapeuta t = (Terapeuta) ois.readObject();
        // Remover arquivo temporario
        file.delete();
        // Fechar as conexoes
        fis.close();
        ois.close();
        // Retornar terapeuta
        return t;
    }

    private void salvarTerapeuta(Terapeuta t) throws Exception {
        TerapeutaDAO dao = new TerapeutaDAO(BancoDadosHelper.getInstance(this));
        dao.salvar(t);
    }

    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoLoginActivity(){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Set degault values for Edit View controls
     */
    public void setDefaultValues(){
        nome.setText("");
        dataNascimento.setText("");
        email.setText("");
        senha.setText("");
    }
}
