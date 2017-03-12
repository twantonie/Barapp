package nl.erc69.barapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.payleven.payment.api.ApiPaymentCompletedStatus;
import com.payleven.payment.api.ApiSalesHistoryCompletedStatus;
import com.payleven.payment.api.ApiTransactionDetailsCompletedStatus;
import com.payleven.payment.api.PaylevenApi;
import com.payleven.payment.api.PaylevenResponseListener;
import com.payleven.payment.api.TransactionRequest;

import java.util.Map;

import static com.payleven.payment.api.ApiPaymentCompletedStatus.*;


public class MainActivity extends AppCompatActivity implements PlusOneFragment.OnFragmentInteractionListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;

    private String DIALOG = "dialog";
    public static final String PAYMENT_FRAGMENT = "payment_fragment";
    public static String RECEIPT_TAG = "receipt_tag";

    private boolean editCategory = false;

    private boolean showFAB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_back);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStack();
            }
        });

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

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                shouldDisplayHomeUp();
            }
        });




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();

        if (Receipt.currentOrderReceipt != null){
            if (findViewById(R.id.order_receipt_fragment) != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.order_receipt_fragment,new OrderReceipt(),RECEIPT_TAG).commit();
                setFabButton(false);
            } else{
                setFabButton(true);
            }

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (editCategory){
            MenuItem menuItem = menu.add(0,R.id.menu_update_category,0,R.string.menu_update_category);
            menuItem.setIcon(R.drawable.ic_edit);

            MenuItemCompat.setShowAsAction(menuItem,MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            switch (item.getItemId()) {
                case R.id.menu_update_category:
                    int categoryPosition = ((ConfigureOrderMenu)getSupportFragmentManager().findFragmentById(R.id.order_grid_fragment)).getCurrentCategory();
                    updateCategory(categoryPosition);
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        PaylevenApi.handleIntent(requestCode, data, new PaylevenResponseListener() {
            @Override
            public void onOpenTransactionDetailsFinished(String s, Map<String, String> map, ApiTransactionDetailsCompletedStatus apiTransactionDetailsCompletedStatus) {

            }

            @Override
            public void onPaymentFinished(String s, TransactionRequest transactionRequest, Map<String, String> map, ApiPaymentCompletedStatus apiPaymentCompletedStatus) {
                switch (apiPaymentCompletedStatus){
                    case SUCCESS:
                        //Handle Payment
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),apiPaymentCompletedStatus.name(),Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onOpenSalesHistoryFinished(ApiSalesHistoryCompletedStatus apiSalesHistoryCompletedStatus) {

            }

            @Override
            public void onNoPaylevenResponse(Intent intent) {

            }
        });
    }

    /** Swaps fragments in the main content view */
    private void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on position
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        Bundle bundle = new Bundle();
        editCategory = false;

        switch (menuItem.getItemId()){
            case R.id.nav_first_fragment:
                fragment = new OrderMenu();
                bundle.putBoolean(OrderMenu.SHOW_FAB,showFAB);
                break;
            case R.id.nav_second_fragment:
                fragment = new ConfigureOrderMenu();
                editCategory = true;
                break;
            default:
                fragment = new OrderMenu();
                break;
        }

        fragment.setArguments(bundle);
        transaction.replace(R.id.order_grid_fragment, fragment);
        transaction.commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
        invalidateOptionsMenu();
    }

    private void shouldDisplayHomeUp(){
        boolean canBack = getSupportFragmentManager().getBackStackEntryCount()>0;
        mDrawerToggle.setDrawerIndicatorEnabled(!canBack);
    }

    public void orderItemSelected(int categoryPosition,int itemPosition){
        Bundle bundle = lookupItemBundle(categoryPosition,itemPosition);
        OrderItemSelectedDialog fragment = new OrderItemSelectedDialog();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),DIALOG);

    }

    public void orderItemSelected(int categoryPosition,int itemPosition,int linePosition,int orderAmount){
        Bundle bundle = lookupItemBundle(categoryPosition,itemPosition);
        bundle.putInt(Receipt.ORDER_AMOUNT,orderAmount);
        bundle.putInt(Receipt.LINE_POSITION,linePosition);

        ReceiptLineSelectedDialog fragment = new ReceiptLineSelectedDialog();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),DIALOG);
    }


    public void addLineReceipt(String categoryId, String itemId, int orderAmount){
        if (Receipt.currentOrderReceipt == null)
            Receipt.NewCurrentReceipt();

        Receipt.currentOrderReceipt.addLine(categoryId, itemId, orderAmount);

        if (findViewById(R.id.order_receipt_fragment) != null){

            OrderReceipt orderReceipt = (OrderReceipt) getSupportFragmentManager().findFragmentById(R.id.order_receipt_fragment);

            if (orderReceipt == null){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                orderReceipt = new OrderReceipt();
                transaction.replace(R.id.order_receipt_fragment,orderReceipt,RECEIPT_TAG);
                transaction.commit();
            } else{
                orderReceipt.dataSetChanged();
            }
        }else{
            setFabButton(true);
        }
    }

    public void updateLineReceipt(int linePosition,int orderAmount){
        if (orderAmount!=Receipt.currentOrderReceipt.getLine(linePosition).getOrderAmount()) {
            Receipt.currentOrderReceipt.getLine(linePosition).setOrderAmount(orderAmount);
            ((OrderReceipt) getSupportFragmentManager().findFragmentByTag(RECEIPT_TAG)).dataSetChanged();
        }
    }

    public void deleteLineReceipt(int linePosition){
        OrderReceipt orderReceipt = (OrderReceipt) getSupportFragmentManager().findFragmentByTag(RECEIPT_TAG);

        if (Receipt.currentOrderReceipt.removeLine(linePosition)){
            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(orderReceipt).commit();
            setFabButton(false);
        }else {
            orderReceipt.dataSetChanged();
        }
    }

    private void setFabButton(boolean show){
        if (show != showFAB){
            showFAB = show;

            FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.receipt);

            if (showFAB){
                floatingActionButton.setVisibility(View.VISIBLE);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        OrderReceipt orderReceipt = new OrderReceipt();
                        transaction.add(R.id.order_grid_fragment,orderReceipt,RECEIPT_TAG);
                        transaction.addToBackStack("Receipt");
                        transaction.commit();
                    }
                });
            } else {
                floatingActionButton.setVisibility(View.GONE);
            }
        }
    }

    private Bundle lookupItemBundle(int categoryPosition, int itemPosition){
        Bundle bundle = new Bundle();
        bundle.putInt(Category.CATEGORY_POSITION,categoryPosition);
        bundle.putInt(Category.ITEM_POSITION,itemPosition);
        return bundle;
    }

    public void updateCategory(int categoryPosition){
        Bundle bundle = new Bundle();
        bundle.putInt(Category.CATEGORY_POSITION,categoryPosition);
        DialogUpdateCategory fragment = new DialogUpdateCategory();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),DIALOG);
    }

    public void addCategory(int categoryPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(Category.CATEGORY_POSITION,categoryPosition);
        DialogAddCategory fragment = new DialogAddCategory();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),DIALOG);
    }

    public void updateItem(int categoryPosition,int itemPosition){
        DialogUpdateItem fragment = new DialogUpdateItem();
        fragment.setArguments(lookupItemBundle(categoryPosition,itemPosition));
        fragment.show(getSupportFragmentManager(),DIALOG);
    }

    public void addItem(int categoryPosition,int itemPosition){
        DialogAddItem fragment = new DialogAddItem();
        fragment.setArguments(lookupItemBundle(categoryPosition,itemPosition));
        fragment.show(getSupportFragmentManager(),DIALOG);
    }

    public void choosePaymentMethod(){
        DialogChoosePaymentMethod fragment = new DialogChoosePaymentMethod();
        fragment.show(getSupportFragmentManager(),DIALOG);
    }

    public void configureOrderMenuDataSetChanged(){
        ((ConfigureOrderMenu) getSupportFragmentManager().findFragmentById(R.id.order_grid_fragment)).dataSetChanged();
    }


}
