package nl.erc69.barapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;


public class OrderReceipt extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_receipt,container,false);

        ListView listView = (ListView) view.findViewById(R.id.order_receipt_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Receipt.Line line = Receipt.currentOrderReceipt.getLine(position);
                Category category = Category.CATEGORIES_ID.get(line.getCategoryId());
                ((MainActivity) getActivity()).orderItemSelected(
                        category.getPosition(),
                        category.ITEMS_ID.get(line.getItemId()).getPosition(),
                        position, line.getOrderAmount());
            }
        });
        listView.setAdapter(new ListItemAdapter());

        TextView textView = (TextView) view.findViewById(R.id.order_receipt_total);
        textView.setText( String.valueOf(Receipt.currentOrderReceipt.getTotal()));

        textView = (TextView) view.findViewById(R.id.order_receipt_date);
        textView.setText(Receipt.currentOrderReceipt.getDate());

        textView = (TextView) view.findViewById(R.id.order_receipt_time);
        textView.setText(Receipt.currentOrderReceipt.getTime()+" ");

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.pay);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).choosePaymentMethod();
            }
        });

        return view;
    }

    public void dataSetChanged(){
        TextView textView = (TextView) getView().findViewById(R.id.order_receipt_total);
        textView.setText( String.valueOf(Receipt.currentOrderReceipt.getTotal()));

        ListView listView = (ListView) getView().findViewById(R.id.order_receipt_list);
        ListItemAdapter listItemAdapter = (ListItemAdapter) listView.getAdapter();

        listItemAdapter.notifyDataSetChanged();
    }

    public class ListItemAdapter extends BaseAdapter {

        public int getCount() {return Receipt.currentOrderReceipt.getLinesSize();}

        public Receipt.Line getItem(int position) {
            return Receipt.currentOrderReceipt.getLine(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null){
                view = getActivity().getLayoutInflater().inflate(R.layout.order_receipt_item, viewGroup, false);
            }
            Receipt.Line line = Receipt.currentOrderReceipt.getLine(position);

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
