package com.jimmy.htmlplayer;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Includes one-time initialization of Firebase related code
 */
public class ApplicationBase extends android.app.Application {

    private static ApplicationBase appInstance;
 

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;




        /**
        * print hash key (SHA1) in output window 
        */
        printFBHashCofigKey();

    }

    /**
     * instance of the application
     * @return instance of the application class
     */

    public static ApplicationBase getAppInstance(){
        return appInstance;

    }

    /**
     * reference to the root application context
     * @return the application context
     */
    public static Context getAppContext(){

        return appInstance.getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public void printFBHashCofigKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo( "com.jimmy.htmlplayer", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(" KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}