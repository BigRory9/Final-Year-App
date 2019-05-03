package com.example.adaptingbackend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.TicketViewHolder> {


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
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.newroworders, null);
        return new TicketViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
        //getting the ticket of the specified position
        ReadOrdersFromDB order = orderList.get(position);
        String prodName = "";
        int price = 0;

        //binding the data with the viewholder views
//        for(int i=0;i<orderList.size();i++) {
//            for(int i =0;i<productList.size();i++){
//
//                if(productList.get(i).getId().equals(order.getProduct_id().get(0))){
//
//                    for(int j=0; j<orderList.get(i).getProduct_id().size();j++) {
//                        System.out.println(orderList.get(i).getProductQuantity());
//                        prodName = prodName +productList.get(i).get_name() + " x " + order.getProductQuantity().get(j);
//                        price = (int) (price + productList.get(i).get_value());
//                    }
//                        break;
//
//                }
//            }
        for (int j = 0; j < orderList.get(position).getProduct_id().size(); j++) {
            for (int i = 0; i < productList.size(); i++) {
                if(productList.get(i).getId().equals(order.getProduct_id().get(j))) {
                    prodName = prodName + productList.get(i).get_name() + " x " + order.getProductQuantity().get(j) + "\n";
                    price = (int) (price + productList.get(i).get_value());
                }
            }


        }
        holder.productName.setText(prodName);
//        holder.date.setText(order.getOrder_id());
        holder.price.setText("€"+Integer.toString(price));
        // }
//        holder.textViewPrice.setText(String.valueOf(ticket.getPrice()));

//        holder.imageView.setImageDrawable(R.drawable.fypbrand);

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }


    class TicketViewHolder extends RecyclerView.ViewHolder {

        TextView productName, date, price;
        Button openPDF;
        ImageView imageView;

        public TicketViewHolder(View itemView) {
            super(itemView);
//
            productName = itemView.findViewById(R.id.productName);
            date = itemView.findViewById(R.id.product);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}