package nl.erc69.barapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class OrderItemSelectedDialog extends DialogFragment {
    private Item item;
    private int orderAmount = 1;
    private int categoryPosition;
    private int itemPosition;
    private NumberPicker mNumberPicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        categoryPosition = bundle.getInt(Category.CATEGORY_POSITION);
        itemPosition = bundle.getInt(Category.ITEM_POSITION);

        item = Category.CATEGORIES_POS.get(categoryPosition).ITEMS_POS.get(itemPosition);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_spinner,null);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMaxValue(25);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setValue(orderAmount);

        builder.setView(view)
                .setTitle(item.getName())
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderAmount = mNumberPicker.getValue();
                        ((MainActivity) getActivity()).addLineReceipt(categoryPosition,itemPosition, orderAmount);
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }
}
