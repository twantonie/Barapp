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

public class DialogUpdateItem extends DialogFragment {
    private int mCategoryPosition;
    private int mItemPosition;

    private Item item;

    private EditText ETName;
    private EditText ETPrice;

    DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        mCategoryPosition = bundle.getInt(Category.CATEGORY_POSITION);
        mItemPosition = bundle.getInt(Category.ITEM_POSITION);

        item = Category.CATEGORIES_POS.get(mCategoryPosition).ITEMS_POS.get(mItemPosition);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Category.CATEGORIES_FB).child(Category.CATEGORIES_POS.get(mCategoryPosition).getId()).child(Category.ITEMS_FB).child(item.getId());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_item,null);

        ETName = (EditText) view.findViewById(R.id.update_item_name);
        ETName.setText(item.getName());
        ETPrice = (EditText) view.findViewById(R.id.update_item_price);
        ETPrice.setText(String.valueOf(item.getPrice()));

        String title = "Update " + item.getName();

        builder.setView(view)
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem();
                        dismiss();
                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkEditText();
                        dismiss();
                    }
                })
        .setTitle(title);

        return builder.create();
    }

    private void checkEditText(){
        String name = ETName.getText().toString();
        if (!name.matches(""))
            updateName(name);
        else
            Toast.makeText(getActivity(),"Enter Name",Toast.LENGTH_SHORT).show();

        String sPrice = ETPrice.getText().toString();
        if(!sPrice.matches(""))
            updatePrice(Double.parseDouble(sPrice));
        else
            Toast.makeText(getActivity(),"Enter Price",Toast.LENGTH_SHORT).show();
    }

    private void updateName(String name){
        if (!name.equals(item.getName())){
            item.setName(name);
            mDatabase.child("Name").setValue(name);
        }
    }

    private void updatePrice(double price){
        if (price != item.getPrice()){
            item.setPrice(price);
            mDatabase.child("Price").setValue(price);
        }
    }

    private void deleteItem(){
        Category.CATEGORIES_POS.get(mCategoryPosition).removeItem(mItemPosition);

        ((MainActivity)getActivity()).configureOrderMenuDataSetChanged();
    }
}
