package com.example.keiji.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Keiji on 3/24/2018.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contactlayout, parent, false);
        }

//        TextView LastName = (TextView) convertView.findViewById(R.id.Name2);
//        LastName.setText(order.getName());
//
//        TextView oID = (TextView) convertView.findViewById(R.id.orderId);
//        oID.setText(String.valueOf(order.getID()));
//
//        TextView amount = (TextView) convertView.findViewById(R.id.amount);
//        amount.setText(String.valueOf(order.getAmount()));
//
//        TextView product= (TextView) convertView.findViewById(R.id.ProductText);
//        product.setText(order.getProduct());

        return super.getView(position, convertView, parent);
    }
}
