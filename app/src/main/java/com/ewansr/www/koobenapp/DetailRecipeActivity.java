package com.ewansr.www.koobenapp;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class DetailRecipeActivity extends AppCompatActivity {
    private Context mainContext;
    private String value, nombreReceta, preparacion;
    private int IdReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainContext = DetailRecipeActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            value = extras.getString("url");
            IdReceta = extras.getInt("IdReceta");
            nombreReceta = extras.getString("nombreReceta");
            preparacion = extras.getString("preparacion");
        }

        TextView tvIngredientes = (TextView) findViewById(R.id.tvlistaIngredientes);
        TextView tvPreparacion = (TextView) findViewById(R.id.tvMetodoPreparacion);
        TextView tvNombreReceta = (TextView) findViewById(R.id.tvTitulo);
        tvPreparacion.setText(nombreReceta);
        tvNombreReceta.setText(preparacion);

        AppBarLayout ab = (AppBarLayout) findViewById(R.id.app_bar);
        LoadRemoteImg loadRemoteImg = new LoadRemoteImg(ab, mainContext);
        loadRemoteImg.execute(value);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Añadido a favoritos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getDataJSON datos = new getDataJSON(cRutasAPI.urlDominio+cRutasAPI.nombreAPI+"receta/"+IdReceta+"/ingredientes",mainContext,tvIngredientes);
        datos.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
