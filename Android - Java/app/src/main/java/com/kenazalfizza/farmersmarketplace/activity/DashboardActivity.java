package com.kenazalfizza.farmersmarketplace.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.dashboard.HomeFragment;
import com.kenazalfizza.farmersmarketplace.dashboard.OrderListFragment;
import com.kenazalfizza.farmersmarketplace.dashboard.ProfileFragment;
import com.kenazalfizza.farmersmarketplace.dashboard.StoreEmptyFragment;
import com.kenazalfizza.farmersmarketplace.dashboard.StoreFragment;
import com.kenazalfizza.farmersmarketplace.community.CommunityJoinActivity;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreCurrent;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Declaring local variables
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    StoreFragment storeFragment = new StoreFragment();
    StoreEmptyFragment storeEmptyFragment = new StoreEmptyFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    OrderListFragment orderListFragment = new OrderListFragment();

    @Override
    // Override the blank activity when it is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Checking the current application view
        // Assigning local variable with their respective values
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home_selector);
        bottomNavigationView.setItemIconTintList(null);

    }

    // Method to check if a menu item is selected
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Switching between different fragments in dashboard when the menu item is pressed
        switch (item.getItemId()) {
            case R.id.nav_home_selector:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, homeFragment).commit();
                return true;

            case R.id.nav_profile_selector:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, profileFragment).commit();
                return true;

            case R.id.nav_store_selector:
                //Log.d(null, UserStoreCurrent.getStoreId().toString());
                if (UserStoreCurrent.getStoreId() == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, storeEmptyFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, storeFragment).commit();
                }
                return true;

            case R.id.nav_community_selector:
                Intent intent = new Intent(getApplicationContext(), CommunityJoinActivity.class);
                startActivity(intent);

                return true;

            case R.id.nav_order_selector:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, orderListFragment).commit();
                return true;
        }
        return false;
    }
}