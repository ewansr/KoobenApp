package com.ewansr.www.koobenapp;

/**
 * Created by EwanS on 21/06/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
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
public class RegisterMenuActivity extends AppCompatActivity {
    private LoginButton facebookLogin;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize( getApplicationContext() );
        callbackManager = CallbackManager.Factory.create();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registermenu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusColor(RegisterMenuActivity.this);

        Button btnLogin = (Button) findViewById(R.id.btnCrearCuenta);
        facebookLogin = (LoginButton) findViewById(R.id.btnFacebookLogin);
        facebookLogin.setReadPermissions( Arrays.asList("email") );
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i( "edmsamuel", "Login con Facebook exitoso." );
                ProgressDialog progressDialog = null;

                try {
                    progressDialog = new ProgressDialog(RegisterMenuActivity.this);
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

                        APIFacebook aFB = new APIFacebook() {
                            @Override
                            public Bundle SuccessAuth() {
                                Bundle b = super.SuccessAuth();
                                openForm(b);
                                return b;
                            }
                        };

                        final GraphRequest request = aFB.getDataFb(AccessToken.getCurrentAccessToken(), accessToken, null);
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

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


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForm(null);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
    }

    private void handleOnBackPress() {
        finish();
    }

    private void openForm(Bundle bundle){
        Intent i = new Intent(RegisterMenuActivity.this, RegisterDataActivity.class);
        if (bundle != null) {
            i.putExtra("id", bundle.getString("idFacebook"));
            i.putExtra("email", bundle.getString("email"));
            i.putExtra("first_name", bundle.getString("first_name"));
            i.putExtra("last_name", bundle.getString("last_name"));
        }
        startActivity(i);
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        callbackManager.onActivityResult( requestCode, resultCode, data );
    }

}
