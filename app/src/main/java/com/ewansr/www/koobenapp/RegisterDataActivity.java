package com.ewansr.www.koobenapp;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import org.json.JSONObject;
import static com.ewansr.www.koobenapp.cUtils.setStatusColor;
import static com.ewansr.www.koobenapp.SQLiteDBDataSource.insertDataUser;

/**
 * Created by EwanS on 22/06/2016.
 */
public class RegisterDataActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nombre;
    private EditText mail;
    private EditText password;
    private EditText confirmacion;
    private Toolbar toolbar;
    private Button btnLogin;
    private RelativeLayout viewForm;
    private APIRegistro registro;
    private String fbUserID;
    private String fb_last_name;
    private ContentValues values;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registerdata );

        nombre = (EditText) findViewById( R.id.EdtNombre );
        mail = (EditText) findViewById( R.id.EdtEmail );
        password = (EditText) findViewById( R.id.EdtContrasena );
        confirmacion = (EditText) findViewById( R.id.EdtConfirmar );
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        btnLogin = (Button) findViewById( R.id.btnCrearCuenta );
        viewForm = (RelativeLayout) findViewById( R.id.viewForm );

        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );

        setStatusColor( RegisterDataActivity.this );
        btnLogin.setOnClickListener( this );
        toolbar.setNavigationOnClickListener( this );

        fbUserID = "";
        fb_last_name = " ";

        Bundle b = getIntent().getExtras();
        if (b != null){
            fbUserID     = b.getString("id");
            fb_last_name = b.getString("last_name");
            mail.setText(b.getString("email"));
            mail.setVisibility(View.GONE);
            nombre.setText(b.getString("first_name"));
        }
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
            registro = new APIRegistro(RegisterDataActivity.this) {
                @Override
                public void registroExitoso(APIRegistroModel usuario) {
                    viewRegistroExitoso(usuario);
                }

                @Override
                public void registroError(String error) {
                    viewRegistroError(error);
                }
            };

            String _mail = mail.getText().toString().trim();
            String _nombre = nombre.getText().toString().trim();
            String _password = password.getText().toString().trim();
            String _confirmacion = confirmacion.getText().toString().trim();
            String form = _mail + _nombre + _password + _confirmacion;



            viewForm.setEnabled(false);

            if (form.isEmpty()) {
                throw new Exception("FORM");
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(_mail).matches()) {
                throw new Exception("MAIL");
            }

            if (!(_confirmacion.equals(_password))) {
                throw new Exception("PASS");
            }

            JSONObject usuario = new JSONObject();

            // Operador culeario(ternario)
            usuario.put( "sImg", (!fbUserID.isEmpty()?"https://graph.facebook.com/" + fbUserID + "/picture?type=large":""));
            usuario.put( "sMail", _mail );
            usuario.put( "sIdUsuario", _mail );
            usuario.put( "sPassword", _password );
            usuario.put( "sNombre", _nombre );
            usuario.put( "sApellidos", fb_last_name );
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
        nombre.setText("");
        mail.setText("");
        password.setText("");
        confirmacion.setText("");

        values = new ContentValues();
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_IMG, (!fbUserID.isEmpty()?"https://graph.facebook.com/" + fbUserID + "/picture?type=large":""));
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_MAIL, usuario.mail);
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_NAME, usuario.nombre);
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_TYPE, "user");
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_USER, usuario.mail);
        values.put(SQLiteDBDataSource.ColumnQuotes.SESSIONID, usuario.session);

        if (values != null){
            insertDataUser(RegisterDataActivity.this, values);
        }

        Intent i = new Intent(RegisterDataActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }


    public void viewRegistroError( String error ) {
        viewForm.setEnabled( true );
        Log.i( "edmsamuel", error );
    }
}
