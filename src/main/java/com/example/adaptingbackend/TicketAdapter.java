package com.example.adaptingbackend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    private final String accessKey="AKIAJVL5I336SYABBB4A";
    private final String secretKey="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";

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
    public void onBindViewHolder(TicketViewHolder holder,  int position) {
        //getting the ticket of the specified position
        final Ticket ticket = ticketList.get(position);

        //binding the data with the viewholder views
        holder.concertName.setText(ticket.getName());
        holder.date.setText(ticket.getDate());
        holder.price.setText(String.valueOf(ticket.getPrice()));
        holder.openPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ResponseHeaderOverrides override = new ResponseHeaderOverrides();
                    override.setContentType("application/pdf");
                    String ticketName=ticket.getId()+"PDF.pdf";

                    GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest("gigzeaze", ticketName);// Added an hour's worth of milliseconds to the current time.urlRequest.setExpiration(    new Date( System.currentTimeMillis() + 3600000 ) );urlRequest.setResponseHeaders( override );
                    AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
                    URL url = s3Client.generatePresignedUrl(urlRequest);

                    mCtx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.toURI().toString())));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
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
            openPDF = itemView.findViewById(R.id.openPDF);



        }
    }
}