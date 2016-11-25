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


public class DialogUpdateCategory extends DialogFragment {
    private int mCategoryPosition;

    private Category mCategory;

    private EditText ETName;
    private Spinner spinner;

    DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        mCategoryPosition = bundle.getInt(Category.CATEGORY_POSITION);
        mCategory = Category.CATEGORIES_POS.get(mCategoryPosition);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Category.CATEGORIES_FB).child(Category.CATEGORIES_POS.get(mCategoryPosition).getId());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_category,null);

        ETName = (EditText) view.findViewById(R.id.update_category_name);
        ETName.setText(mCategory.getName());

        spinner = (Spinner) view.findViewById(R.id.dialog_category_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,populateSpinner());
        spinner.setAdapter(adapter);
        spinner.setSelection(mCategoryPosition);

        String title = "Update " + mCategory.getName();

        builder.setView(view)
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCategory();
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
    }

    private void updateName(String name){
        if (!name.equals(mCategory.getName())){
            mCategory.setName(name);
            mDatabase.child("name").setValue(name);
        }

        dataSetChanged();
    }

    private void checkSpinner(){
        int newPosition = spinner.getSelectedItemPosition();
        if (mCategoryPosition != newPosition){
            Category.setCategoryPosition(newPosition,mCategoryPosition);

            dataSetChanged();
        }
    }


    private void deleteCategory(){
        Category.removeCategory(mCategoryPosition);

        dataSetChanged();
    }

    private void dataSetChanged(){
        ((MainActivity)getActivity()).configureOrderMenuDataSetChanged();
    }

    private ArrayList<String> populateSpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i=0;i<Category.CATEGORIES_POS.size();i++){
            arrayList.add(String.valueOf(i+1));
        }
        return arrayList;
    }
}
