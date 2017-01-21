package com.lastengineer.mapboxhidetest;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.MapboxAccountManager;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {

    public static final int RT_LOCATION_PERM = 123;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    private MapViewFragment mapViewFragmentOne;
    private MapViewFragment mapViewFragmentTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapboxAccountManager.start(this, "pk.eyJ1IjoiZGVqYW5yaXN0aWMiLCJhIjoicnNmZzBONCJ9.AdU8fjtSDKmjubgxVlNrlQ");
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fragmentManager = getFragmentManager();
        checkLocationPermission();

    }

    private void initBottomNavigation() {
        mapViewFragmentOne = MapViewFragment.newInstance(R.layout.fragment_map_view);
        mapViewFragmentTwo = MapViewFragment.newInstance(R.layout.fragment_map_view_two);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        addMainFragmentsToStack();
        showMapTabOne();
    }

    private void addMainFragmentsToStack() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, mapViewFragmentOne);
        transaction.add(R.id.fragment_container, mapViewFragmentTwo);
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }


    private void showMapTabOne() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mapViewFragmentTwo);
        transaction.show(mapViewFragmentOne);
        transaction.commit();
    }


    private void showMapTabTwo() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mapViewFragmentOne);
        transaction.show(mapViewFragmentTwo);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map_one:
                showMapTabOne();
                break;
            case R.id.action_map_two:
                showMapTabTwo();
                break;
        }
        return true;
    }


    @AfterPermissionGranted(RT_LOCATION_PERM)
    public void checkLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            initBottomNavigation();
            // Have permissions, do the thing!
        } else {
            // Ask for permission
            EasyPermissions.requestPermissions(this, "Location pls",
                    RT_LOCATION_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        initBottomNavigation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }
}
