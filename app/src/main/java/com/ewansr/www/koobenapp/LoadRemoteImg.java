package com.ewansr.www.koobenapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Saulo Euan on 07/06/2016.
 */
class LoadRemoteImg extends AsyncTask<String, Void, Bitmap> {
    public Context context;
    private Exception exception;
    private ImageView img;
    private CircleImageView circleImg;
    private AppBarLayout Abl;

    public LoadRemoteImg(CircleImageView circleImg){
        this.circleImg = circleImg;
    }
    public LoadRemoteImg(ImageView img){
        this.img = img;
    }

    public LoadRemoteImg(AppBarLayout Abl, Context context){
        this.Abl = Abl;
        this.context = context;
    }

    protected Bitmap doInBackground(String... params) {
        try {

            URL url = null;
            try {
                url = new URL(params[0].toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmp;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(Bitmap bmp) {
        if (bmp != null && img != null) {
            img.setImageBitmap(bmp);
        }

        if (bmp != null && circleImg != null) {
            circleImg.setImageBitmap(bmp);
        }

        if (bmp != null && Abl != null) {
            Drawable d = new BitmapDrawable(context.getResources(), bmp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Abl.setBackground(d);
            }
        }
    }
}