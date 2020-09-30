package com.example.kartikonlinefirebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kartikonlinefirebase.MainActivity;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.adapters.AdminTabsViewPagerAdapter;
import com.example.kartikonlinefirebase.adapters.ProductTabsViewPagerAdapter;
import com.example.kartikonlinefirebase.fragments.AdminCatalogueFragment;
import com.example.kartikonlinefirebase.fragments.AdminHomeFragment;
import com.example.kartikonlinefirebase.fragments.AdminOtherFragment;
import com.example.kartikonlinefirebase.fragments.CatalogueItemInfoFragment;
import com.example.kartikonlinefirebase.fragments.CatalogueItemInventoryFragment;
import com.example.kartikonlinefirebase.fragments.CatalogueItemNotesFragment;
import com.example.kartikonlinefirebase.interfaces.OnMenuSaveButonClickListener;
import com.example.kartikonlinefirebase.interfaces.TextWatcherInterface;
import com.example.kartikonlinefirebase.models.Product;
import com.example.kartikonlinefirebase.utils.BitmapTransformer;
import com.example.kartikonlinefirebase.utils.TabChangedEvent;
import com.example.kartikonlinefirebase.utils.TextChangedEvent;
import com.example.kartikonlinefirebase.viewmodels.ProductViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import io.opencensus.common.ToLongFunction;

import static com.example.kartikonlinefirebase.utils.Config.mStaticProduct;


public class EditProductInfoActivity extends AppCompatActivity {

    private static final String TAG = "EditProductInfoActivity";
    String catalogueName;
    String catalogueId = "";
    TabLayout tabLayout;
    ViewPager mViewPager;
    ImageView productImageView;
    Toolbar toolbar;
    Intent catalogueMainIntent;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    Bitmap photo;
    //EventBus bus = EventBus.getDefault();
    //EventBus mBus = EventBus.getDefault();
    FloatingActionButton saveProductInfoFab;
//    OnMenuSaveButonClickListener mCallback;

//    public Product product;
    ArrayList<Fragment> fragments = new ArrayList<>();
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private FirebaseFirestore mFirestore;
    private CollectionReference productCollection;
    private CollectionReference catalogueCollection;

    private ProductViewModel productViewModel;
    private boolean isFabShowing = false;

    Animation show_fab;


    @Override
    public void onStart() {
        super.onStart();
        //mBus.register(this);
        //bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //mBus.unregister(this);
        //bus.unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_info);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.v_pager_prod);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        saveProductInfoFab = (FloatingActionButton) findViewById(R.id.save_prod_info_fab);
        collapsingToolbarLayout = findViewById(R.id.collap_toolbar_layout);
        productImageView = findViewById(R.id.iv_header);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Product Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catalogueMainIntent = getIntent();

        if(catalogueMainIntent != null) {
            Uri imageUri = Uri.parse(catalogueMainIntent.getStringExtra("imageUri"));
            catalogueName = catalogueMainIntent.getStringExtra("catalogueName");
            catalogueId = catalogueMainIntent.getStringExtra("catalogueId");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Glide.with(this).load(bitmap).transform(new BitmapTransformer(MAX_WIDTH, MAX_HEIGHT)).into(productImageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        saveProductInfoFab.setY(300);
        show_fab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show);


        mFirestore = FirebaseFirestore.getInstance();

        productCollection = mFirestore.collection("products");
        catalogueCollection = mFirestore.collection("catalogues");

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        productViewModel.eventDataChanged.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean){
                    showFab();
                    //saveProductInfoFab.startAnimation(show_fab);

                }

            }
        });

        saveProductInfoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Product product = productViewModel.onClickSaveButton();
                product.setCatalogueId(catalogueId);

                productCollection.add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.v("product table", documentReference.getId());
                    }
                });

                final HashMap<String, Object> mCatalogue = new HashMap<>();
                mCatalogue.put("catalogueTitle", catalogueName);
                catalogueCollection.whereEqualTo("catalogueTitle", catalogueName)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            int count = 0;
                            String docFoundId = "";
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for(DocumentSnapshot document: queryDocumentSnapshots){
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    count++;
                                    if (count == 1){
                                        docFoundId = document.getId();
                                    }
                                }

                                Log.v("documents", queryDocumentSnapshots.getDocuments().toString());

                                if(queryDocumentSnapshots.isEmpty()){
                                    catalogueCollection.add(mCatalogue).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {

                                            /*DocumentReference documentReference = catalogueCollection
                                                    .document(task.getResult().getId())
                                                    .collection("products").document();

                                            documentReference.set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(EditProductInfoActivity.this,
                                                            "product set ", Toast.LENGTH_SHORT).show();
                                                }
                                            });*/
                                        }
                                    });
                                }

                                /*else{

                                    DocumentReference documentReference = catalogueCollection
                                            .document(docFoundId)
                                            .collection("products").document();

                                    documentReference.set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(EditProductInfoActivity.this,
                                                    "product set ", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }*/

                                /*for (DocumentSnapshot document : task.getResult()) *//*{
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    count++;
                                    if (count == 1){
                                        docFoundId = document.getId();
                                    }
                                }*/


                            }
                        });
                        /*.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {


                                    else if(!task.isSuccessful()){



                                    }


                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());


                                }
                            }
                        });
*/

            }
        });




