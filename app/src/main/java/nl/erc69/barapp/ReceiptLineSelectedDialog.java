package nl.erc69.barapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class ReceiptLineSelectedDialog extends DialogFragment {
    private Item item;
    private int orderAmount = 1;
    private int linePosition = -1;
    private NumberPicker mNumberPicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        int categoryPosition = bundle.getInt(Category.CATEGORY_POSITION);
        int itemPosition = bundle.getInt(Category.ITEM_POSITION);
        linePosition = bundle.getInt(Receipt.LINE_POSITION);
        orderAmount = bundle.getInt(Receipt.ORDER_AMOUNT);

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
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderAmount = mNumberPicker.getValue();
                        ((MainActivity) getActivity()).updateLineReceipt(linePosition,orderAmount);
                        dismiss();
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getActivity()).deleteLineReceipt(linePosition);
                        dismiss();
                    }
                });

        return builder.create();
    }
}
