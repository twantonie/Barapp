package nl.erc69.barapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.payleven.payment.api.PaylevenApi;
import com.payleven.payment.api.TransactionRequest;
import com.payleven.payment.api.TransactionRequestBuilder;

import java.util.Currency;
import java.util.UUID;

public class PinFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pin_fragment,container,false);

        Button button = (Button) view.findViewById(R.id.start_pin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionRequestBuilder builder = new TransactionRequestBuilder((int) Receipt.currentOrderReceipt.getTotal()*100, Currency.getInstance("EUR"));
                builder.setDescription("Payment done via Barapp");

                TransactionRequest request = builder.createTransactionRequest();
                String orderId = UUID.randomUUID().toString();

                PaylevenApi.initiatePayment(getActivity(),orderId,request);

            }
        });

        return view;
    }





}
