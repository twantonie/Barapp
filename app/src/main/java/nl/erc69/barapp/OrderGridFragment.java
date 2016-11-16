package nl.erc69.barapp;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class OrderGridFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mCategory;

    public static OrderGridFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        OrderGridFragment fragment = new OrderGridFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getInt(ARG_PAGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_grid, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.order_grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                ((MainActivity)getActivity()).orderItemSelected(mCategory,position);    }
        });
        gridView.setAdapter(new OrderItemAdapter(view.getContext())); // uses the view to get the context instead of getActivity()
        return view;
    }

    private class OrderItemAdapter extends BaseAdapter {
        private Context mContext;

        private OrderItemAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Category.CATEGORIES.get(mCategory).ITEMS.size();
        }

        public Item getItem(int position) {
            return Category.CATEGORIES.get(mCategory).ITEMS.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null){
                view = getActivity().getLayoutInflater().inflate(R.layout.order_grid_item, viewGroup, false);
            }

            final Item item = getItem(position);

            TextView name = (TextView) view.findViewById(R.id.order_grid_item_label);
            name.setText(item.getName());

            name = (TextView) view.findViewById(R.id.order_grid_item_price);
            name.setText("€"+String.valueOf(item.getPrice()));
            return view;
        }

    }

}