package com.irvandwiputra.skripsimentee;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.irvandwiputra.skripsimentee.Fragment.HistoryFragment;
import com.irvandwiputra.skripsimentee.Fragment.HomeFragment;
import com.irvandwiputra.skripsimentee.Fragment.MyAccountFragment;
import com.irvandwiputra.skripsimentee.Utility.Constant;
import com.irvandwiputra.skripsimentee.Utility.TinyDB;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public FragmentManager fragmentManager = getSupportFragmentManager();

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        TinyDB tinyDB = new TinyDB(getApplicationContext());

        Log.i(TAG, "onCreate: " + tinyDB.getString(Constant.TOKEN));

        fragmentManager.beginTransaction().replace(R.id.content_fragment, new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Sure")
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TinyDB tinyDB = new TinyDB(getApplicationContext());
                            tinyDB.clear();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragmentManager.beginTransaction().replace(R.id.content_fragment, new HomeFragment()).commit();
                break;
            case R.id.nav_history:
                fragmentManager.beginTransaction().replace(R.id.content_fragment, new HistoryFragment()).commit();
                break;
            case R.id.nav_help:
                break;
            case R.id.nav_my_account:
                fragmentManager.beginTransaction().replace(R.id.content_fragment, new MyAccountFragment()).commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
