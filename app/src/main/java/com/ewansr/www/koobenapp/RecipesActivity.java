package com.ewansr.www.koobenapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

/**
 * Created by Saulo Euan on 07/06/2016.
 */
public class RecipesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static Context mainContext;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static RecyclerView rv;
    public static StaggeredGridLayoutManager llm;

    private static final String EXTRA_POSITION = "com.ewansr.www.koobenapp.extra.POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        mainContext = RecipesActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** Crear el adaptador que va a utilizar el fragment **/
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        /** Establecer el viewPager con su adaptador */
        mViewPager = (ViewPager) findViewById(R.id.cnt_menu);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /** Sección para añadir el nav bar header a la activity y poder manipularlo**/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** inflar el menú y agregar los items a la barra si está disponible */
        getMenuInflater().inflate(R.menu.menu_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search) .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //((cTiposRecetaAdapter)lvMenu.getAdapter()).getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //((cTiposRecetaAdapter)lvMenu.getAdapter()).getFilter().filter(newText);
                return true;
            }
        });

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

    /** Codigo para el nav Header
     *  Permite controlar las acciones de cada item dentro del navigationView
     **/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /** Controlar los elementos del NavigationView al hacer click**/
        int id = item.getItemId();

        if (id == R.id.nav_compras) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Fragment PlaceHolder contiene una vista simple
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * ARG_SECTION NUMBER  representa el numero de argumento del fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {
        }

        /**
         * Retorna una instancia del fragment
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        /** Sobre escribe el metodo onCreateView inflando el fragmento establecido*/
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.reciclerview_recipes, container, false);

            rv = (RecyclerView)rootView.findViewById(R.id.rv);
            rv.setHasFixedSize(true);
            llm = new StaggeredGridLayoutManager (1, 1);
            rv.setLayoutManager(llm);

            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }
            });

            refreshRV(rv);
            return rootView;
        }

        /** Actualizar el Recicler View*/
        public void refreshRV(RecyclerView...params){
            RecyclerView rv = params[0];
            String UrlJSON = cRutasAPI.urlDominio + cRutasAPI.nombreAPI + "recetas/tipo/"+cRutasAPI.vIdTipo+"/paginar/"+cRutasAPI.vDesde+"/"+cRutasAPI.vHasta;
            getDataJSON datos = new getDataJSON(UrlJSON,mainContext, rv);
            datos.execute();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position+1);
        }

        @Override
        public int getCount() {
            return 1;/** Muestra un total de 3 paginas, se pueden agregar mas*/
        }

        /** Al sobre escribir esta función asígno el nombre a cada una de las tabs*/
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Nombre tipo Receta";
            }
            return null;
        }
    }
}
