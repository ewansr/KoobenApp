package com.ewansr.www.koobenapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.ewansr.www.koobenapp.cUtils.setStatusColor;



/**
 * Created by EwanS on 20/06/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private TextView tvRegister;
    private TextView tvNameFB;
    private CircleImageView imgFB;
    private Button btnLogin;
    private EditText mail;
    private EditText password;
    private LoginButton facebookLogin;
    private CallbackManager callbackManager;



    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        FacebookSdk.sdkInitialize( getApplicationContext() );
        callbackManager = CallbackManager.Factory.create();
        setContentView( R.layout.activity_login );
        setStatusColor( LoginActivity.this );

        context = LoginActivity.this;
        btnLogin = (Button) findViewById( R.id.btnLogin );
        mail = (EditText) findViewById( R.id.edtUsuario );
        password = (EditText) findViewById( R.id.edtContrasena );
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvNameFB = (TextView) findViewById(R.id.tvNameFB);
        facebookLogin = (LoginButton) findViewById(R.id.btnFacebookLogin);
        imgFB = (CircleImageView) findViewById(R.id.imgFB);

        btnLogin.setOnClickListener( this );
        tvRegister.setOnClickListener( this );

        // Si ya tiene token activo lanzar la activity principal
        if ( AccessToken.getCurrentAccessToken() != null) {
            final String accessToken = AccessToken.getCurrentAccessToken().getToken();

            APIFacebook objAF = new APIFacebook() {
                @Override public void SuccessAuth( Bundle datos ) {
                    LoginFB( datos.getString("email"), accessToken );
                }
            };
            final GraphRequest request = objAF.getDataFb(AccessToken.getCurrentAccessToken(), accessToken, imgFB);
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();
        }

        facebookLogin.setReadPermissions( Arrays.asList("email") );
        facebookLogin.registerCallback( callbackManager, new FacebookCallback<LoginResult>() {
            @Override public void onSuccess(LoginResult loginResult) {
                Log.i( "edmsamuel", "Login con Facebook exitoso." );
                ProgressDialog progressDialog = null;

                try {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Leyendo datos de Facebook");
                    progressDialog.show();

                    // Token de la sessión activa
                    String accessToken = null;
                    if (AccessToken.getCurrentAccessToken() != null) {
                        accessToken = AccessToken.getCurrentAccessToken().getToken();
                    }

                    Log.i("accessToken", accessToken);

                    if (AccessToken.getCurrentAccessToken() != null) {
                        final String at = AccessToken.getCurrentAccessToken().getToken();

                        APIFacebook objAF = new APIFacebook() {
                            @Override public void SuccessAuth( Bundle datos ) {
                                LoginFB(datos.getString("email"), at);
                            }
                        };

                        final GraphRequest request = objAF.getDataFb(AccessToken.getCurrentAccessToken(), accessToken, imgFB);
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                } finally {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }

            @Override public void onCancel() {
                Log.i( "edmsamuel", "Login con Facebook cancelado" );
            }

            @Override public void onError(FacebookException exception) {
                System.out.println("onError");
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });
    }



    /**
     * Limpia los campos del formulario
     */
    // No me digassssssssssssssssss
    // si te digooooo
    public void clearForm() {
        mail.setText( "" );
        password.setText( "" );
    }



    /**
     * Llamado en caso que la autenticación sea exitosa.
     * @param usuario APIModelLoginAuth Resultado de la autenticación
     */
    public void loginAutenticacionExitosa( APILoginAuthModel usuario ) {
        clearForm();
        AlertDialog.Builder builder = new AlertDialog.Builder( LoginActivity.this );
        builder
                .setTitle( "Autenticación exitosa" )
                .setMessage( "Bienvenido " + usuario.nombre + " " + usuario.apellidos )
                .create()
                .show();
        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(i);
    }



    /**
     * Llamdo al ocurrir un error en la autenticación
     *
     */
    public void loginAutenticacionFallida() {
        AlertDialog.Builder builder = new AlertDialog.Builder( LoginActivity.this );
        builder
            .setTitle( "Error de autenticación" )
            .setMessage( "Usuario ó Contraseña incorrectos" )
            .create()
            .show();
        clearForm();
    }



    /**
     * Llamado al ocurrir un evento de click a un elemento en la vista.
     *
     * @param target View Elemento en la vista al que se dió click.
     */
    @Override public void onClick( View target ) {

        if ( target.getId() == R.id.btnLogin ) {
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Procesando datos...");
            progressDialog.show();
            try {
                APILoginAuth autenticacion = new APILoginAuth() {
                    @Override
                    public void autenticacionExitosa(APILoginAuthModel usuario) {
                        loginAutenticacionExitosa(usuario);
                    }

                    @Override
                    public void autenticacionFallida() {
                        loginAutenticacionFallida();
                    }
                };
                autenticacion.autenticar(mail.getText().toString(), password.getText().toString());
            } finally {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }

        if ( target.getId() == R.id.tvRegister ) {
            Intent i = new Intent(context, RegisterMenuActivity.class);
            startActivity(i);
        }
    }



    @Override protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        callbackManager.onActivityResult( requestCode, resultCode, data );
    }



    private void LoginFB(String emailFb, String finalAccessToken){
        // Login con API de don twichy
        APILoginAuth autenticacion = new APILoginAuth() {
            @Override public void autenticacionExitosa(APILoginAuthModel usuario) {
                loginAutenticacionExitosa(usuario);
            }

            @Override public void autenticacionFallida() {
                loginAutenticacionFallida();
            }
        };
        autenticacion.autenticarConFacebook(emailFb, finalAccessToken);
    }

}
