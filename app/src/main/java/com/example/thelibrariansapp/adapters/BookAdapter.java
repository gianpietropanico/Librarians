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
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.activity.BookActivity;
import com.example.thelibrariansapp.models.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Book> books;

    // Corretto il costruttore
    public BookAdapter(ArrayList<Book> listBook, Context context) {
        this.context = context;
        this.books = listBook; // Assegna correttamente la lista di libri
    }

    public Book getItem(int position) {
        return books.get(position);
    }

    public ArrayList<Book> getListBook() {
        return books;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.book_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setBook(books.get(position));

        holder.bookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookActivity.class);
                intent.putExtra("Book", books.get(position));
                context.startActivity(intent);
            }
        });
    }

    // Aggiorna la lista di libri
    public void updateBooks(ArrayList<Book> newBooks) {
        if (newBooks != null) {
            this.books.clear(); // Pulisce la lista esistente
            this.books.addAll(newBooks); // Aggiunge i nuovi libri
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView bookTitle, bookIsbn, bookAuthor, bookGenre, nonDisponibile;
        public ImageView bookImg;


        public MyViewHolder(View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookIsbn = itemView.findViewById(R.id.bookIsbn);
            bookGenre = itemView.findViewById(R.id.bookGenre);
            bookImg = itemView.findViewById(R.id.bookImg);
            nonDisponibile = itemView.findViewById(R.id.nonDisponibileTextView);

        }

        void setBook(Book book) {
            bookTitle.setText(book.getTitle());
            bookGenre.setText(book.getGenre());
            bookAuthor.setText(book.getAuthor());
            bookIsbn.setText(book.getIsbn());
            if(book.getAvailable() < 1){
                nonDisponibile.setVisibility(View.VISIBLE);
            }
            if (book.getImageUrl() == null) {
                bookImg.setImageResource(R.drawable.ic_launcher_background);
            } else {
                Glide.with(context)
                        .load(book.getImageUrl())
                        .centerInside()
                        .into(bookImg);            }
        }
    }
}
