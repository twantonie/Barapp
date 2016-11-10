package nl.erc69.barapp;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderItemSelected extends android.support.v4.app.Fragment{
    private Item item;
    private int orderAmount = 1;
    private int categoryPosition;
    private int itemPosition;
    private int linePosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        categoryPosition = bundle.getInt(Category.CATEGORY_POSITION);
        itemPosition = bundle.getInt(Category.ITEM_POSITION);

        item = Category.CATEGORIES.get(categoryPosition).ITEMS.get(itemPosition);

        if (bundle.containsKey(Receipt.ORDER_AMOUNT)) {
            linePosition = bundle.getInt(Receipt.LINE_POSITION);
            orderAmount = bundle.getInt(Receipt.ORDER_AMOUNT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_item_selected,container,false);

        TextView textview = (TextView) view.findViewById(R.id.order_item_selected_label);
        textview.setText(item.getName());

        textview = (TextView) view.findViewById(R.id.order_item_selected_price);
        textview.setText("€" + String.valueOf(item.getPrice()));

        EditText edittext = (EditText) view.findViewById(R.id.order_item_selected_amount);
        edittext.setText(String.valueOf(orderAmount));
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) getView().findViewById(R.id.order_item_selected_amount);
                if(hasFocus){
                    editText.setText("");
                } else{
                    String string = editText.getText().toString();
                    if (!string.matches("")){
                        orderAmount = Integer.parseInt(string);
                    } else {
                        editText.setText(String.valueOf(orderAmount));
                    }

                }
            }
        });


        android.support.percent.PercentFrameLayout mLayout = (android.support.percent.PercentFrameLayout) view.findViewById(R.id.order_item_selected_layout);
        mLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                cancelPopup();
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.order_item_selected_layout_child);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        Button button = (Button) view.findViewById(R.id.order_item_selected_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                if (linePosition==-1)
                    cancelPopup();
                else {
                    ((MainActivity) getActivity()).deleteLineReceipt(linePosition);
                    cancelPopup();
                }
            }
        });
        if (linePosition!=-1)
            button.setText("Delete");

        button = (Button) view.findViewById(R.id.order_item_selected_minus);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                if(orderAmount>0){
                    orderAmount--;
                    updateAmount();
                }
            }
        });

        button = (Button) view.findViewById(R.id.order_item_selected_plus);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                orderAmount++;
                updateAmount();
            }
        });

        button = (Button) view.findViewById(R.id.order_item_selected_add);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                if (orderAmount>0) {
                    if (linePosition == -1)
                        ((MainActivity) getActivity()).addLineReceipt(categoryPosition,itemPosition, orderAmount);
                    else
                        ((MainActivity) getActivity()).updateLineReceipt(linePosition, orderAmount);
                }
                cancelPopup();
            }
        });

        return view;
    }

    public void cancelPopup(){
        if (linePosition!=-1)
            ((MainActivity)getActivity()).listItemUnClicked();
        hideKeyboard();
        getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(this).commit();
    }

    public void updateAmount(){
        EditText edittext = (EditText) getView().findViewById(R.id.order_item_selected_amount);
        edittext.setText(String.valueOf(orderAmount));
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        clearFocus();
    }

    public void clearFocus() {
        EditText edittext = (EditText) getView().findViewById(R.id.order_item_selected_amount);
        edittext.clearFocus();
    }

}
