package com.ewansr.www.koobenapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by EwanS on 08/06/2016.
 */
public abstract class ProductosRvAdapter extends RecyclerView.Adapter<ProductosRvAdapter.recipesViewHolder> {
    public ArrayList<APIProductoModel> productsItems;
    private Context context;
    private ArrayList<APIProductoModel>  mOriginalValues;
    private ItemFilter mFilter = new ItemFilter();

    public ProductosRvAdapter(ArrayList<APIProductoModel> productsItems, Context context){
        this.productsItems = productsItems;
        this.context = context;
    }

    @Override
    public recipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_adapter_products, parent, false);
        recipesViewHolder pvh = new recipesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final recipesViewHolder holder, int position) {
        //YoYo.with(Techniques.FadeIn).playOn(holder.cv);
        holder.product_Name.setText(productsItems.get(position).nombre);
        holder.url = productsItems.get(position).image_url;
        holder.idProducto = productsItems.get(position).id;
        holder.productDescription = productsItems.get(position).descripcion;
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.img.getLayoutParams().height = 250;
        Picasso.with(context)
                .load(productsItems.get(position).image_url)
                .error(R.drawable.logo_kooben)
                .placeholder( R.drawable.progress_animation)
                .into(holder.img);

        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        Random rnd = new Random();
        int randomColor = rnd.nextInt(10);
        holder.cv.setCardBackgroundColor(androidColors[randomColor]);
        holder.product_Name.setBackgroundColor(androidColors[randomColor]);

        if ((position >= productsItems.size() - 1)){
            load();
        }
    }

    public abstract void load();

    @Override
    public int getItemCount() {
        return productsItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class recipesViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView product_Name;
        ImageView img;
        String url, productDescription;
        int idProducto;

        recipesViewHolder(View itemView) {
            super(itemView);
            WindowManager windowmanager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dimension = new DisplayMetrics();
            windowmanager.getDefaultDisplay().getMetrics(dimension);
            cv = (CardView)itemView.findViewById(R.id.cv);
            product_Name = (TextView)itemView.findViewById(R.id.product_title);
            img = (ImageView) itemView.findViewById(R.id.product_image);
            itemView.setOnClickListener(Click);
        }

        public View.OnClickListener Click=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailRecipeActivity.class);
                v.getContext().startActivity(intent);
            }
        };

    }

    public Filter getFilter() {
        return mFilter;
    }

    /** Implementar el filtrado de mi adaptador personalizado*/
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            /** Obtengo los caracteres que voy a usar para filtar y los añado a mi variable*/
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            /** A mi lista le asigno los valores de mi adaptador original*/
            ArrayList<APIProductoModel> list = productsItems;

            /** Hago un respaldo de mi adaptador original para posteriormente utilizarlo
             * en cuyo caso no encuentre alguna coincidencia */
            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<>(productsItems); /**REspaldo los valores originales*/
            }

            /** En el caso que no escriba nada en el filtro o no haya coincidencias
             * reasigno los valores originales para que se pueda continuar con el filtrado
             * a la hora de ir borrando caracteres */

            if (constraint == null || constraint.length() == 0 || productsItems.size() == 0) {
                /** Regresar sus valores originales*/
                results.count = mOriginalValues.size();
                results.values = mOriginalValues;
                list = mOriginalValues;
            }

            int count = list.size();
            final ArrayList<APIProductoModel> nlist = new ArrayList<>(count);
            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).nombre;
                if (filterableString.toString().toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
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
                productsItems = (ArrayList<APIProductoModel>) results.values;

                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetChanged();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}