package com.ewansr.www.koobenapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URL;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public  static String UrlJSON = cRutasAPI.UrlJSON_tiposReceta;
    public static Context mainContext;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static int IdTipoRecetaSel;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static ListView lvMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainContext = MenuActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** Crear el adaptador que va a utilizar el fragment **/
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        /** Establecer el viewPager con su adaptador */
        mViewPager = (ViewPager) findViewById(R.id.cnt_menu);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /** Añadiendo uso a las Tabs*/
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /** Añadiendo el Floating action Button para trabajar con el */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

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
                ((cTiposRecetaAdapter)lvMenu.getAdapter()).getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((cTiposRecetaAdapter)lvMenu.getAdapter()).getFilter().filter(newText);
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

        if (id == R.id.nav_ciudad) {

        }
        else if (id == R.id.nav_metodopago) {

        } else if (id == R.id.nav_cerrarsesion) {

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
            View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

            lvMenu = (ListView) rootView.findViewById(R.id.lvMenuRecetas);
            lvMenu.setTextFilterEnabled(true);
            refreshMenu(lvMenu);

            /** Añadir codigo al evento on Click del listView*/
            lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IdTipoRecetaSel = (int)((cTiposRecetaAdapter)lvMenu.getAdapter()).getItemId(position);
                cRutasAPI.vIdTipo = IdTipoRecetaSel;
                Intent i = new Intent(mainContext, RecipesActivity.class);
                i.putExtra("IdTipoReceta", IdTipoRecetaSel);
                startActivity(i);
            }});

            return rootView;
        }

        /** Este procedimiento me carga los datos desde el API y crea un adaptador para el listview*/
        public void refreshMenu(ListView...params){
            ListView lv = params[0];
            getDataJSON datos = new getDataJSON(UrlJSON,mainContext, lv);
            datos.execute();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** getItem is called to instantiate the fragment for the given page.
             * Return a PlaceholderFragment (defined as a static inner class below).
             * mientras estoy trabajando con un solo fragment al las otras pestñas
             * le asigno un fragment 'nulo'*/
            if (position == 0) {
                return PlaceholderFragment.newInstance(position +1);
            }
            else
            {
                Fragment fragment_null = new Fragment();
                return fragment_null;
            }

        }

        @Override
        public int getCount() {
            /** Muestra un total de 3 paginas, se pueden agregar mas*/
            return 3;
        }

        /** Al sobre escribir esta función asígno el nombre a cada una de las tabs*/
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Categorias";
                case 1:
                    return "Nuevas Recetas";
                case 2:
                    return "Menú";
            }
            return null;
        }
    }




}
