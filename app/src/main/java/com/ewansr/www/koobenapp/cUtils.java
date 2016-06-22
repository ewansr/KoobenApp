package com.ewansr.www.koobenapp;

/**
 * Created by EwanS on 08/06/2016.
 */
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.io.InputStream;
import java.io.OutputStream;


public class cUtils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }


   /** Esta Función colorea la barra de estado en caso de ser versión compatible*/
    public static void setStatusColor(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.color_primario_obscuro_cyan));
        }
    }


}