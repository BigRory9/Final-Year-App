package com.example.adaptingbackend;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the tickets in a list
    private List<ReadOrdersFromDB> orderList;

    private List<Product> productList;

    //getting the context and ticket list with constructor
    public OrderAdapter(Context mCtx, List<ReadOrdersFromDB> orderList, List<Product> productList) {
        this.mCtx = mCtx;
        this.orderList = orderList;
        this.productList = productList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.newroworders, null);
        return new OrderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(OrderViewHolder holder, final int position) {
        //getting the ticket of the specified position
        final ReadOrdersFromDB order = orderList.get(position);
        String prodName = "";
        int price = 0;

        for (int j = 0; j < orderList.get(position).getProduct_id().size(); j++) {
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getId().equals(order.getProduct_id().get(j))) {
                    prodName = prodName + productList.get(i).get_name() + " x " + order.getProductQuantity().get(j) + "\n";
                    price = (int) (price + productList.get(i).get_value());
                }
            }


        }
        holder.productName.setText(prodName);
//        holder.date.setText(order.getOrder_id());
        holder.price.setText("€" + Integer.toString(price));

        holder.collectItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Opening Box");


                // set the custom dialog components - text, image and button
//        TextView text = (TextView) dialog.findViewById(R.id.text);
//        text.setText("Enter your pin sent via your email to collect");
//        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        image.setImageResource(R.drawable.burger);


//
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialog.show();
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText Edit1 = (EditText) dialog.findViewById(R.id.editTextone);
                        final EditText Edit2 = (EditText) dialog.findViewById(R.id.editTexttwo);
                        final EditText Edit3 = (EditText) dialog.findViewById(R.id.editTextthree);
                        final EditText Edit4 = (EditText) dialog.findViewById(R.id.editTextfour);
                        String pinEntered = Edit1.getText().toString() + Edit2.getText().toString() + Edit3.getText().toString() + Edit4.getText().toString();
                        if (pinEntered.equals(order.getCode())) {
                            Toast.makeText(v.getContext(), "CONGRADULATIONS User Entered " + pinEntered,
                                    Toast.LENGTH_SHORT).show();

                            dialog.getContext().startActivity(new Intent(dialog.getContext(), DoorServo.class));
                            dialog.dismiss();
                        } else {
                            Toast.makeText(v.getContext(), "ERROR User Entered code" + pinEntered,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                });

            }
        });


    }
//    }
//        holder.collectItem.setOnClickListener(
//
//        );
    // }
//        holder.textViewPrice.setText(String.valueOf(ticket.getPrice()));

//        holder.imageView.setImageDrawable(R.drawable.fypbrand);


    @Override
    public int getItemCount() {
        return orderList.size();
    }


    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView productName, date, price;
        Button collectItem;
        LinearLayout linearLayout;
        ImageView imageView;

        public OrderViewHolder(View itemView) {
            super(itemView);
//
            productName = itemView.findViewById(R.id.productName);
            date = itemView.findViewById(R.id.product);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.imageView);
            collectItem = itemView.findViewById(R.id.openPDF);
//            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear);
        }
    }
}