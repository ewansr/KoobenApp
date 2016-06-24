package com.ewansr.www.koobenapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.ewansr.www.koobenapp.cUtils.setStatusColor;


/**
 * Created by EwanS on 20/06/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private TextView tvRegister;
    private FancyButton btnLogin;
    private EditText mail;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_login );
        setStatusColor( LoginActivity.this );

        context = LoginActivity.this;
        btnLogin = (FancyButton) findViewById( R.id.btnLogin );
        mail = (EditText) findViewById( R.id.edtUsuario );
        password = (EditText) findViewById( R.id.edtContrasena );
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        btnLogin.setOnClickListener( this );
        tvRegister.setOnClickListener( this );
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
    public void loginAautenticacionFallida() {
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
            APILoginAuth autenticacion = new APILoginAuth(){
                @Override public void autenticacionExitosa( APILoginAuthModel usuario ) { loginAutenticacionExitosa( usuario ); }
                @Override public void autenticacionFallida() { loginAautenticacionFallida(); }
            };
            autenticacion.autenticar( mail.getText().toString(), password.getText().toString() );
        }

        if ( target.getId() == R.id.tvRegister ) {
            Intent i = new Intent(context, RegisterMenuActivity.class);
            startActivity(i);
        }
    }
}
