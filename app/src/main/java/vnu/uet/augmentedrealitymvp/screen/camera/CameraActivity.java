package vnu.uet.augmentedrealitymvp.screen.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.artoolkit.ar.base.camera.CameraPreferencesActivity;

import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.screen.main.MainActivity;


public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("nftBookNative");
    }

    private GLSurfaceView glView;
    private CameraSurface camSurface;
    private int wCam, hCam;
    private FrameLayout mainLayout;
    private Button btOpenMarker;

    public static native boolean nativeCreate(Context ctx);

    public static native boolean nativeStart();

    public static native boolean nativeStop();

    public static native boolean nativeDestroy();

    // Camera functions.
    public static native boolean nativeVideoInit(int w, int h, int cameraIndex, boolean cameraIsFrontFacing);

    public static native void nativeVideoFrame(byte[] image);

    // OpenGL functions.
    public static native void nativeSurfaceCreated();

    public static native void nativeSurfaceChanged(int w, int h);

    public static native void nativeDrawFrame();

    // Other functions.
    public static native void nativeDisplayParametersChanged(int orientation, int w, int h, int dpi); // 0 = portrait, 1 = landscape (device rotated 90 degrees ccw), 2 = portrait upside down, 3 = landscape reverse (device rotated 90 degrees cw).

    public static native void nativeSetInternetState(int state);

    /** Called when the activity is first created. */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        boolean needActionBar = false;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                if (!ViewConfiguration.get(this).hasPermanentMenuKey()) needActionBar = true;
//            } else {
//                needActionBar = true;
//            }
//        }
//        if (needActionBar) {
//            requestWindowFeature(Window.FEATURE_ACTION_BAR);
//        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // Force landscape-only.
        updateNativeDisplayParameters();

        setContentView(R.layout.activity_camera);
        btOpenMarker = (Button)findViewById(R.id.btOpenMarker);
        btOpenMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CameraActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        CameraActivity.nativeCreate(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        mainLayout = (FrameLayout)this.findViewById(R.id.mainLayoutDetect);

        CameraActivity.nativeStart();
    }

    @SuppressWarnings("deprecation") // FILL_PARENT still required for API level 7 (Android 2.1)
    @Override
    public void onResume() {
        super.onResume();

        // Update info on whether we have an Internet connection.
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        nativeSetInternetState(isConnected ? 1 : 0);

        // In order to ensure that the GL surface covers the camera preview each time onStart
        // is called, remove and add both back into the FrameLayout.
        // Removing GLSurfaceView also appears to cause the GL surface to be disposed of.
        // To work around this, we also recreate GLSurfaceView. This is not a lot of extra
        // work, since Android has already destroyed the OpenGL context too, requiring us to
        // recreate that and reload textures etc.

        // Create the camera view.
        camSurface = new CameraSurface(this);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(wCam,hCam));
        // Create/recreate the GL view.
        glView = new GLSurfaceView(this);
        //glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Do we actually need a transparent surface? I think not, (default is RGB888 with depth=16) and anyway, Android 2.2 barfs on this.
        glView.setRenderer(new Renderer());
        glView.setZOrderMediaOverlay(true); // Request that GL view's SurfaceView be on top of other SurfaceViews (including CameraPreview's SurfaceView).
        mainLayout.addView(camSurface, new LayoutParams(128, 128));
//        mainLayout.addView(glView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        mainLayout.addView(glView, new LayoutParams(wCam, hCam));


        if (glView != null) glView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glView != null) glView.onPause();

        // System hardware must be release in onPause(), so it's available to
        // any incoming activity. Removing the CameraPreview will do this for the
        // camera. Also do it for the GLSurfaceView, since it serves no purpose
        // with the camera preview gone.
        mainLayout.removeView(glView);
        mainLayout.removeView(camSurface);
    }

    @Override
    public void onStop() {
        super.onStop();

        CameraActivity.nativeStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        CameraActivity.nativeDestroy();
    }

    private void updateNativeDisplayParameters()
    {
        Display d = getWindowManager().getDefaultDisplay();
        int orientation = d.getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        int dpi = dm.densityDpi;
        Log.e("cxz","h " + h + " "+w);
        hCam = h;
        wCam = hCam *4/3;
        nativeDisplayParametersChanged(orientation, wCam, hCam, dpi);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        // We won't use the orientation from the config, as it only tells us the layout type
        // and not the actual orientation angle.
        //int nativeOrientation;
        //int orientation = newConfig.orientation; // Only portrait or landscape.
        //if (orientation == Configuration.ORIENTATION_LANSCAPE) nativeOrientation = 0;
        //else /* orientation == Configuration.ORIENTATION_PORTRAIT) */ nativeOrientation = 1;
        updateNativeDisplayParameters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, CameraPreferencesActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}