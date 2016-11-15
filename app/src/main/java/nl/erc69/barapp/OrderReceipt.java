package nl.erc69.barapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class OrderReceipt extends Fragment {

    public Receipt currentReceipt = new Receipt();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int categoryPosition = getArguments().getInt(Category.CATEGORY_POSITION);
        int itemPosition = getArguments().getInt(Category.ITEM_POSITION);
        int orderAmount = getArguments().getInt(Receipt.ORDER_AMOUNT);

        currentReceipt.addLine(categoryPosition,itemPosition,orderAmount);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_receipt,container,false);

        ListView listView = (ListView) view.findViewById(R.id.order_receipt_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Receipt.Line line = currentReceipt.LINES.get(position);
                ((MainActivity) getActivity()).orderItemSelected(line.getCategoryPosition(), line.getItemPosition(), position, line.getOrderAmount());
            }
        });
        listView.setAdapter(new ListItemAdapter());

        TextView textView = (TextView) view.findViewById(R.id.order_receipt_total);
        textView.setText( String.valueOf(currentReceipt.getTotal()));

        textView = (TextView) view.findViewById(R.id.order_receipt_date);
        textView.setText(currentReceipt.getParsedDate());

        textView = (TextView) view.findViewById(R.id.order_receipt_time);
        textView.setText(currentReceipt.getParsedTime()+" ");

        return view;
    }

    public void addLineReceipt(int categoryPosition,int itemPosition,int amount){
        currentReceipt.addLine(categoryPosition,itemPosition,amount);
        dataSetChanged();
    }

    public void updateLineReceipt(int linePosition,int orderAmount) {
        if (orderAmount!=currentReceipt.LINES.get(linePosition).getOrderAmount()) {
            currentReceipt.LINES.get(linePosition).setOrderAmount(orderAmount);
            dataSetChanged();
        }
    }

    public void deleteLineReceipt(int linePosition){
        if (currentReceipt.LINES.size() > 1){
            currentReceipt.LINES.remove(linePosition);
            dataSetChanged();
        } else{
            getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(this).commit();
        }

    }

    private void dataSetChanged(){
        TextView textView = (TextView) getView().findViewById(R.id.order_receipt_total);
        textView.setText( String.valueOf(currentReceipt.getTotal()));

        ListView listView = (ListView) getView().findViewById(R.id.order_receipt_list);
        ListItemAdapter listItemAdapter = (ListItemAdapter) listView.getAdapter();

        listItemAdapter.notifyDataSetChanged();
    }

    public class ListItemAdapter extends BaseAdapter {

        public int getCount() {return currentReceipt.LINES.size();}

        public Receipt.Line getItem(int position) {
            return currentReceipt.LINES.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null){
                view = getActivity().getLayoutInflater().inflate(R.layout.order_receipt_item, viewGroup, false);
            }
            Receipt.Line line = currentReceipt.LINES.get(position);

            TextView textView = (TextView) view.findViewById(R.id.order_receipt_item_amount);
            textView.setText(String.valueOf(line.getOrderAmount()));

            textView = (TextView) view.findViewById(R.id.order_receipt_item_name);
            textView.setText(line.getItem().getName());

            textView = (TextView) view.findViewById(R.id.order_receipt_item_price);
            textView.setText(String.valueOf(line.getItem().getPrice()));

            textView = (TextView) view.findViewById(R.id.order_receipt_item_total);
            textView.setText(String.valueOf(line.getItem().getPrice()*line.getOrderAmount()));

            return view;
        }

    }

}
