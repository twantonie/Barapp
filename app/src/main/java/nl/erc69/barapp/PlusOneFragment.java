package nl.erc69.barapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.plus.PlusOneButton;

public class PlusOneFragment extends Fragment {

    private static final String ARG_CATEGORY_NUMBER = "categoryNumber";

    private int categoryNumber;

    private OnFragmentInteractionListener mListener;

    public static PlusOneFragment newInstance(int categoryNumber) {
        PlusOneFragment fragment = new PlusOneFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_NUMBER, categoryNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryNumber = getArguments().getInt(ARG_CATEGORY_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plus_one, container, false);

        //Find the +1 button
        Button button = (Button) view.findViewById(R.id.plus_one_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed();
            }
        });

        return view;
    }

    public void buttonPressed() {
        if (mListener != null) {
            mListener.addCategory(categoryNumber);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void addCategory(int categoryNumber);
    }

}
