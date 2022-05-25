package com.example.itk19.fragments

/*
    There are some changes I would like to do
    1) Firestore db instead of RealTime db
    2) Deprecated things - startActivityForResult and ProgressDialog  (status - done)
 */

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.itk19.R
import com.example.itk19.models.FileInfoModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class NotesFragment : Fragment() {

    private lateinit var fileBrowse: ImageView
    private lateinit var fileLogo: ImageView
    private lateinit var cancelFile: ImageView
    private lateinit var uploadFile: Button
    private lateinit var fileSubject: EditText
    private lateinit var fileSemester: EditText
    private lateinit var progressBar:ProgressBar

    private val storageReference = FirebaseStorage.getInstance().reference
    private val databaseReference = FirebaseDatabase.getInstance().getReference("documents")

    private lateinit var filePath: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)




        fileSubject = view.findViewById(R.id.file_subject)
        fileSemester = view.findViewById(R.id.file_semester)
        fileLogo = view.findViewById(R.id.filelogo)
        cancelFile = view.findViewById(R.id.cancel_file)
        fileBrowse = view.findViewById(R.id.file_browse)
        uploadFile = view.findViewById(R.id.file_upload)
        progressBar = view.findViewById(R.id.determinateBar)



        fileLogo.visibility = View.INVISIBLE
        cancelFile.visibility = View.INVISIBLE
        fileBrowse.visibility = View.VISIBLE

        cancelFile.setOnClickListener {
            fileLogo.visibility = View.INVISIBLE
            cancelFile.visibility = View.INVISIBLE
            fileBrowse.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
            fileSemester.setText("")
            fileSubject.setText("")
            Log.i("Clicked", "cancelFile Clicked!..")
        }

        fileBrowse.setOnClickListener {

                Dexter.withContext(context?.applicationContext)
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener{
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            val intent = Intent()
                            intent.type = "application/pdf"
                            intent.action = Intent.ACTION_GET_CONTENT
                           // startActivityForResult(Intent.createChooser(intent,"select pdf files"),101)
                            getResult.launch(intent)
                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                            TODO("Not yet implemented")
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?
                        ) {
                            p1?.continuePermissionRequest()
                        }

                    }).check()

        }


        // Code to Upload file

        uploadFile.setOnClickListener {
            processUpload(filePath)
        }

        return view

    }



    private val getResult = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            filePath = it.data?.data!!
            fileLogo.visibility = View.VISIBLE
            cancelFile.visibility = View.VISIBLE
            fileBrowse.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
}



    private fun processUpload(filePath: Uri) {



    progressBar.visibility = View.VISIBLE

            val storageRef = storageReference.child("uploads/"+System.currentTimeMillis()+"pdf")
            storageRef.putFile(filePath)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        val obj = FileInfoModel(fileSemester.text.toString(),fileSubject.text.toString(),filePath.toString())
                        databaseReference.push().key?.let { it1 -> databaseReference.child(it1).setValue(obj) }

                        progressBar.visibility = View.INVISIBLE
//                        pd.dismiss()
                        Toast.makeText(context?.applicationContext,"File Uploaded",Toast.LENGTH_LONG).show()

                        fileLogo.visibility = View.INVISIBLE
                        cancelFile.visibility = View.INVISIBLE
                        fileBrowse.visibility = View.VISIBLE
                        fileSemester.setText("")
                        fileSubject.setText("")

                    }


                }
                .addOnProgressListener {
                    val percent: Float = ((100)*it.bytesTransferred/it.totalByteCount).toFloat()
                    //pd.setMessage("uploaded:"+percent.toInt()+"%")
                    progressBar.progress = percent.toInt()
                }
    }


}




