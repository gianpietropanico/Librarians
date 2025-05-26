package com.example.thelibrariansapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thelibrariansapp.R;

import com.example.thelibrariansapp.models.Loans;
import com.example.thelibrariansapp.utils.SocketClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LoansAdapter extends RecyclerView.Adapter<LoansAdapter.LoanViewHolder> {

    private List<Loans> loansList;
    private Context context;
    private SocketClient socketClient = new SocketClient();


    public LoansAdapter(List<Loans> loansList, Context context) {
        this.loansList = loansList;
        this.context = context;
    }

    @Override
    public LoanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lendlease, parent, false);
        return new LoanViewHolder(view);

    }

    @Override
    public void onBindViewHolder(LoanViewHolder holder, int position) {
        Loans loan = loansList.get(position);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String startDate = sdf.format(loansList.get(position).getStartDate());
        String dueDate = sdf.format(loansList.get(position).getDueDate());

        // Popola i dati del prestito nel ViewHolder
        holder.bookTitle.setText(loan.getBook().getTitle());
        holder.startDate.setText(startDate);
        holder.dueDate.setText(dueDate);
        holder.status.setText(loan.getStatus());
        holder.isbn.setText(loan.getBook().getIsbn());
        holder.genre.setText(loan.getBook().getGenre());




        try {
            // Ottieni la data di scadenza (dueDate2) direttamente dal modello `loan`
            Date dueDate2 = loan.getDueDate();
            Date currentDate = new Date();  // Ottieni la data corrente

            //holder.status.setTextColor(ContextCompat.getColor(context, R.color.red));

            // Se lo stato è "attivo" e la data di scadenza è passata, imposta il colore in rosso
            if (loan.getStatus().equals("attivo") && dueDate2.before(currentDate)) {
                holder.status.setText("IN RITARDO");
                holder.status.setTextColor(Color.RED);
            } else if (loan.getStatus().equals("attivo")) { // navy se IN USO e non scaduto
                holder.status.setText("       IN USO");
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.navy));
            } else {
                holder.status.setText("CONSEGNATO");
                holder.status.setTextColor(Color.BLACK);  // Colore per libro consegnato
                holder.buttonReturnBook.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.status.setTextColor(Color.BLACK);  // Colore di default in caso di errore
        }



    // Caricamento dell'immagine del libro
        Glide.with(context)
                .load(loan.getBook().getImageUrl())
                .centerInside()
                .into(holder.bookImage);

        // Gestisci il clic sul pulsante di restituzione
        holder.buttonReturnBook.setOnClickListener(v -> {
            returnBook(loan, position);
        });
    }

    public void sortLoansList() {
        Collections.sort(loansList, (loan1, loan2) -> {
            if ("attivo".equals(loan1.getStatus()) && !"attivo".equals(loan2.getStatus())) {
                return -1; // loan1 before loan2
            } else if (!"attivo".equals(loan1.getStatus()) && "attivo".equals(loan2.getStatus())) {
                return 1; // loan2 before loan1
            } else {
                return loan1.getDueDate().compareTo(loan2.getDueDate()); // ordina per data di scadenza
            }
        });
        notifyDataSetChanged(); // Notifica l'adapter che i dati sono cambiati
    }

    @Override
    public int getItemCount() {
        return loansList.size();
    }


    // Funzione per gestire la restituzione del libro
    private void returnBook(Loans loan, int position) {
        new Thread(() -> {
            SocketClient client = new SocketClient();
            String response = client.returnBook("delivered", loan.getBook().getIsbn(), loan.getUser().getUsername());

            // Usa un Handler per postare sul thread principale
            new Handler(Looper.getMainLooper()).post(() -> {
                if ("Stato del prestito aggiornato con successo a 'consegnato'".equals(response)) {

                    // Aggiorna lo stato a 'consegnato' senza rimuovere l'elemento
                    loan.setStatus("consegnato");
                    notifyItemChanged(position);  // Notifica l'adapter che l'elemento è cambiato

                    // Mostra un messaggio di successo
                    Toast.makeText(context, "Libro restituito con successo", Toast.LENGTH_SHORT).show();

                } else {
                    // Gestisci l'errore
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle, startDate, dueDate, status, isbn, genre;
        ImageView bookImage;
        Button buttonReturnBook;

        public LoanViewHolder(View itemView) {
            super(itemView);

            // Inizializza le view con i rispettivi ID
            bookTitle = itemView.findViewById(R.id.textViewBookNames);
            startDate = itemView.findViewById(R.id.textViewStartDate);
            dueDate = itemView.findViewById(R.id.textViewEndDate);
            status = itemView.findViewById(R.id.textViewStatus);
            isbn = itemView.findViewById(R.id.textViewISBN);
            genre = itemView.findViewById(R.id.textViewGenre);
            bookImage = itemView.findViewById(R.id.imageViewBookCover);
            buttonReturnBook = itemView.findViewById(R.id.buttonReturnBook);
        }
    }
}
