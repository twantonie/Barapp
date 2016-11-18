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

public class DialogAddCategory extends DialogFragment {
    private int mCategoryPosition;

    private EditText name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        mCategoryPosition = bundle.getInt(Category.CATEGORY_POSITION);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_category,null);

        name = (EditText) view.findViewById(R.id.update_category_name);

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
                    }
                })
                .setTitle("Add new category");

        return builder.create();
    }

    private void checkEditText(){
        String string =  name.getText().toString();
        if (!string.matches(""))
            editDatabase(string);
        else
            Toast.makeText(getActivity(),"Enter Name",Toast.LENGTH_SHORT).show();
    }

    private void editDatabase(String string){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        String id = mDatabase.child(Category.CATEGORIES_FB).push().getKey();

        Category category = new Category(string,id,mCategoryPosition);

        Category.newCategory(category);
        mDatabase.child(Category.CATEGORIES_FB).child(id).setValue(category);

        ((MainActivity)getActivity()).configureOrderMenuDataSetChanged();
    }
}

