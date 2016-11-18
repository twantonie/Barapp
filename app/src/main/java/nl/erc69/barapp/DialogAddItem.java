package nl.erc69.barapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogAddItem extends DialogFragment {
    private int mCategoryPosition;
    private int mItemPosition;

    private EditText ETName;
    private EditText ETPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        mCategoryPosition = bundle.getInt(Category.CATEGORY_POSITION);
        mItemPosition = bundle.getInt(Category.ITEM_POSITION);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_item,null);

        ETName = (EditText) view.findViewById(R.id.update_item_name);
        ETPrice = (EditText) view.findViewById(R.id.update_item_price);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkEditText();
                        dismiss();
                    }
                })
                .setTitle("Add new item");

        return builder.create();
    }

    private void checkEditText(){
        String name = ETName.getText().toString();
        String price = ETPrice.getText().toString();
        if (!name.matches("") & !price.matches(""))
            addItem(name,Double.parseDouble(price));
        else
            Toast.makeText(getActivity(),"Enter Name and/or Price",Toast.LENGTH_SHORT).show();

    }

    private void addItem(String name,double price){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(Category.CATEGORIES).child(Category.CATEGORIES_POS.get(mCategoryPosition).getId()).child(Category.ITEMS);
        String id = mDatabase.push().getKey();

        Item item = new Item(name,id,mItemPosition,price);

        Category.CATEGORIES_POS.get(mCategoryPosition).addItem(item);
        mDatabase.child(id).setValue(item);

        ((MainActivity)getActivity()).configureOrderMenuDataSetChanged();
    }

}
