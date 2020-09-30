package com.example.kartikonlinefirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.kartikonlinefirebase.activities.LoginToFireStore;
import com.example.kartikonlinefirebase.adapters.AdminTabsViewPagerAdapter;
import com.example.kartikonlinefirebase.fragments.AdminCatalogueFragment;
import com.example.kartikonlinefirebase.fragments.AdminHomeFragment;
import com.example.kartikonlinefirebase.fragments.AdminOtherFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;

    String userID, userName;
    ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userID = fAuth.getCurrentUser().getUid();

        fragments = new ArrayList<>();

        initViews();
        initActionBar();
        initViewPager();

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //TODO: implement a toggle listener to update values of switches in realtime
        //TODO: write firebase code to set all the product and catalogue related data

        /*navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_diff:
                        Switch navDiffSwitch = (Switch) item.getActionView();
                        navDiffSwitch.toggle();
                        break;
                    case R.id.nav_store:
                        Switch navStoreSwitch = (Switch) item.getActionView();
                        navStoreSwitch.toggle();
                        break;
                    case R.id.nav_cod:
                        Switch navCodSwitch = (Switch) item.getActionView();
                        navCodSwitch.toggle();
                        break;
                }
                return true;
            }
        });*/




/*        DocumentReference documentReference = fstore.collection("users").document(userID);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("RegisterToFireStore", "User profile updated.");
                        }
                    }
                });*/

    }

    public void initViews(){
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.v_pager_admin);
    }

    public void initActionBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Admin");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.item_home:
                Toast.makeText(getApplicationContext(), "home clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_logout:
                fAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginToFireStore.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViewPager() {
        fragments.add(new AdminHomeFragment());
        fragments.add(new AdminCatalogueFragment());
        fragments.add(new AdminOtherFragment());
        AdminTabsViewPagerAdapter adminTabs = new AdminTabsViewPagerAdapter(
                getSupportFragmentManager(),this, fragments);
        mViewPager.setAdapter(adminTabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("Home");
        tabLayout.getTabAt(1).setText("Catalogue");
        tabLayout.getTabAt(2).setText("Other");
    }
}

//TODO(1): Make a common ViewPagerAdapter for existing view pagers and tablayouts
//TODO(2): Safely remove activities which are already been copied inside fragments