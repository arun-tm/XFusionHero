package com.teramatrix.xfusionhero;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import java.lang.reflect.Field;

/**
 * Created by arun.singh on 10/24/2016.
 */
public class XFusionHero extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("Application Classs");
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Roboto-Light.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Roboto-Light.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Roboto-Light.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Roboto-Light.ttf");


        // Setup handler for uncaught exceptions.
       Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });

    }
    public void handleUncaughtException (Thread thread, Throwable e)
    {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        Intent intent = new Intent (this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);
        trackException((Exception)e);
        System.exit(1); // kill off the crashed app
    }
    static final class FontsOverride {

        static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
            final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
            replaceFont(staticTypefaceFieldName, regular);
        }

        static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
            try {
                final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public Tracker getDefaultTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        Tracker mTracker = analytics.newTracker(R.xml.global_tracker);
        mTracker.enableExceptionReporting(true);

        return mTracker;
    }

    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getDefaultTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(
                                    new StandardExceptionParser(this, null)
                                            .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build()
            );
        }
    }

}
