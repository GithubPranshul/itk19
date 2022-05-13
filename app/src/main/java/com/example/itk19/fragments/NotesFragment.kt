package com.example.itk19.fragments



import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.itk19.R
import com.example.itk19.models.FileInfoModel
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.security.auth.Subject


class NotesFragment : Fragment() {

    private lateinit var fileBrowse:ImageView
    private lateinit var fileLogo: ImageView
    private lateinit var cancelFile:ImageView
    private lateinit var uploadFile: Button
    private lateinit var fileSubject:EditText
    private lateinit var fileSemester: EditText

   val storageReference = Firebase.storage
   val databaseReference = Firebase.firestore


    private lateinit var  filePath:Uri

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)




        fileSubject = view.findViewById(R.id.file_subject)
        fileSemester = view.findViewById(R.id.file_semester)
        fileLogo = view.findViewById(R.id.filelogo)
        cancelFile = view.findViewById(R.id.cancel_file)
        fileBrowse = view.findViewById(R.id.file_browse)
        uploadFile = view.findViewById(R.id.image_upload)



        fileLogo.visibility = View.GONE
        cancelFile.visibility =View.GONE
        fileBrowse.visibility = View.VISIBLE
        cancelFile.setOnClickListener {
            fileLogo.visibility = View.GONE
            cancelFile.visibility = View.GONE
            fileBrowse.visibility = View.VISIBLE
            Log.i("Clicked","cancelFile Clicked!..")
        }

        fileBrowse.setOnClickListener {
            Dexter.withContext(requireContext())
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener{
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        fileBrowse.setOnClickListener{
                            takePdf.launch("application/pdf")
                        }


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

    private fun processUpload(filePath: Uri) {
        var storageRef:StorageReference? = storageReference.reference.child("uploads/"+System.currentTimeMillis()+".pdf")
        if (storageRef != null) {
            storageRef.putFile(filePath)
                .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener {
                            FileInfoModel(fileSubject.text.toString(),fileSubject.text.toString(),filePath.toString())

                            databaseReference.collection(databaseReference.push())
                        }
                }
        }


    }

    val takePdf = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            fileBrowse.setImageURI(it)
            fileLogo.visibility = View.VISIBLE
            cancelFile.visibility = View.VISIBLE
          //  fileBrowse.visibility = View.GONE
        })





}




