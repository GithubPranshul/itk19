package com.example.itk19

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itk19.models.FileInfoModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class MyAdapter(options: FirestoreRecyclerOptions<FileInfoModel>) : FirestoreRecyclerAdapter<FileInfoModel, MyAdapter.myViewHolder>(
    options
) {
    class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etSemester: TextView = itemView.findViewById(R.id.etSemester)
        val etSubject:TextView = itemView.findViewById(R.id.etSubject)
        val ivPdfView:ImageView = itemView.findViewById(R.id.ivPdfView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_design,parent,false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int, model: FileInfoModel) {
        holder.etSubject.text = model.fileSubject
        holder.etSemester.text = model.fileSemester
    }
}
