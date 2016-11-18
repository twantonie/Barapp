package nl.erc69.barapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LaunchActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Button button = (Button) findViewById(R.id.launch_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                createOrderItems();
            }
        });
    }

    private void createOrderItems(){
        FirebaseDatabase.getInstance().getReference().child(Category.CATEGORIES_FB).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Category.clear();
                String id;
                for (DataSnapshot categoriesSnapshot: dataSnapshot.getChildren()){
                    Category category = categoriesSnapshot.getValue(Category.class);
                    id = category.getId();
                    Category.newCategory(category);
                    for (DataSnapshot itemsSnapshot: categoriesSnapshot.child(Category.ITEMS_FB).getChildren()){
                        Category.CATEGORIES_ID.get(id).addItem(itemsSnapshot.getValue(Item.class));
                    }
                }
                hideProgressDialog();
                startActivity(new Intent(LaunchActivity.this,MainActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private double checkPrice(Object price){
        if (price instanceof Long)
            return ((Long) price).doubleValue();
        else
            return (double) price;

    }
}
