package com.etrungpro.appshoppet.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.models.Product;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.Slide;
import com.etrungpro.appshoppet.adapters.SlideAdapter;
import com.etrungpro.appshoppet.activities.CategoryActivity;
import com.etrungpro.appshoppet.activities.DetailActivity;
import com.etrungpro.appshoppet.adapters.CategoryAdapter;
import com.etrungpro.appshoppet.models.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {


    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private ViewPager2 slider;
    private SlideAdapter slideAdapter;
    private ArrayList<Slide> mListSlide;
    private CircleIndicator3 circleIndicator3;
    private ImageView banner;
    ArrayList<Category> categories = new ArrayList<>();
    private Timer timer;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        rcvCategory = v.findViewById(R.id.rcv_category);
        slider = v.findViewById(R.id.slider);
        circleIndicator3 = v.findViewById(R.id.circleIndicator);
        banner = v.findViewById(R.id.banner);
        Glide.with(getActivity()).load("https://firebasestorage.googleapis.com/v0/b/pet-shop-app-9a3f9.appspot.com/o/matpet.png?alt=media&token=6e2f1f02-134e-4495-bfa4-b0a92b467f90")
                .into(banner);
        categoryAdapter = new CategoryAdapter(getContext(), new CategoryAdapter.ICategoryEnvent() {
            @Override
            public void showProductDetail(Product product) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productName", product.getName());
                bundle.putInt("productPrice", product.getPrice());
                bundle.putStringArrayList("productImages", product.getImg());
                bundle.putString("productId", product.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void showProductList(String category) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("categoryName", category);
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);
        rcvCategory.setAdapter(categoryAdapter);
        getCategoryList();
        mListSlide = getListSlide();
        slideAdapter = new SlideAdapter(getContext(), mListSlide);
        slider.setAdapter(slideAdapter);
        circleIndicator3.setViewPager(slider);
        slideAdapter.registerAdapterDataObserver(circleIndicator3.getAdapterDataObserver());
        autoSlideImages();

        return v;
    }

    private void getCategoryList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String category = document.getString("name");
                    getProductCategoryList(category);
                }
            }
        });
    }

    private void autoSlideImages() {
        if(mListSlide == null || mListSlide.isEmpty() || slider == null) {
            return ;
        }

        // Init timer
        if(timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = slider.getCurrentItem();
                        int totalItem = mListSlide.size() - 1;
                        if(currentItem < totalItem) {
                            currentItem += 1;
                            slider.setCurrentItem(currentItem);;
                        }
                        else {
                            currentItem = 0;
                            slider.setCurrentItem(0);
                        }
                    }


                });
            }
        }, 500, 3000);
    }

    private ArrayList<Slide> getListSlide() {
        ArrayList<Slide> list= new ArrayList<>();

        list.add(new Slide(R.drawable.slider_1));
        list.add(new Slide(R.drawable.slider_2));
        list.add(new Slide(R.drawable.slider_3));

        return list;
    }

    private void getProductCategoryList(String category) {
        ArrayList<Product> products = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .whereEqualTo("category", category)
                .limit(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String productId = document.getId();
                                Product product = document.toObject(Product.class);
                                product.setId(productId);
                                products.add(product);
                            }
                            categories.add(new Category(category, products));
                            categoryAdapter.setData(categories);
                        } else {
                            Toast.makeText(getContext(), "False to get collection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}