//        mCallback = (OnMenuSaveButonClickListener) this;

//        product = new Product();
        fragments.add(new CatalogueItemInfoFragment());
        fragments.add(new CatalogueItemInventoryFragment());
        fragments.add(new CatalogueItemNotesFragment());
        initViewPager();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //bus.post(new TabChangedEvent());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




//        Gson gson = new Gson();
//        Type type = new TypeToken<List<Student>>() {}.getType();
//        String json = gson.toJson(students, type);
//        Intent intent = new Intent(getBaseContext(), YourActivity.class);
//        intent.putExtra(YourNextActivity.ADDITIONAL_STUDENTS, json);

        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.prof_pic);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {

                    int vibrantColor = palette.getVibrantColor(R.color.colorAccent);
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimary);
                    collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                }
            });

        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            Log.e(TAG, "onCreate: failed to create bitmap from background", e.fillInStackTrace());
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.colorAccent)
            );
            collapsingToolbarLayout.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
            );
        }



    }

    @Subscribe
    public void onEvent(TextChangedEvent event){
        saveProductInfoFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "product saved", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    public boolean onSupportNavigateUp() {
        //Toast.makeText(this, "item saved", Toast.LENGTH_SHORT).show();
        onBackPressed();
        return true;
    }



    private void initViewPager() {

        ProductTabsViewPagerAdapter productTabs = new ProductTabsViewPagerAdapter(
                getSupportFragmentManager(),this, fragments);
        mViewPager.setAdapter(productTabs);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("Product Info");
        tabLayout.getTabAt(1).setText("Inventory");
        tabLayout.getTabAt(2).setText("Notes");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_check:
//                mCallback.onMenuButonClick();
                int selectedTab = tabLayout.getSelectedTabPosition();
                ((OnMenuSaveButonClickListener) fragments.get(selectedTab)).onMenuButonClick();
                return true;

            default: return super.onOptionsItemSelected(item);
        }

    }

    private void hideFab() {
        if (isFabShowing) {
            isFabShowing = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                final Point point = new Point();
                getWindow().getWindowManager().getDefaultDisplay().getSize(point);
                final float translation = saveProductInfoFab.getY() - point.y;
                saveProductInfoFab.animate().translationYBy(-translation).start();
            } else {
                Animation animation = AnimationUtils.makeOutAnimation(this, true);
                animation.setFillAfter(true);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        saveProductInfoFab.setClickable(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                saveProductInfoFab.startAnimation(animation);
            }
        }
    }

    private void showFab() {
        if (!isFabShowing) {
            isFabShowing = true;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                saveProductInfoFab.animate().translationY(0).setDuration(60).start();
            } else {
                Animation animation = AnimationUtils.makeInAnimation(this, false);
                animation.setFillAfter(true);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        saveProductInfoFab.setClickable(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                saveProductInfoFab.startAnimation(animation);
            }
        }
    }



}
