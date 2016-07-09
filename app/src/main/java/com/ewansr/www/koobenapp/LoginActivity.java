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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        facebookLogin = (LoginButton) findViewById(R.id.facebookLoginButton);
        imgFB = (CircleImageView) findViewById(R.id.imgFB);

        btnLogin.setOnClickListener( this );
        tvRegister.setOnClickListener( this );

        facebookLogin.setReadPermissions( Arrays.asList("email") );
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i( "edmsamuel", "Login con Facebook exitoso." );
                ProgressDialog progressDialog = null;

                try {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Leyendo datos de Facebook");
                    progressDialog.show();

                    String accessToken = loginResult.getAccessToken().getToken();
                    Log.i("accessToken", accessToken);

                    //** twichy Checar https://developers.facebook.com/docs/graph-api/*/
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.i("LoginActivity", response.toString());
                            // Obtener datos de facebook
                            Bundle bFacebookData = getFacebookData(object, imgFB);

                            // Aquí tus variables pedorras
                            // que me solicitaste
                            String idFB    = bFacebookData.getString("id");
                            String nameFB  = bFacebookData.getString("first_name");
                            String emailFb = bFacebookData.getString("email");
                            tvNameFB.setText(nameFB);
                        }
                    });

                    // Contenedor de los datos que requerimos
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                    request.setParameters(parameters);
                    request.executeAsync();
                }finally{
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
        }

        @Override
        public void onCancel() {
            Log.i( "edmsamuel", "Login con Facebook cancelado" );
        }

        @Override
        public void onError(FacebookException exception) {
            System.out.println("onError");
            Log.v("LoginActivity", exception.getCause().toString());
        }
    });
    }

    private Bundle getFacebookData(JSONObject object, ImageView imgProfile) {
            Bundle bundle = new Bundle();
            String id = null;
            try {
                id = object.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        String UrlImgProfile = "https://graph.facebook.com/" + id + "/picture?width=200&height=150";
        bundle.putString("profile_pic", UrlImgProfile);
        LoadRemoteImg rimg = new LoadRemoteImg(imgProfile);
        rimg.execute( UrlImgProfile );

        // Lo siguiente está de más explicarlo con manzanas
        bundle.putString("idFacebook", id);
            try {
                if (object.has("first_name"))
                    bundle.putString("first_name", object.getString("first_name"));
                if (object.has("last_name"))
                    bundle.putString("last_name", object.getString("last_name"));
                if (object.has("email"))
                    bundle.putString("email", object.getString("email"));
                if (object.has("gender"))
                    bundle.putString("gender", object.getString("gender"));
                if (object.has("birthday"))
                    bundle.putString("birthday", object.getString("birthday"));
                if (object.has("location"))
                    bundle.putString("location", object.getJSONObject("location").getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bundle;
        }


    /**
     * Limpia los campos del formulario
     */
    public void clearForm() {
        mail.setText( "" );
        password.setText( "" );
    }

    /**
     * Llamado en caso que la autenticación sea exitosa.
     *
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
        Intent i = new Intent(LoginActivity.this, RegisterMenuActivity.class);
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
    @Override
    public void onClick( View target ) {

        // en caso que el objetivo del `click` del usuario sea el botón de logueo
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
            }finally {
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

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        callbackManager.onActivityResult( requestCode, resultCode, data );
    }
}
