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


public class DialogUpdateCategory extends DialogFragment {
    private int mCategoryPosition;

    private Category mCategory;

    private EditText ETName;

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


    private void deleteCategory(){
        Category.removeCategory(mCategoryPosition);

        dataSetChanged();
    }

    private void dataSetChanged(){
        ((MainActivity)getActivity()).configureOrderMenuDataSetChanged();
    }
}
