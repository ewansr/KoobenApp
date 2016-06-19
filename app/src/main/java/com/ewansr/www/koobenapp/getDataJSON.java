package com.ewansr.www.koobenapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saul Euán on 31/05/2016.
 *
 * Mediante esta clase podemos consumir datos JSON
 * del API web.
 * Solo hay que instanciar la clase y en el parámetro del constructor
 * agregar la ruta de donde se va a obtener el JSON.
 */
public class getDataJSON extends AsyncTask<ListView, String, JSONObject>{
    HttpURLConnection urlConn;
    Context context;
    String urlJSON = null;
    JSONObject jsonObj = null;
    cTiposReceta ctr;
    cRecetas cr;
    ArrayList<cTiposReceta> arraycTiposReceta = new ArrayList<cTiposReceta>();
    List<cRecetas> recipes = new ArrayList<>();

    /** Objetos que pueden rellenar con datos JSON inicializados en Null */
    ListView lv;
    RecyclerView rv;
    public static TextView Ingredientes;


    /** Lista de adaptadores inicalizados en null */
    public static cTiposRecetaAdapter<cTiposReceta> adapter;
    public static cRvAdapter rvadapter;
    public static ProgressDialog dialog;


    public getDataJSON(String urlJSON, Context context, TextView Ingredientes){
        this.urlJSON = urlJSON;
        this.context = context;
        this.Ingredientes = Ingredientes;
    }

    public getDataJSON(String urlJSON, Context context, ListView...params){
        this.urlJSON = urlJSON;
        this.context = context;
        this.lv = params[0];
    }

    public getDataJSON(String urlJSON, Context context, RecyclerView rv){
        this.urlJSON = urlJSON;
        this.context = context;
        this.rv = rv;
    }

    @Override
    protected void onPreExecute(){
        dialog = ProgressDialog.show(context, "", "Cargando menú, por favor espere...", true, true);
    }

    @Override
    protected JSONObject doInBackground(ListView... params) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlJSON);
            urlConn = (HttpURLConnection) url.openConnection();
            InputStream in;

            in = new BufferedInputStream(urlConn.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            urlConn.disconnect();
        }

        try {
            jsonObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj;
    }


    @Override
    protected void onPostExecute(JSONObject a){
        if (a != null){

            try {
                 /**
                 * adaptador para el tipo de recetas en un listView
                 */
                if(lv != null) {
                    JSONArray jArray = a.getJSONArray("items");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject objJSON = jArray.getJSONObject(i);
                            /** Esta sección solo aplica para el adaptador de listview
                            *  para los tipos de receta
                            */
                            ctr = new cTiposReceta(objJSON.getInt("id"),
                                                   objJSON.getString("codigo"),
                                                   objJSON.getString("nombre"));
                            arraycTiposReceta.add(ctr);
                            /** Aquí hay que configurar el resto de adaptores que se llegue a requerir*/

                    }
                    adapter = new cTiposRecetaAdapter<cTiposReceta>(context, 0, arraycTiposReceta);
                    lv.setAdapter(adapter);
                }

                /** Esta sección solo aplica para el adaptador del cardview recetas paginadas */
                else if (rv != null){
                    JSONArray jArray = a.getJSONArray("items");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject objJSON = jArray.getJSONObject(i);
                        String urlImg = cRutasAPI.urlImgRecetas + cRutasAPI.imgNameRecipes + Integer.toString(objJSON.getInt("id")) + cRutasAPI.extjpg;
                        cr = new cRecetas(objJSON.getInt("id"), objJSON.getString("nombre"), objJSON.getString("descripcion"), urlImg, objJSON.getString("preparacion"));
                        recipes.add(cr);
                    }

                    rvadapter = new cRvAdapter(recipes, context);
                    rvadapter.notifyDataSetChanged();
                    rv.setAdapter(rvadapter);
                }

                /** En caso de que el programdor no envie un listview o un cardview
                 *  va a retornar el JsonArray para que haga lo que se le antoje
                 * */

                else {
                    String ListaIngredientes = "";
                    JSONArray jArray = a.getJSONArray("items");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject objJSON = jArray.getJSONObject(i);
                        ListaIngredientes = ListaIngredientes  + objJSON.getString("cantidad") + "\t" + /*objJSON.getString("um") +*/ "\t" + objJSON.getString("nombre") +"\n";
                    }
                    Ingredientes.setText(ListaIngredientes);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (dialog.isShowing()) {
          dialog.dismiss();
        }
    }

}
