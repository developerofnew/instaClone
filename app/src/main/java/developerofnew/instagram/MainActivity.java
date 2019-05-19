package developerofnew.instagram;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import developerofnew.SharedPreferenceManager;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionDrawerToggle;
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //layout variables
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        mNavigationView = findViewById(R.id.main_nav_view);

        mActionDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mActionDrawerToggle);
        mActionDrawerToggle.syncState();


        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setTitle("Instagram");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burger);

        //Default fragment to be displayed

        changeFragmentDisplay(R.id.home);

        //listener for navigation view
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                changeFragmentDisplay(item.getItemId());

            return  true;
            }
        });



    }



    private void changeFragmentDisplay(int item){

        Fragment fragment = null;

        if(item == R.id.home){


            fragment = new HomeFragment();


        } else if(item == R.id.search){

           fragment = new SearchFragment();
            Toast.makeText(this, "check", Toast.LENGTH_SHORT).show();

        }else if(item == R.id.camera){

            fragment = new CameraFragment();

        }
        else if(item == R.id.likes){

            fragment = new LikesFragment();

        }

        else if(item == R.id.profile){

           fragment = new ProfileFragment();

        }

        else if(item == R.id.log_out){

           SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(getApplicationContext());
           sharedPreferenceManager.logUserOut();
           startActivity(new Intent(MainActivity.this,LoginActivity.class));

        }

        else {

            Toast.makeText(MainActivity.this,"error",Toast.LENGTH_LONG).show();
        }

        //hide navigationdrawer
        mDrawerLayout.closeDrawer(Gravity.START);

        if(fragment != null){

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment_content,fragment);
            ft.commit();
        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mActionDrawerToggle.onOptionsItemSelected(item)){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    //    @Override
//    protected void onStart() {
//        super.onStart();
//
//        boolean isUserLoggedIn = SharedPreferenceManager.getInstance(getApplicationContext()).isUserLoggedIn();
//
//        if(!isUserLoggedIn){
//
//            startActivity(new Intent(MainActivity.this,LoginActivity.class));
//        } else {
//
//
//        }
//    }

















}
