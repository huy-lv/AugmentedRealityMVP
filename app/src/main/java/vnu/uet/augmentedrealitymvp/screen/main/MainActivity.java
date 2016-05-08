package vnu.uet.augmentedrealitymvp.screen.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.base.BaseActivityDrawer;
import vnu.uet.augmentedrealitymvp.common.Constants;
import vnu.uet.augmentedrealitymvp.helper.SessionManager;
import vnu.uet.augmentedrealitymvp.screen.camera.CameraActivity;
import vnu.uet.augmentedrealitymvp.screen.fragment.marker.MarkerFragment;
import vnu.uet.augmentedrealitymvp.screen.fragment.profile.ProfileFragment;
import vnu.uet.augmentedrealitymvp.screen.fragment.setting.SettingsFragment;
import vnu.uet.augmentedrealitymvp.screen.login.LoginActivity;
import vnu.uet.augmentedrealitymvp.screen.viewcache.ViewCacheActivity;

public class MainActivity extends BaseActivityDrawer<MainPresenter> implements MainView,NavigationView.OnNavigationItemSelectedListener {

    Fragment markerFragment;
    Fragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager session = new SessionManager(getApplicationContext());

        nav_header_email.setText(session.getValue("email"));
        nav_header_username.setText(session.getValue("username"));

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                profileFragment = new ProfileFragment();
                fragmentTransaction.add(R.id.mainLayout, profileFragment).addToBackStack(null);
                fragmentTransaction.commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


        markerFragment = new MarkerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, markerFragment).commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresenter onCreatePresenter() {
        return new MainPresenterImpl(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_marker) {
            Fragment markerFragment = new MarkerFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, markerFragment).commit();
        } else if (id == R.id.nav_data) {

        } else if (id == R.id.nav_settings) {
            Fragment settingsFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mainLayout, settingsFragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(this)
                    .setMessage("Would you like to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SessionManager session = new SessionManager(getApplicationContext());
                            session.setLogin(false);
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_cache:
                deleteCache(this);
                break;
            case R.id.menu_view_cache:
                viewCache(this);
                break;
        }
        return true;
    }

    void viewCache(Context context) {
        File cacheDir = context.getCacheDir();
        File cacheNFT = new File(cacheDir + "/DataNFT");
        if (cacheNFT.exists()) {
            File[] files = cacheNFT.listFiles();
            ArrayList<String> values = new ArrayList<>();
            for (File f : files) {
                values.add(f.getName());
            }
            Intent i = new Intent(MainActivity.this, ViewCacheActivity.class);
            i.putStringArrayListExtra(Constants.KEY_INTENT_FILES_LIST, values);
            startActivity(i);
        } else {
            showErrorDialog("cache NFT did not exists!");
        }
    }

    public void deleteCache(Context context) {
        try {
            File dir = new File(context.getCacheDir() + "/DataNFT");
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
        }
    }

    public void deleteDir(File dir) {
        for (File f : dir.listFiles()) {
            f.delete();
        }
    }
}
