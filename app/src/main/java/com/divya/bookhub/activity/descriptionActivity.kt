package com.divya.bookhub.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.AsyncCache
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.divya.bookhub.R
import com.divya.bookhub.database.BookDatabase
import com.divya.bookhub.database.BookEntity
import com.divya.bookhub.utl.connectionmanager
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception


class descriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout

    lateinit var toolbar: Toolbar

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.bookimg)
        txtBookDesc = findViewById(R.id.txtBookData)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@descriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@descriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        val queue = Volley.newRequestQueue(this@descriptionActivity)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (connectionmanager().checkconnectivity(this@descriptionActivity)) {

            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE

                            val bookImageUrl = bookJsonObject.getString("image")

                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)
                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuthor.text = bookJsonObject.getString("autho")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookRating.text = bookJsonObject.getString("rating")
                            txtBookDesc.text = bookJsonObject.getString("description")

                            val bookEntity=BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookRating.text.toString(),
                                txtBookDesc.text.toString(),
                                bookImageUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav = checkFav.get()

                            if(isFav){
                                btnAddToFav.text="Remove from Favourites"
                                val favcolor = ContextCompat.getColor(applicationContext,R.color.colorFav)
                                btnAddToFav.setBackgroundColor(favcolor)
                            }else{
                                btnAddToFav.text="Add to Favourites"
                                val nofavColor = ContextCompat.getColor(applicationContext, R.color.teal_200)
                                btnAddToFav.setBackgroundColor(nofavColor)
                            }

                            btnAddToFav.setOnClickListener{

                                if (!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){

                                    val async = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                    val result = async.get()
                                    if (result){
                                        Toast.makeText(
                                            this@descriptionActivity,
                                            "Book added to favourites",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        btnAddToFav.text="Remove from favourites"
                                        val favColor= ContextCompat.getColor(applicationContext,R.color.colorFav)
                                        btnAddToFav.setBackgroundColor(favColor)
                                    }else{
                                        Toast.makeText(
                                            this@descriptionActivity,
                                            "Some Error Occurred !!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                                else{
                                    val async = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result = async.get()

                                    if (result){
                                        Toast.makeText(
                                            this@descriptionActivity,
                                            "Book removed from favourites !!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        btnAddToFav.text="Add to favourites"
                                        val favColor= ContextCompat.getColor(applicationContext,R.color.teal_200)
                                        btnAddToFav.setBackgroundColor(favColor)
                                    }else{
                                        Toast.makeText(
                                            this@descriptionActivity,
                                            "Some Error Occurred !!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                this@descriptionActivity,
                                "Some erorr has Occurred!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@descriptionActivity,
                            "Some unexpected error occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@descriptionActivity,
                        "Volley error occurred!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["taken"] = "3a660199590abd"
                        return headers
                    }
                }

            queue.add(jsonRequest)

        }
        else{
            val dialog= AlertDialog.Builder(this@descriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text,listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit"){text,listener ->
                ActivityCompat.finishAffinity(this@descriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsyncTask(val context: Context,val bookEntity: BookEntity , val mode:Int) : AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is favourite or not
        Mode 2 -> Save the book into DB as favourite
        Mode 3 -> Remove the favourite book
        */

        val db = Room.databaseBuilder(context, BookDatabase::class.java,"books-db").build()


        override fun doInBackground(vararg p0: Void?): Boolean {

            when(mode){
                1 ->{
                    //check DB if the book is fav or not
                    val book:BookEntity?= db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book!=null
                }
                2 ->{
                    //save the book into DB as fav
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 ->{
                    //Remove the book from fav
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }

             return false
        }
    }

}