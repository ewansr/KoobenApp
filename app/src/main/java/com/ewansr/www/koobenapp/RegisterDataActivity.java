package com.ewansr.www.koobenapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import org.json.JSONObject;
import mehdi.sakout.fancybuttons.FancyButton;
import static com.ewansr.www.koobenapp.cUtils.setStatusColor;

/**
 * Created by EwanS on 22/06/2016.
 */
public class RegisterDataActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nombre;
    private EditText mail;
    private EditText password;
    private EditText confirmacion;
    private Toolbar toolbar;
    private FancyButton btnLogin;
    private RelativeLayout viewForm;
    private APIRegistro registro;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registerdata );

        nombre = (EditText) findViewById( R.id.EdtNombre );
        mail = (EditText) findViewById( R.id.EdtEmail );
        password = (EditText) findViewById( R.id.EdtContrasena );
        confirmacion = (EditText) findViewById( R.id.EdtConfirmar );
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        btnLogin = (FancyButton) findViewById( R.id.btnCrearCuenta );
        viewForm = (RelativeLayout) findViewById( R.id.viewForm );

        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );

        setStatusColor( RegisterDataActivity.this );
        btnLogin.setOnClickListener( this );
        toolbar.setNavigationOnClickListener( this );

        registro = new APIRegistro( RegisterDataActivity.this ) {
            @Override public void registroExitoso( APIRegistroModel usuario ) { viewRegistroExitoso( usuario ); }
            @Override public void registroError( String error ) { viewRegistroError( error ); }
        };
    }


    /**
     * Llamado al dar clic en atras
     *
     */
    private void handleOnBackPress() {
        finish();
    }


    /**
     * Llamado para iniciar la creación de la cuenta
     *
     */
    public void crearCuenta() {
        try {
            String _mail = mail.getText().toString().trim();
            String _nombre = nombre.getText().toString().trim();
            String _password = password.getText().toString().trim();
            String _confirmacion = confirmacion.getText().toString().trim();
            String form = _mail + _nombre + _password + _confirmacion;
            viewForm.setEnabled( false );

            if ( form.isEmpty() ) {
                throw new Exception( "FORM" );
            }

            if ( !Patterns.EMAIL_ADDRESS.matcher( _mail ).matches() ) {
                throw new Exception( "MAIL" );
            }

            if ( !( _confirmacion.equals( _password ) ) ) {
                throw new Exception( "PASS" );
            }

            JSONObject usuario = new JSONObject();
            usuario.put( "sMail", _mail );
            usuario.put( "sIdUsuario", _mail );
            usuario.put( "sPassword", _password );
            usuario.put( "sNombre", _nombre );
            usuario.put( "sApellidos", " " );
            usuario.put( "sTipo", "user" );

            registro.registrar( usuario );

        } catch ( Exception e ) {
            String mensaje;
            String codigo = e.getMessage();

            switch (codigo) {
                case "FORM":
                    mensaje = "El formulario está incompleto";
                    break;
                case "MAIL":
                    mensaje = "Correo electrónico invalido";
                    break;
                case "PASS":
                    mensaje = "Las contraseñas no coinciden";
                    break;
                default:
                    mensaje = codigo;
                    break;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder( RegisterDataActivity.this );
            builder.setTitle( "Lo sentimos" );
            builder.setMessage( mensaje );

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override public void onClick( DialogInterface dialog, int which ) {}
            });
            builder.create().show();
        }
    }



    /**
     * Llamado al dar click en algún elemento
     *
     */
    @Override
    public void onClick( View target ) {
        if ( target.getId() == R.id.btnCrearCuenta ) {
            crearCuenta();
        } else if ( target.getId() == R.id.toolbar ) {
            handleOnBackPress();
        }
    }




    /**
     * Llamado si el registro fue exitoso
     */
    public void viewRegistroExitoso( APIRegistroModel usuario ) {
        Intent i = new Intent(RegisterDataActivity.this, MainActivity.class);
        startActivity(i);
    }


    public void viewRegistroError( String error ) {
        viewForm.setEnabled( true );
        Log.i( "edmsamuel", error );
    }
}
