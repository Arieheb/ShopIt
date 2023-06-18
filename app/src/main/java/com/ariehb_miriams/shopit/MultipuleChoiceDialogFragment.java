package com.ariehb_miriams.shopit;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MultipuleChoiceDialogFragment extends DialogFragment {

    private String listName;
    private ArrayList<String> itemList;
    public MultipuleChoiceDialogFragment (String listName, ArrayList<String> itemList) {
        this.listName = listName;
        this.itemList = itemList;
    }
    public interface onMultiChoiceListener{
        void onPositiveButtonClicked(String[] list, ArrayList<String> selectedItemList);
        void onNegativeButtonClicked();
    }

    onMultiChoiceListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (onMultiChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString()+ "onMultiChoiceListener must implemented") ;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ArrayList<String> selectedItemList= new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] listArray = itemList.toArray(new String[0]);
//        String[] list = getActivity().getResources().getStringArray(R.array.shopping_list);

        builder.setTitle(listName)
                .setMultiChoiceItems(listArray, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            selectedItemList.add(listArray[i]);
                        } else{
                            selectedItemList.remove(listArray[i]);
                        }

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onPositiveButtonClicked(listArray,selectedItemList);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onNegativeButtonClicked();

                    }
                });
        return builder.create();
    }
}