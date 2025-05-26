package com.example.thelibrariansapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.thelibrariansapp.activity.BookLoans;
import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.models.Book;

import java.util.ArrayList;



public class RecommendedBookAdapter extends RecyclerView.Adapter<RecommendedBookAdapter.Viewholder> {

    ArrayList<Book> items;
    Context context;

    public RecommendedBookAdapter(ArrayList<Book> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_book,parent,false);
        context = parent.getContext();
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.TextCategory.setText(items.get(position).getGenre());
        holder.TextTitle.setText(items.get(position).getTitle());
        holder.TextAuthor.setText(items.get(position).getAuthor());
        holder.TextISBN.setText(items.get(position).getIsbn());

        Glide.with(context).load(items.get(position).getImageUrl()).centerInside().into(holder.ImageCopertina);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), BookLoans.class);
                intent.putExtra("object",items.get(position));
                holder.itemView.getContext().startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView    TextCategory,TextTitle,TextAuthor,TextISBN;
        ImageView ImageCopertina;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            TextCategory = itemView.findViewById(R.id.textViewCategory);
            TextTitle = itemView.findViewById(R.id.textViewtitle);
            TextAuthor = itemView.findViewById(R.id.textViewAuthorCardBook);
            TextISBN = itemView.findViewById(R.id.textViewISBNCardBook);
            ImageCopertina = itemView.findViewById(R.id.imageViewCopertinaLibroCardBook);
        }
    }
}
