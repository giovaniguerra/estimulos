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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.util.Util;
import br.com.estimulos.app.core.servicos.webservice.UsuarioWebService;
import br.com.estimulos.app.ui.launcher.MainActivity;
import br.com.estimulos.dominio.Usuario;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Error Msg TextView Object
    private TextView errorMsg;
    // Email Edit View Object
    private EditText email;
    // Passwprd Edit View Object
    private EditText senha;

    private UsuarioWebService usuarioWebService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioWebService = new UsuarioWebService(getApplicationContext());

        errorMsg = (TextView)findViewById(R.id.login_error);
        email = (EditText)findViewById(R.id.loginEmail);
        senha = (EditText)findViewById(R.id.loginPassword);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Aguarde...");
        prgDialog.setCancelable(false);
    }

    /**
     * Method gets triggered when Login button is clicked
     *
     * @param view
     */
    public void loginUser(View view){
        prgDialog.show();
        // Get Email Edit View Value
        String email = this.email.getText().toString();
        // Get Password Edit View Value
        String password = senha.getText().toString();

        // When Email Edit View and Password Edit View have values other than Null
        if(Util.isNotNull(email) && Util.isNotNull(password)){
            // When Email entered is Valid
            if(Util.validateEmail(email)){
                Usuario usuario = new Usuario();
                usuario.setLogin(email);
                usuario.setSenha(password);

                usuarioWebService.logar(usuario, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "Sucesso! Status = " + statusCode, Toast.LENGTH_LONG).show();
                        // Converter dados
                        Usuario u;
                        try {
                            u = readBytes(responseBody);
                            // Gravar terapeuta no banco de dados
                            salvarUsuario(u);
                        } catch (Exception e) {
                            Log.d("Usuario REST", e.getMessage());
                            // TODO : o que fazer se nao conseguir salvar o terapeuta?
                        }

                        navegarParaHomeActivity();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "Falhou! Status = " + statusCode, Toast.LENGTH_LONG).show();
                    }
                });
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Por favor, preencher com um email válido!", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getApplicationContext(), "Por favor, preencher todos os campos!", Toast.LENGTH_LONG).show();
        }
    }

    private <T> T readBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        // Criar o arquivo temporario para guardar os dados vindo do array de bytes
        File file = File.createTempFile("tmp", ".tmp", this.getFilesDir());
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        // Criar ZipInput para receber os dados que vieram zipados do stream
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ZipInputStream zipis = new ZipInputStream(bais);
        ZipEntry entry;
        // Rodar enquanto houver Entries
        while ((entry = zipis.getNextEntry()) != null) {
            int lenght;
            byte[] b = new byte[1024];
            // Gravar no arquivo temporario
            while ((lenght = zipis.read(b)) != -1) {
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
        T u = (T) ois.readObject();
        // Remover arquivo temporario
        file.delete();
        // Fechar as conexoes
        fis.close();
        ois.close();
        // Retornar terapeuta
        return u;
    }

    private void salvarUsuario(Usuario u){
        // TODO : CADE O DAO DO USUARIO ????
//        UsuarioDAO dao = new UsuarioDAO(BancoDadosHelper.getInstance(this));
//        dao.salvar(u);
    }

    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navegarParaHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void navigatetoRegisterActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }


}
