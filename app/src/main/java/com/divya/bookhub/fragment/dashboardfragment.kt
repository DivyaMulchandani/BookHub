package com.divya.bookhub.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.divya.bookhub.R
import com.divya.bookhub.R.drawable
import com.divya.bookhub.adaptor.dashboardrecycleradaptor
import com.divya.bookhub.model.Book
import com.divya.bookhub.utl.connectionmanager
import org.json.JSONException

/*TODO: Rename parameter arguments, choose names that match*/
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [dashboardfragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class dashboardfragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerDashboad:RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var btncheckinternet:Button
    lateinit var recyclerAdaptor:dashboardrecycleradaptor
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    val bookInfo= arrayListOf<Book>()



   //lateinit var bookInfo= arrayListOf(
        /*"P.s. I love you",
        "The love hypothesis",
        "It End's with US",
        "It start's with US",
        "Ugly Love",
        "Life is what you make it",
        "Why not me",
        "The Sweetest Oblivion",
        "One Indian Girl",
        "The subtle art of not giving a fuck",
        "Verity",
        "2 states",
        "Half girlfriend"*/
   // )


    /*val bookInfo= arrayListOf<Book>(
        Book("P.s. I love you","akiu7yhgbfvdc","267", R.drawable.a),
        Book("The love hypothesis","akiu7yhgbfvdc","625", R.drawable.b),
        Book("It End's with US","akiu7yhgbfvdc","267", R.drawable.c),
        Book("It start's with US","akiu7yhgbfvdc","550", R.drawable.d),
        Book("Ugly Love","akiu7yhgbfvdc","267", R.drawable.e),
        Book("Life is what you make it","akiu7yhgbfvdc","325", R.drawable.f),
        Book("Why not me","akiu7yhgbfvdc","200", R.drawable.g),
        Book("The Sweetest Oblivion","akiu7yhgbfvdc","125", R.drawable.h),
        Book("One Indian Girl","akiu7yhgbfvdc","550", R.drawable.i),
        Book("The subtle art of not giving a fuck\"","akiu7yhgbfvdc","267", R.drawable.j),
        Book("Verity","akiu7yhgbfvdc","325", R.drawable.k),
        Book("2 states","akiu7yhgbfvdc","200", R.drawable.l),
        Book("Half girlfriend","akiu7yhgbfvdc","125", R.drawable.m)
        )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_dashboardfragment, container, false)

        setHasOptionsMenu(true)

        recyclerDashboad= view.findViewById(R.id.recyclerdashboard)

        btncheckinternet=view.findViewById(R.id.btncheckinternet)

        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)

        progressLayout.visibility= View.VISIBLE

        btncheckinternet.setOnClickListener{
            if (connectionmanager().checkconnectivity(activity as Context)){
                //Internet is available
                val dialog=AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok"){text,listener ->
                    //Do nothing
                }
                dialog.setNegativeButton("Cancel"){text,listener ->
                    //Do nothing
                }
                dialog.create()
                dialog.show()
            }
            else{
                //Internet is not available
                val dialog=AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not Found")
                dialog.setPositiveButton("Ok"){text,listener ->
                    //Do nothing
                }
                dialog.setNegativeButton("Cancel"){text,listener ->
                    //Do nothing
                }
                dialog.create()
                dialog.show()
            }
        }

        layoutManager=LinearLayoutManager(activity)
        recyclerAdaptor= dashboardrecycleradaptor(activity as Context,bookInfo)



        //request
        val queue=Volley.newRequestQueue(activity as Context)
        val url ="http://13.235.250.119/vl/book/fetch_books/"

        if (connectionmanager().checkconnectivity(activity as Context)){

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,url,null,Response.Listener{
                //here we will handle the responses

                try {
                    progressLayout.visibility=View.GONE
                    val success = it.getBoolean("success")

                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfo.add(bookObject)
                            recyclerDashboad.adapter = recyclerAdaptor
                            recyclerDashboad.layoutManager = layoutManager

                            recyclerDashboad.addItemDecoration(
                                DividerItemDecoration(
                                    recyclerDashboad.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )
                        }
                    } else {
                        Toast.makeText(
                            activity as Context,
                            "Some erorr has Occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
               } catch (e: JSONException){
                     Toast.makeText(activity as Context, "Some unexpected error occurred!!!",Toast.LENGTH_SHORT).show()
                }


            },Response.ErrorListener {
                //here we will handle the error
                if (activity != null) {
                    Toast.makeText(
                        activity as Context,
                        "Volley error occurred!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "3a660199590abd"
                    return headers
                }


            }

            queue.add(jsonObjectRequest)


        }
        else{
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text,listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment dashboardfragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            dashboardfragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard,menu)

    }

}