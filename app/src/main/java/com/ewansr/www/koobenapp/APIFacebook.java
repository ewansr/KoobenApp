package com.ewansr.www.koobenapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by EwanS on 11/07/2016.
 */
public class APIFacebook {
    /**
     * para obtener los valores del perfil de Facebook con GraphApi es necesario pasarle
     * @param aToken de tipo AccessToken
     * @param faToken (El token actual con el que se encuentra logeado) de tipo string
     * @return GraphRequest
     */

    public Bundle bFacebookData;

    public  GraphRequest getDataFb(AccessToken aToken, final String faToken, final CircleImageView imgFB){
        GraphRequest request = GraphRequest.newMeRequest(aToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i("LoginActivity", response.toString());
                // Obtener datos de facebook
                bFacebookData = getFacebookData(object, imgFB);
                SuccessAuth( bFacebookData );
            }
        });
        return request;
    }


    public void SuccessAuth( Bundle datos ){

    }


    @SuppressLint("LongLogTag")
    public  Bundle getFacebookData(JSONObject object, ImageView imgProfile) {
        Bundle bundle = new Bundle();
        String id = null;
        try {
            id = object.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String UrlImgProfile = "https://graph.facebook.com/" + id + "/picture?width=200&height=150";
        bundle.putString("profile_pic", UrlImgProfile);
        LoadRemoteImg rimg = new LoadRemoteImg(imgProfile);
        rimg.execute( UrlImgProfile );

        // Lo siguiente está de más explicarlo con manzanas
        bundle.putString("idFacebook", id);
        try {
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Error al obtener información del perfil de FB", e.getMessage());
        }
        return bundle;
    }
}
