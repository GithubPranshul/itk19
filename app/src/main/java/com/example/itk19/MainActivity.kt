package com.example.itk19


import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.itk19.fragments.HomeFragment
import com.example.itk19.fragments.NotesFragment
import com.example.itk19.fragments.SearchFragment

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val notesFragment = NotesFragment()

    private lateinit var uploadFile:Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        replaceFragment(homeFragment)

        /* There is a chance of getting null pointer in navigation bar don't change the
            position or take caution
         */
        val navigationBar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigationBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_search -> replaceFragment(searchFragment)
                R.id.ic_notes -> replaceFragment(notesFragment)
            }
            true

        }




    }

    private fun replaceFragment(fragment: Fragment){

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()

    }

}