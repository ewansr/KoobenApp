package com.ewansr.www.koobenapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.facebook.login.LoginManager;

import static com.ewansr.www.koobenapp.SQLiteDBDataSource.deleteAll;

/**
 * Created by EwanS on 26/07/2016.
 */
public abstract class NavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static Context context = null;
    public static int IdLayoutResource = 0;
    public static int IdDrawerLayout = 0;
    public static int IdMenuInflater = 0;
    public static int IdSearchView = 0;
    public static int IdToolBar = 0;


    public NavActivity(int IdLayoutResource, int IdDrawerLayout, int IdMenuInflater, int IdSearchView, int IdToolBar, Context context){
        this.IdLayoutResource = IdLayoutResource;
        this.IdDrawerLayout   = IdDrawerLayout;
        this.IdMenuInflater   = IdMenuInflater;
        this.IdSearchView     = IdSearchView;
        this.IdToolBar        = IdToolBar;
        this.context          = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(IdLayoutResource);

        Toolbar toolbar = (Toolbar) findViewById(IdToolBar);
        setSupportActionBar(toolbar);

//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        mViewPager = (ViewPager) findViewById(R.id.cnt_menu);
//        mViewPager.setAdapter(mSectionsPagerAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(IdDrawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_cerrarsesion) {
            deleteAll(context, "user_profile");//Borrar todos los registros de la BD
            LoginManager.getInstance().logOut();
            Intent i = new Intent(context, LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(IdDrawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Para activar el search Manager
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(IdMenuInflater, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(IdSearchView) .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                ((ProductosRvAdapter)rv.getAdapter()).getFilter().filter(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                ((ProductosRvAdapter)rv.getAdapter()).getFilter().filter(newText);
//                return true;
//            }
//        });

        return true;
    }


}
