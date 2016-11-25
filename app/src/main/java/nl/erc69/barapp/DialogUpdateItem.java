package nl.erc69.barapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DialogUpdateItem extends DialogFragment {
    private int mCategoryPosition;
    private int mItemPosition;

    private Item item;

    private EditText ETName;
    private EditText ETPrice;
    private Spinner spinner;

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

        spinner = (Spinner) view.findViewById(R.id.dialog_item_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,populateSpinner());
        spinner.setAdapter(adapter);
        spinner.setSelection(mItemPosition);

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
                        checkSpinner();
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
            mDatabase.child("name").setValue(name);
        }
    }

    private void updatePrice(double price){
        if (price != item.getPrice()){
            item.setPrice(price);
            mDatabase.child("price").setValue(price);
        }
    }

    private void checkSpinner(){
        int newPosition = spinner.getSelectedItemPosition();
        if (mItemPosition != newPosition){
            Category.CATEGORIES_POS.get(mCategoryPosition).setItemPosition(newPosition,mItemPosition);
        }
    }

    private void deleteItem(){
        Category.CATEGORIES_POS.get(mCategoryPosition).removeItem(mItemPosition);

        ((MainActivity)getActivity()).configureOrderMenuDataSetChanged();
    }

    private ArrayList<String> populateSpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i=0;i<Category.CATEGORIES_POS.get(mCategoryPosition).ITEMS_POS.size();i++){
            arrayList.add(String.valueOf(i+1));
        }
        return arrayList;
    }

}
