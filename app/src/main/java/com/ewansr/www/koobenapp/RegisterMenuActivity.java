package com.ewansr.www.koobenapp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;
import static com.ewansr.www.koobenapp.cUtils.setStatusColor;


/**
 * Created by EwanS on 20/06/2016.
 */
public class RegisterMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginButton facebookLogin;
    private Button btnLogin;
    private Toolbar toolbar;
    private CallbackManager callbackManager;

    @Override protected void onCreate( Bundle savedInstanceState ) {
        FacebookSdk.sdkInitialize( getApplicationContext() );
        callbackManager = CallbackManager.Factory.create();

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registermenu );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        setStatusColor( RegisterMenuActivity.this );


        btnLogin = (Button) findViewById( R.id.btnCrearCuenta );
        facebookLogin = (LoginButton) findViewById( R.id.btnFacebookLogin );
        btnLogin.setOnClickListener( this );
        toolbar.setNavigationOnClickListener( this );

        inicializarFacebookButton();
    }


    
    private void handleOnBackPress() {
        finish();
    }



    /**
     * Inicializa el botón de inicio de sesión de Facebook
     *
     */
    private void inicializarFacebookButton() {
        facebookLogin.setReadPermissions( Arrays.asList( "email" ) );
        facebookLogin.registerCallback( callbackManager, new FacebookCallback<LoginResult>() {
            @Override public void onSuccess( LoginResult loginResult ) {
                ProgressDialog progressDialog = new ProgressDialog( RegisterMenuActivity.this );
                progressDialog.setMessage( "Leyendo datos de Facebook" );

                try {
                    progressDialog.show();
                    AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
                    String accessToken = ( ( currentAccessToken != null ) ? currentAccessToken.getToken() : "" );

                    if ( currentAccessToken != null) {
                        APIFacebook aFB = new APIFacebook() { @Override public void SuccessAuth( Bundle datos ) {
                            menuActivityOpenForm( datos );
                        } };

                        final GraphRequest request = aFB.getDataFb( currentAccessToken, accessToken, null );
                        Bundle parameters = new Bundle();
                        parameters.putString( "fields", "id,first_name,last_name,email,gender,birthday,location" );
                        request.setParameters( parameters );
                        request.executeAsync();
                    }

                } finally {
                    if ( progressDialog.isShowing() ){
                        progressDialog.dismiss();
                    }
                }
            }

            @Override public void onCancel() {
                Log.i( this.getClass().toString(), "Login con Facebook cancelado" );
            }

            @Override public void onError(FacebookException exception) {
                menuActivityShowMessage( "FacebookException", exception.getMessage() );
            }
        });
    }



    /**
     * Muestra el activity del formulario de registro
     * @param bundle
     */
    private void menuActivityOpenForm(Bundle bundle ){
        Intent form = new Intent( RegisterMenuActivity.this, RegisterDataActivity.class );
        if ( bundle != null ) {
            String email = bundle.getString("email");
            String facebookId =  bundle.getString( "idFacebook" );
            email = ( ( email == null ) ? facebookId + "@facebook.com" : email );
            form.putExtra( "id", facebookId );
            form.putExtra( "email", email );
            form.putExtra( "first_name", bundle.getString( "first_name" ) );
            form.putExtra( "last_name", bundle.getString( "last_name" ) );
        }
        startActivity( form );
    }



    @Override protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        callbackManager.onActivityResult( requestCode, resultCode, data );
    }



    /**
     * Muestra un mensaje simple
     *
     * @param title
     * @param message
     */
    public void menuActivityShowMessage( String title, String message ) {
        AlertDialog.Builder builder = new AlertDialog.Builder( RegisterMenuActivity.this );
        builder
                .setTitle( title )
                .setMessage( message )
                .create()
                .show();
    }

    /**
     * Invocado cuando se da clic a un elemento de la vista
     * @param target
     */
    @Override public void onClick( View target ) {
        int id = target.getId();

        if ( id == R.id.btnCrearCuenta ) {
            menuActivityOpenForm(null);
        } else if ( id == R.id.toolbar ) {
            handleOnBackPress();
        }
    }
}
