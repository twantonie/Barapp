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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DialogAddItem extends DialogFragment {
    private int mCategoryPosition;
    private int mItemPosition;

    private EditText ETName;
    private EditText ETPrice;

    private Spinner spinner;

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

        spinner = (Spinner) view.findViewById(R.id.dialog_item_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,populateSpinner());
        spinner.setAdapter(adapter);
        spinner.setSelection(mItemPosition);

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
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(Category.CATEGORIES_FB).child(Category.CATEGORIES_POS.get(mCategoryPosition).getId()).child(Category.ITEMS_FB);
        String id = mDatabase.push().getKey();

        Item item = new Item(name,id,mItemPosition,price);

        Category.CATEGORIES_POS.get(mCategoryPosition).addItem(item);
        mDatabase.child(id).setValue(item);

        int newPosition = spinner.getSelectedItemPosition();
        if (newPosition != mItemPosition){
            Category.CATEGORIES_POS.get(mCategoryPosition).setItemPosition(newPosition,mItemPosition);
        }

        ((MainActivity)getActivity()).configureOrderMenuDataSetChanged();
    }

    private ArrayList<String> populateSpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i=0;i<mItemPosition+1;i++){
            arrayList.add(String.valueOf(i+1));
        }
        return arrayList;
    }

}
