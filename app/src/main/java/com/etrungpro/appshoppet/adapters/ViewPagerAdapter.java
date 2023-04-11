package com.etrungpro.appshoppet.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.etrungpro.appshoppet.ui.FeedFragment;
import com.etrungpro.appshoppet.ui.HomeFragment;
import com.etrungpro.appshoppet.ui.OrderFragment;
import com.etrungpro.appshoppet.ui.UserFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position) {
           case 0 :
               return new HomeFragment();
           case 1 :
               return new FeedFragment();
           case 2 :
               return new OrderFragment();
           case 3 :
               return new UserFragment();
           default:
               throw new IllegalStateException("Unexpected value: " + position);
       }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
