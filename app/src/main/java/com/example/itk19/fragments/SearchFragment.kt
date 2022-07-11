package com.example.itk19.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itk19.MyAdapter
import com.example.itk19.R
import com.example.itk19.models.FileInfoModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase




class SearchFragment : Fragment() {

    private lateinit var rvViewPdf:RecyclerView
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_search, container, false)

        rvViewPdf = view.findViewById(R.id.rvViewPdf)
        rvViewPdf.layoutManager = LinearLayoutManager(context)


        val options =  FirestoreRecyclerOptions.Builder<FileInfoModel>()
            .setQuery(Firebase.firestore.collection("documents"),FileInfoModel::class.java)
            .build()

        adapter = MyAdapter(options)
        rvViewPdf.adapter = adapter


        return view


    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_bar,menu)
        val item = menu.findItem(R.id.search_icon) as MenuItem
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {

                processSearch(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                processSearch(p0)
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)

    }



    private fun processSearch(p0: String?) {
        val options =  FirestoreRecyclerOptions.Builder<FileInfoModel>()
            .setQuery(Firebase.firestore.collection("documents").orderBy("fileSubject").orderBy("fileSemester").startAt(p0).endAt(p0+"\uf8ff"),FileInfoModel::class.java)
            .build()

        val adapter = MyAdapter(options)
        adapter.startListening()
        rvViewPdf.adapter = adapter
    }


}
