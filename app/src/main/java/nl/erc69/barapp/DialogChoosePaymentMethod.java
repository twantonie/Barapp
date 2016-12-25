package nl.erc69.barapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

public class DialogChoosePaymentMethod extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.payment_options_title)
                .setItems(R.array.payment_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 1:
                                startPinPayment();
                                break;
                            default:
                                Toast toast = Toast.makeText(getActivity(),"Not yet implemented",Toast.LENGTH_SHORT);
                                toast.show();
                                break;
                        }

                    }
                });

        return builder.create();
    }

    private void startPinPayment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        PinFragment pinFragment = new PinFragment();
        transaction.add(R.id.order_grid_fragment,pinFragment,MainActivity.PAYMENT_FRAGMENT);
        transaction.addToBackStack("Payment");
        transaction.commit();
    }

}
