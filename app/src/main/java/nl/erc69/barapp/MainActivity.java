package nl.erc69.barapp;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements PlusOneFragment.OnFragmentInteractionListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.order_grid_fragment,new OrderMenu()).commit();
            setTitle(R.string.order_menu);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Swaps fragments in the main content view */
    private void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on position
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        switch (menuItem.getItemId()){
            case R.id.nav_first_fragment:
                fragment = new OrderMenu();
                break;
            case R.id.nav_second_fragment:
                fragment = new ConfigureOrderMenu();
                break;
            default:
                fragment = new OrderMenu();
                break;
        }

        transaction.replace(R.id.order_grid_fragment, fragment);
        transaction.commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    public void orderItemSelected(int categoryPosition,int itemPosition){
        Bundle bundle = lookupItemBundle(categoryPosition,itemPosition);
        OrderItemSelectedDialog fragment = new OrderItemSelectedDialog();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),"dialog");

    }

    public void orderItemSelected(int categoryPosition,int itemPosition,int linePosition,int orderAmount){
        Bundle bundle = lookupItemBundle(categoryPosition,itemPosition);
        bundle.putInt(Receipt.ORDER_AMOUNT,orderAmount);
        bundle.putInt(Receipt.LINE_POSITION,linePosition);

        ReceiptLineSelectedDialog fragment = new ReceiptLineSelectedDialog();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),"dialog");
    }


    public void addLineReceipt(int categoryPosition,int itemPosition,int orderAmount){
        OrderReceipt orderReceipt = (OrderReceipt) getSupportFragmentManager().findFragmentById(R.id.order_receipt_fragment);

        if (orderReceipt == null){
            Bundle bundle = lookupItemBundle(categoryPosition,itemPosition);
            bundle.putInt(Receipt.ORDER_AMOUNT,orderAmount);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            orderReceipt = new OrderReceipt();
            orderReceipt.setArguments(bundle);
            transaction.replace(R.id.order_receipt_fragment,orderReceipt);
            transaction.commit();
        } else {
            orderReceipt.addLineReceipt(categoryPosition,itemPosition,orderAmount);
        }
    }

    public void updateLineReceipt(int linePosition,int orderAmount){
        OrderReceipt orderReceipt = (OrderReceipt) getSupportFragmentManager().findFragmentById(R.id.order_receipt_fragment);
        orderReceipt.updateLineReceipt(linePosition,orderAmount);
    }

    public void deleteLineReceipt(int linePosition){
        OrderReceipt orderReceipt = (OrderReceipt) getSupportFragmentManager().findFragmentById(R.id.order_receipt_fragment);
        orderReceipt.deleteLineReceipt(linePosition);
    }

    private Bundle lookupItemBundle(int categoryPosition, int itemPosition){
        Bundle bundle = new Bundle();
        bundle.putInt(Category.CATEGORY_POSITION,categoryPosition);
        bundle.putInt(Category.ITEM_POSITION,itemPosition);
        return bundle;
    }


    public void onFragmentInteraction(int categoryNumber) {

    }





}
