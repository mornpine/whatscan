package com.jeroabd.whatsscan;


import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.jeroabd.whatsscan.gcm.RegistrationIntentService;
import com.jeroabd.whatsscan.utilities.AppPreferences;

import static com.google.android.gms.wearable.DataMap.TAG;


public class Online extends Activity {
    private AlertDialog AlertDialog;
    private SQLiteDatabase mydb;
    private String s1, s2, s3, s4, s5, s6;
    private WebView webView;
    private InterstitialAd interstitial;
    private AdRequest adRequest;
    // private AdView mAdView;
    private Dialogs cdd;
    private MediaPlayer m;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private boolean isReceiverRegistered;
    private boolean isOfferWallEnabled = false;
    private MenuItem removeAds = null;
    private boolean shouldEnableRemoveAds = false;

    private void enableRemoveAdsButton() {
//        if (shouldEnableRemoveAds) {
            removeAds.setEnabled(true);
//        }
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppPreferences.initialize(this);

        setContentView(R.layout.webcontent);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#043B69")));
        mydb = openOrCreateDatabase("rating", MODE_PRIVATE, null);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS RATES (id INTEGER PRIMARY KEY AUTOINCREMENT,RATING varchar);");
        Rate();

        String appKey = "9f12a65654fe5682ce0886130116a20bc7ffa4296287392c";
        Appodeal.setBannerViewId(R.id.appodealBannerView);
        Appodeal.setAutoCache(Appodeal.INTERSTITIAL, false);
        Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL | Appodeal.BANNER);
        // Appodeal.show(this, Appodeal.BANNER_BOTTOM);
        Appodeal.cache(this, Appodeal.INTERSTITIAL);

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            public void onInterstitialLoaded(boolean isPrecache) {
                Appodeal.show(Online.this, Appodeal.INTERSTITIAL);
            }
            public void onInterstitialFailedToLoad() { }
            public void onInterstitialShown() { }
            public void onInterstitialClicked() { }
            public void onInterstitialClosed() { }
        });

        Appodeal.show(this, Appodeal.BANNER_VIEW);

        // AdMob

        /*
        LinearLayout layout = (LinearLayout) findViewById(R.id.admobs);
        s1 = "c";
        s2 = "a";
        s3 = "p";
        s4 = "u";
        s5 = "b";
        s6 = "/";
        */

	    /*I N T E R S T I T I A L */
        // String AdsItr = s1 + s2 + "-" + s2 + s3 + s3 + "-" + s3 + s4 + s5 + "-" + "3495542477987820" + s6 + "7563790992";
        /*B A N N E R*/
        // String AdsBnr = s1 + s2 + "-" + s2 + s3 + s3 + "-" + s3 + s4 + s5 + "-" + "3495542477987820" + s6 + "6087057797";
        /*
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(AdsItr);
        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(AdsBnr);
        adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        layout.addView(mAdView);
        mAdView.loadAd(adRequest);

        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                interstitial.show();
            }
        });
        */

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setAllowFileAccess(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebChromeClient(new WebChromeClient() {


            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });


        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36 OPR/42.0.2393.9");
        webView.loadUrl("https://web.whatsapp.com/%F0%9F%8C%90/" + Locale.getDefault().getLanguage());

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                removeAds.setEnabled(true);
                shouldEnableRemoveAds = true;
                if (removeAds!=null) {
                    removeAds.setEnabled(true);
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.closing_app))
                .setMessage(getResources().getString(R.string.ask_app))
                .setPositiveButton(getResources().getString(R.string.rate_app), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String PACKAGE_NAME = getApplicationContext().getPackageName();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
                    }

                })
                .setNegativeButton(getResources().getString(R.string.exit_app), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                    }

                })
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        removeAds = menu.getItem(0);
        removeAds.setEnabled(false);

        if (shouldEnableRemoveAds) {
            removeAds.setEnabled(true);
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                String PACKAGE_NAME = getApplicationContext().getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out Whats Scan at: https://play.google.com/store/apps/details?id=" + PACKAGE_NAME);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.rating:
                String PACKAGE_NAME2 = getApplicationContext().getPackageName();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME2)));
                return true;
            case R.id.remove_ads:
                // Start activity for offerwall...
                Intent intent = new Intent(this, Offerwall.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Rate() {
        Cursor cursor4 = mydb.rawQuery("SELECT * FROM RATES LIMIT 1 ", null);

        if (cursor4.moveToFirst()) {
            do {


            } while (cursor4.moveToNext());
        } else {


            if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("العربية"))

            {
                int resourceId = R.raw.voices;
                m = MediaPlayer.create(Online.this, resourceId);
                m.setVolume(1f, 1f);

                m.start();
                cdd = new Dialogs(Online.this);
                cdd.setCancelable(false);
                cdd.show();
                cdd.yes.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cdd.dismiss();

                        String PACKAGE_NAME = getApplicationContext().getPackageName();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
                        mydb.execSQL("insert into RATES (RATING) VALUES ('YES');");

                    }

                });

                cdd.no.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cdd.dismiss();


                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                    }

                });


            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER);
        registerReceiver();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(AppPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}