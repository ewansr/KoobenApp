package com.ewansr.www.koobenapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saul Euán on 31/05/2016.
 * Clase que sirver como adaptador para un ListView
 */
public class cTiposRecetaAdapter<String> extends ArrayAdapter<String> implements Filterable {
    public Context context;
    public static ImageView icon = null;
    private List<cTiposReceta> values;
    private List<cTiposReceta> mOriginalValues;
    private LayoutInflater inflater;
    private ItemFilter mFilter = new ItemFilter();

    public cTiposRecetaAdapter(Context context, int resource, List<cTiposReceta> values){
        super(context, resource, (List<String>) values);
        this.context = context;
        this.values = values;
    }

    @Override
    public long getItemId(int position){
        return values.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        TextView txtName;
        Boolean needRefreshImg = false;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View itemView = convertView;
        if (convertView == null) {
            itemView = inflater.inflate(R.layout.listview_row_adapter, parent, false);
            needRefreshImg = true;
        }
        /** Localizar todos los elementos del layout **/

        icon = (ImageView) itemView.findViewById(R.id.list_row_image);

        if (needRefreshImg) {
            LoadRemoteImg loadRemoteImg = new LoadRemoteImg(icon);
            loadRemoteImg.execute(cRutasAPI.urlImgTipoReceta + cRutasAPI.imgName + Integer.toString(values.get(position).getId()) + cRutasAPI.extDefault);
        }

        txtName = (TextView) itemView.findViewById(R.id.tvNombre);
        txtName.setText(values.get(position).getName());
        return itemView;
    }


    /** Esta funcion sobreescrita me ayuda
     * a que no truene al intentar buscar un arreglo fuera
     * de la posición haciendo el filter*/
    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    /** Implementar el filtrado de mi adaptador personalizado*/
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            /** Obtengo los caracteres que voy a usar para filtar y los añado a mi variable*/
            String filterString = (String) constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            /** A mi lista le asigno los valores de mi adaptador original*/
            List<cTiposReceta> list = values;

            /** Hago un respaldo de mi adaptador original para posteriormente utilizarlo
             * en cuyo caso no encuentre alguna coincidencia */
            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<cTiposReceta>(values); /**REspaldo los valores originales*/
            }

            /** En el caso que no escriba nada en el filtro o no haya coincidencias
             * reasigno los valores originales para que se pueda continuar con el filtrado
             * a la hora de ir borrando caracteres */

            if (constraint == null || constraint.length() == 0 || values.size() == 0) {
                /** Regresar sus valores originales*/
                results.count = mOriginalValues.size();
                results.values = mOriginalValues;
                list = mOriginalValues;
            }

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);
            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = (String) list.get(i);
                if (filterableString.toString().toLowerCase().contains((CharSequence) filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try{
                /** Casteo los valores al tipo de Lista que sirve de adaptador a mi Listview*/
                values = (List<cTiposReceta>) results.values;

                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
