package nl.erc69.barapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class ConfigureOrderGridFragment extends Fragment {
    public static final String ARG_PAGE = "CATEGORY_POSITION";

    private int mCategory;

    public static ConfigureOrderGridFragment newInstance(int categoryPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, categoryPosition);
        ConfigureOrderGridFragment fragment = new ConfigureOrderGridFragment();
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
                if (position < Category.CATEGORIES_POS.get(mCategory).ITEMS_POS.size()) {
                    System.out.print("Update button pressed");
                    ((MainActivity) getActivity()).updateItem(mCategory, position);
                }else {
                    System.out.print("Add button pressed");
                    ((MainActivity) getActivity()).addItem(mCategory, position);
                }
            }
        });

        gridView.setAdapter(new ConfigureOrderGridFragment.OrderItemAdapter(view.getContext())); // uses the view to get the context instead of getActivity()
        return view;
    }

    private class OrderItemAdapter extends BaseAdapter {
        private Context mContext;

        private OrderItemAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Category.CATEGORIES_POS.get(mCategory).ITEMS_POS.size() + 1;
        }

        public Item getItem(int position) {
            return Category.CATEGORIES_POS.get(mCategory).ITEMS_POS.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (position < Category.CATEGORIES_POS.get(mCategory).ITEMS_POS.size()) {
                if (view == null) {
                    view = getActivity().getLayoutInflater().inflate(R.layout.order_grid_item, viewGroup, false);
                }

                final Item item = getItem(position);

                TextView name = (TextView) view.findViewById(R.id.order_grid_item_label);
                name.setText(item.getName());

                name = (TextView) view.findViewById(R.id.order_grid_item_price);
                name.setText("€" + String.valueOf(item.getPrice()));

            } else {
                if (view == null) {
                    view = getActivity().getLayoutInflater().inflate(R.layout.configure_order_menu_item, viewGroup, false);
                }
            }
            return view;
        }

    }

}
