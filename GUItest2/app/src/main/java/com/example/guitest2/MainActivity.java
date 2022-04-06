package com.example.guitest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView navigationTV;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        //for testing if no db created,create 2 record
        //if no record created
        if(dbHelper.getCount() <= 0){
            dbHelper.insertContact("Item 1", "this is item 1", "100", "item1_low", "item1_high");
            dbHelper.insertContact("Item 2", "this is item 2", "200", "item1_low", "item1_high");
        }

        navigationTV = findViewById(R.id.navigationTV);
        drawerLayout = findViewById(R.id.drawer_layout);
        loadFragment(new HomeFragment());
    }

    //Menu
    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    //Home button function
    public void ClickHome(View view){
        closeDrawer(drawerLayout);
        navigationTV.setText("Home");
        loadFragment(new HomeFragment());
    }

    //About us button function
    public void ClickAboutUs(View view){
        closeDrawer(drawerLayout);
        navigationTV.setText("About Us");
        loadFragment(new AboutUsFragment());
    }

    public void ClickLogout(View view){
        logout(this);
    }

    public void ClickEmpty(View view){
        closeDrawer(drawerLayout);
    }

    //logout button function
    public void logout(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure that you want to logout");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
            }
        });
        builder.show();
    }

    // buy and upload button
    public void onButtonClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.buy:
                intent = new Intent(getApplicationContext(), ItemListActivity.class);
                startActivity(intent);
                break;
            case R.id.upload:
                intent = new Intent(getApplicationContext(), UploadActivity.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }


    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


    private void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            logout(this);
            return true;
        }
        else
            return super.onKeyDown(keyCode, e);
    }
}
