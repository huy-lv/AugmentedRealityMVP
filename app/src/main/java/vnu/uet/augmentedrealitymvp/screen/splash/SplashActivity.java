package vnu.uet.augmentedrealitymvp.screen.splash;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.common.Constants;
import vnu.uet.augmentedrealitymvp.gcm.QuickstartPreferences;
import vnu.uet.augmentedrealitymvp.gcm.RegistrationIntentService;
import vnu.uet.augmentedrealitymvp.helper.SessionManager;
import vnu.uet.augmentedrealitymvp.screen.login.LoginActivity;
import vnu.uet.augmentedrealitymvp.screen.main.MainActivity;

/**
 * Created by huylv on 17/02/2016.
 */
public class SplashActivity extends Activity {
    private boolean LOGGED_IN = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    private static final String TAG = "SplashScreenActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private SessionManager session;

    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplicationContext());

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                token = intent.getStringExtra("reg_token");
                session.setRegToken(token);

                if (!token.equals("")) {
                    Intent i;
                    if (session.isLoggedIn()) {
                        i = new Intent(SplashActivity.this, MainActivity.class);

                    } else {
                        i = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    startActivity(i);
                    finish();
                } else {
                    String temp = getString(R.string.token_error_message);
                    Toast.makeText(SplashActivity.this, temp, Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerReceiver();
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LOGGED_IN) {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, Constants.SPLASH_TIME_OUT);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

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
