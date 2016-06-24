package com.ewansr.www.koobenapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by EwanS on 08/06/2016.
 */
public class cRvAdapter extends RecyclerView.Adapter<cRvAdapter.recipesViewHolder> {
    public List<cRecetas> recipes;
    private Context context;

    public cRvAdapter(List<cRecetas> recipes, Context context){
        this.recipes = recipes;
        this.context = context;
    }

    @Override
    public recipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_adapter, parent, false);
        recipesViewHolder pvh = new recipesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(recipesViewHolder holder, int position) {
        YoYo.with(Techniques.FadeIn).playOn(holder.cv);
        holder.recipeName.setText(recipes.get(position).name);
        holder.recipeCode.setText(recipes.get(position).preparation);
        holder.url = recipes.get(position).url_img;
        holder.IdReceta = recipes.get(position).getId();
        holder.preparacion = recipes.get(position).name;
        holder.nombreReceta = recipes.get(position).preparation;

        //holder.initheight(-1);
        Picasso.with(context)
                .load(cRutasAPI.urlImgRecetas + cRutasAPI.imgNameRecipes + Integer.toString(recipes.get(position).getId()) + cRutasAPI.extjpg)
                .error(R.drawable.cloud_outline_off)
                .placeholder( R.drawable.progress_animation)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class recipesViewHolder extends RecyclerView.ViewHolder {
        int minHeight;
        CardView cv;
        TextView recipeName;
        TextView recipeCode;
        ImageView img;
//        FloatingActionButton btnExpand;
        String url, nombreReceta, preparacion;
        int IdReceta;
        FloatingActionButton btnShow;

        recipesViewHolder(View itemView) {
            super(itemView);

            WindowManager windowmanager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dimension = new DisplayMetrics();
            windowmanager.getDefaultDisplay().getMetrics(dimension);
            final int height = dimension.heightPixels;


            cv = (CardView)itemView.findViewById(R.id.cv);
            recipeName = (TextView)itemView.findViewById(R.id.recipe_name);
            recipeCode = (TextView)itemView.findViewById(R.id.recipe_descripcion);
            img = (ImageView) itemView.findViewById(R.id.recipe_image);
//            btnExpand = (FloatingActionButton) itemView.findViewById(R.id.menu_item_compartir);

//            cv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    cv.getViewTreeObserver().removeOnPreDrawListener(this);
//                    minHeight = cv.getHeight();
//                    ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
//                    layoutParams.height = minHeight;
//                    cv.setLayoutParams(layoutParams);
//
//                    return true;
//                }
//            });

//            btnExpand.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    toggleCardViewnHeight(height);
//                }
//            });

            itemView.setOnClickListener(Click);

        }

        public View.OnClickListener Click=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailRecipeActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("preparacion",preparacion);
                intent.putExtra("nombreReceta",nombreReceta);
                intent.putExtra("IdReceta", IdReceta);

                v.getContext().startActivity(intent);
            }
        };


        private void toggleCardViewnHeight(int height) {
            if (cv.getHeight() == minHeight) {
                recipeCode.setVisibility(View.VISIBLE);
                expandView(height);

            } else {
                recipeCode.setVisibility(View.GONE);
                collapseView();
            }
        }

        public void collapseView() {
            ValueAnimator anim = ValueAnimator.ofInt(cv.getMeasuredHeightAndState(),
                    minHeight);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                    layoutParams.height = val;
                    cv.setLayoutParams(layoutParams);
                }
            });
            anim.start();
        }

        public void expandView(int height) {
            ValueAnimator anim = ValueAnimator.ofInt(cv.getMeasuredHeightAndState(),
                    height);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                    layoutParams.height = val;
                    layoutParams.height = recipeCode.getHeight() + minHeight + 15;
                    cv.setLayoutParams(layoutParams);
                }
            });
            anim.start();
        }

        public void initheight(int height) {
            ValueAnimator anim = ValueAnimator.ofInt(cv.getMeasuredHeightAndState(),
                    height);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    recipeCode.setVisibility(View.GONE);
                    ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                    layoutParams.height = 400;
                    cv.setLayoutParams(layoutParams);
                }
            });
            anim.start();
        }
    }
}