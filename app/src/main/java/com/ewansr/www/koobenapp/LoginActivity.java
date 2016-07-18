package com.ewansr.www.koobenapp;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.ewansr.www.koobenapp.cUtils.setStatusColor;
import static com.ewansr.www.koobenapp.SQLiteDBConfig.DATABASE_NAME;



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

        context       = LoginActivity.this;
        btnLogin      = (Button)          findViewById( R.id.btnLogin );
        mail          = (EditText)        findViewById( R.id.edtUsuario );
        password      = (EditText)        findViewById( R.id.edtContrasena );
        tvRegister    = (TextView)        findViewById( R.id.tvRegister );
        tvNameFB      = (TextView)        findViewById( R.id.tvNameFB );
        facebookLogin = (LoginButton)     findViewById( R.id.btnFacebookLogin );
        imgFB         = (CircleImageView) findViewById( R.id.imgFB );

        btnLogin.setOnClickListener( this );
        tvRegister.setOnClickListener( this );
        facebookLogin.setReadPermissions( Arrays.asList("email") );
        facebookButtonVerificarSesion();
        facebookButtonInicializar();

        SQLiteDBDataSource dataSource = new SQLiteDBDataSource(this);
    }



    /**
     * Verifica la sesión de Facebook
     *
     */
    private void facebookButtonVerificarSesion() {
        if ( AccessToken.getCurrentAccessToken() != null) {
            final String accessToken = AccessToken.getCurrentAccessToken().getToken();

            APIFacebook objAF = new APIFacebook() {
                @Override public void SuccessAuth( Bundle datos ) {
                    ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Leyendo sesión activa...");
                    progressDialog.show();

                    String email = null;
                    if ( datos.getString("email") == null ){
                        email = datos.getString("idFacebook") + "@facebook.com";
                    } else {
                        email = datos.getString("email");
                    }
                    koobenFacebookLogin( email , accessToken );
                }
            };
            final GraphRequest request = objAF.getDataFb( AccessToken.getCurrentAccessToken(), accessToken, imgFB );
            Bundle parameters = new Bundle();
            parameters.putString( "fields", "id,first_name,last_name,email,gender,birthday,location" );
            request.setParameters( parameters );
            request.executeAsync();
        }
    }



    /**
     * Inicializa el botón de inicio de sesión de Facebook
     *
     */
    private void facebookButtonInicializar() {
        facebookLogin.registerCallback( callbackManager, new FacebookCallback<LoginResult>() {
            @Override public void onSuccess( LoginResult loginResult ) {
                ProgressDialog progressDialog = null;

                try {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage( "Por favor, espere..." );
                    progressDialog.show();

                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    String str_token = ( ( accessToken != null ) ? AccessToken.getCurrentAccessToken().getToken() : "" );

                    if ( accessToken != null ) {
                        final String at = str_token;

                        APIFacebook objAF = new APIFacebook() {
                            @Override public void SuccessAuth( Bundle datos ) {
                                String email = datos.getString( "email" );
                                email = ( email == null ? datos.getString( "idFacebook" ) + "@facebook.com" : email );
                                koobenFacebookLogin( email , at );
                            }
                        };

                        final GraphRequest request = objAF.getDataFb( AccessToken.getCurrentAccessToken(), str_token, imgFB );
                        Bundle parameters = new Bundle();
                        parameters.putString( "fields", "id,first_name,last_name,email,gender,birthday,location" );
                        request.setParameters( parameters );
                        request.executeAsync();
                    }

                } catch ( Exception error ) {
                    loginActivityShowMessage( "Facebook Login :: " + error.getClass().toString(), error.getLocalizedMessage() + " :: " + error.getMessage() );
                } finally {
                    if ( progressDialog.isShowing() ) { progressDialog.dismiss(); }
                }
            }

            @Override public void onCancel() {
                loginActivityShowMessage( "Facebook", "Operacion Cancelada" );
            }

            @Override public void onError( FacebookException exception ) {
                loginActivityShowMessage( "FacebookException :: " + exception.getCause().toString(), exception.getLocalizedMessage() + "::" + exception.getMessage() );
            }
        });
    }



    /**
     * Limpia los campos del formulario
     *
     */
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
        loginActivityShowMessage( "Autenticación exitosa", "Bienvenido " + usuario.nombre + " " + usuario.apellidos );
        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }



    /**
     * Llamado al ocurrir un error en la autenticación
     */
    public void loginAutenticacionFallida() {
        loginActivityShowMessage( "Error de autenticación", "Usuario ó Contraseña incorrectos" );
        clearForm();
    }



    /**
     * Llamado al ocurrir un error en la autenticación al no tener cuenta con fb
     */
    public void loginAutenticacionFallidaFB() {
        LoginManager.getInstance().logOut();
        AlertDialog.Builder builder = new AlertDialog.Builder( LoginActivity.this );
        builder
                .setTitle( "Error de autenticación" )
                .setMessage( "Al parecer aun no te has registrado para acceder a nuestra aplicación. ¿Deseas registrarte?" )
                .setPositiveButton( "Si", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent( context, RegisterMenuActivity.class );
                        startActivity( i );
                        dialog.cancel();
                    }
                })
                .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
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
            progressDialog.setMessage("Iniciando sesión...");
            progressDialog.show();

            try {
                APILoginAuth autenticacion = new APILoginAuth() {
                    @Override public void autenticacionExitosa(APILoginAuthModel usuario) {
                        loginAutenticacionExitosa( usuario );
                    }

                    @Override public void autenticacionFallida() {
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



    /**
     * Solicita a la api la autenticación del usuario con datos de Facebook
     * @param emailFb
     * @param finalAccessToken
     */
    private void koobenFacebookLogin(String emailFb, String finalAccessToken){
        // Login con API de don twichy
        APILoginAuth autenticacion = new APILoginAuth() {
            @Override public void autenticacionExitosa(APILoginAuthModel usuario) {
                loginAutenticacionExitosa(usuario);
            }

            @Override public void autenticacionFallida() {
                loginAutenticacionFallidaFB();
            }
        };
        autenticacion.autenticarConFacebook(emailFb, finalAccessToken);
    }



    /**
     * Muestra un mensaje simple
     *
     * @param title
     * @param message
     */
    public void loginActivityShowMessage( String title, String message ) {
        AlertDialog.Builder builder = new AlertDialog.Builder( LoginActivity.this );
        builder
                .setTitle( title )
                .setMessage( message )
                .create()
                .show();
    }



    @Override protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        callbackManager.onActivityResult( requestCode, resultCode, data );
    }
}
