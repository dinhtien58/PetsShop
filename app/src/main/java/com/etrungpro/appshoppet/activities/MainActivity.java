package com.etrungpro.appshoppet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    BottomNavigationView bottomNav;
    SearchView searchView;
    ImageView btnMess;
    ImageView btnCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.view_pager);
        bottomNav = findViewById(R.id.bottom_nav);
        searchView = findViewById(R.id.search_view);
        btnMess = findViewById(R.id.mess_btn);
        btnCart = findViewById(R.id.cart_btn);
        setUpViewPager();

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.action_feed:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.action_order:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.action_user:
                        viewPager2.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("searchValue", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
                startActivity(intent);
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNav.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNav.getMenu().findItem(R.id.action_feed).setChecked(true);
                        break;
                    case 2:
                        bottomNav.getMenu().findItem(R.id.action_order).setChecked(true);
                        break;
                    case 3:
                        bottomNav.getMenu().findItem(R.id.action_user).setChecked(true);
                        break;

                }
            }
        });
    }
}