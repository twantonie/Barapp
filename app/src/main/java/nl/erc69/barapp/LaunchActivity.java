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
        FirebaseDatabase.getInstance().getReference().child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Category.CATEGORIES.clear();
                int i = 0;
                for (DataSnapshot categoriesSnapshot: dataSnapshot.getChildren()){
                    Category.CATEGORIES.add(new Category((String) categoriesSnapshot.child("Name").getValue()));
                    for (DataSnapshot itemsSnapshot: categoriesSnapshot.child("Items").getChildren()){
                        Category.CATEGORIES.get(i).addItem((String) itemsSnapshot.child("Name").getValue(),checkPrice(itemsSnapshot.child("Price").getValue()));
                    }
                    i++;
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
