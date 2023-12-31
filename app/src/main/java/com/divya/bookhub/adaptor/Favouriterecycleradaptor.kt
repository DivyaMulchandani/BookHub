package com.divya.bookhub.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.divya.bookhub.R
import com.divya.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class Favouriterecycleradaptor(val context: Context, val bookList: List<BookEntity>): RecyclerView.Adapter<Favouriterecycleradaptor.Favouriteviewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Favouriteviewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favorite_single_row,parent,false)

        return Favouriteviewholder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: Favouriteviewholder, position: Int) {
        val book = bookList[position]

        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)
    }


    class Favouriteviewholder(view: View):RecyclerView.ViewHolder(view){
        val txtBookName : TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtBookAuthor : TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice : TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating : TextView = view.findViewById(R.id.txtFavBookRating)
        val imgBookImage : ImageView = view.findViewById(R.id.imgFavBookImage)
        val llContent : LinearLayout = view.findViewById(R.id.llFavContent)

    }

}