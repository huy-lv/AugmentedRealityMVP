package vnu.uet.augmentedrealitymvp.base;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.TextView;

import vnu.uet.augmentedrealitymvp.R;

/**
 * Created by huylv on 22-Apr-16.
 */
public abstract class BaseActivityDrawer<T extends BasePresenter> extends BaseActivityToolbar<T> implements NavigationView.OnNavigationItemSelectedListener{

    protected TextView nav_header_username;
    protected TextView nav_header_email;
    protected View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nav_header_username = (TextView)header.findViewById(R.id.nav_header_username);
        nav_header_email = (TextView)header.findViewById(R.id.nav_header_email);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
