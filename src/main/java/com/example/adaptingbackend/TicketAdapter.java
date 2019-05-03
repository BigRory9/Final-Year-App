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

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the tickets in a list
    private List<Ticket> ticketList;

    //getting the context and ticket list with constructor
    public TicketAdapter(Context mCtx, List<Ticket> ticketList) {
        this.mCtx = mCtx;
        this.ticketList = ticketList;
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.newrow, null);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
        //getting the ticket of the specified position
        Ticket ticket = ticketList.get(position);

        //binding the data with the viewholder views
        holder.concertName.setText(ticket.getName());
        holder.date.setText(ticket.getDate());
        holder.price.setText(String.valueOf(ticket.getPrice()));
//        holder.textViewPrice.setText(String.valueOf(ticket.getPrice()));

//        holder.imageView.setImageDrawable(R.drawable.fypbrand);

    }


    @Override
    public int getItemCount() {
        return ticketList.size();
    }


    class TicketViewHolder extends RecyclerView.ViewHolder {

        TextView concertName, date, price;
        Button openPDF;
        ImageView imageView;

        public TicketViewHolder(View itemView) {
            super(itemView);
//
            concertName = itemView.findViewById(R.id.concertName);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}