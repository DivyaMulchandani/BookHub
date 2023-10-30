package com.divya.bookhub.adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.divya.bookhub.R
import com.divya.bookhub.activity.descriptionActivity
import com.divya.bookhub.model.Book
import com.squareup.picasso.Picasso

class dashboardrecycleradaptor(val context:Context,val itemlist:ArrayList<Book>):
    RecyclerView.Adapter<dashboardrecycleradaptor.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.single_row_dashboard,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemlist.size

    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book=itemlist[position]
        holder.bookname.text=book.bookName
        holder.authorname.text=book.bookAuthor
        holder.price.text=book.bookPrice
        //holder.cover.setImageResource(book.bookImg)
        Picasso.get().load(book.bookImg).error(R.drawable.default_book_cover).into(holder.cover)

        holder.bookcontent.setOnClickListener{
            val intent = Intent(context, descriptionActivity::class.java)
            intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)    }
    }


    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view){
        val bookname : TextView=view.findViewById(R.id.txtrowitem)
        val authorname :TextView=view.findViewById(R.id.txtauthorname)
        val price :TextView=view.findViewById(R.id.txtprice)
        val cover :ImageView=view.findViewById(R.id.imgbook)
        val bookcontent:RelativeLayout=view.findViewById(R.id.bookcontent)
    }



}

