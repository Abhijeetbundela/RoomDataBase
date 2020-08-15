package com.example.roomdatabase.activities

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.roomdatabase.R
import com.example.roomdatabase.adapter.DataAdapter
import com.example.roomdatabase.model.Data
import com.example.roomdatabase.viewModel.DataVM
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), DataAdapter.AdapterCallback {

    private val dataList by lazy { ArrayList<Data>() }

    private val dataAdapter: DataAdapter by lazy { DataAdapter(dataList, this) }

    private val dataViewModel by lazy { ViewModelProvider(this).get(DataVM::class.java) }

    private lateinit var localMenu: Menu

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private var isGrid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        create_btn.setOnClickListener {
            addData()
        }

        dataViewModel.data.observe(this, androidx.lifecycle.Observer {
            dataAdapter.addItem(it)
        })

    }

    override fun onResume() {
        super.onResume()

        isGrid = sharedPreferences.getBoolean("isGrid", false)

        if (isGrid) {
            data_recycler_view.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
        } else {
            data_recycler_view.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        data_recycler_view.adapter = dataAdapter

    }

    private fun addData() {

        val layoutInflater = LayoutInflater.from(this)
        val myview: View = layoutInflater.inflate(R.layout.create_data, null)

        val builder =
            AlertDialog.Builder(this)

        builder.setView(myview).setCancelable(false).setTitle("Add Data")

        val titleInput: TextInputLayout = myview.findViewById(R.id.title)
        val detailInput: TextInputLayout = myview.findViewById(R.id.detail)

        builder.setPositiveButton(
            "Add"
        ) { _, _ -> }.setNegativeButton(
            "Cancel"
        ) { _, _ -> }

        val alertDialog = builder.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {
                val title: String =
                    titleInput.editText!!.text.toString().trim()
                val detail: String =
                    detailInput.editText!!.text.toString().trim()

                val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm a")
                val currentDate = sdf.format(Date())

                when {
                    title.isBlank() -> {
                        Toast.makeText(this@MainActivity, "Add Title", Toast.LENGTH_SHORT).show()
                    }
                    detail.isBlank() -> {
                        Toast.makeText(this@MainActivity, "Add Detail", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                        val todo = Data(
                            0,
                            title,
                            detail,
                            currentDate
                        )
                        dataViewModel.insert(todo)
                        alertDialog.dismiss()

                    }
                }

            }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setOnClickListener { alertDialog.dismiss() }

    }

    override fun update(data: Data) {

        val layoutInflater = LayoutInflater.from(this)
        val myview: View = layoutInflater.inflate(R.layout.create_data, null)

        val builder =
            AlertDialog.Builder(this)

        builder.setView(myview).setCancelable(false).setTitle("Update Data")

        val titleInput: TextInputLayout = myview.findViewById(R.id.title)
        val detailInput: TextInputLayout = myview.findViewById(R.id.detail)

        titleInput.editText!!.setText(data.title)
        detailInput.editText!!.setText(data.details)

        builder.setPositiveButton(
            "Update"
        ) { _, _ -> }.setNegativeButton(
            "Cancel"
        ) { _, _ -> }

        val alertDialog = builder.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {
                val title: String =
                    titleInput.editText!!.text.toString().trim()
                val detail: String =
                    detailInput.editText!!.text.toString().trim()

                val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US)
                val currentDate = sdf.format(Date())

                when {
                    title.isBlank() -> {
                        Toast.makeText(this@MainActivity, "Enter Title", Toast.LENGTH_SHORT).show()
                    }
                    detail.isBlank() -> {
                        Toast.makeText(this@MainActivity, "Enter Detail", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                        val todo = Data(
                            data.id,
                            title,
                            detail,
                            currentDate
                        )
                        dataViewModel.update(todo)
                        alertDialog.dismiss()

                    }
                }

            }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setOnClickListener { alertDialog.dismiss() }


    }

    override fun delete(data: Data) {
        val builder =
            AlertDialog.Builder(this)

        builder.setCancelable(false).setTitle("Delete Data")
            .setMessage("Are you sure you want to delete ?")

        builder.setPositiveButton(
            "Delete"
        ) { _, _ -> }.setNegativeButton(
            "Cancel"
        ) { _, _ -> }

        val alertDialog = builder.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {
                dataViewModel.delete(data)
                alertDialog.dismiss()
            }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setOnClickListener { alertDialog.dismiss() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        localMenu = menu!!
        menuInflater.inflate(R.menu.menu, menu)

        if(isGrid){
            localMenu.findItem(R.id.change_layout).setIcon(R.drawable.ic_baseline_view_stream);
        }
        else{
            localMenu.findItem(R.id.change_layout).setIcon(R.drawable.ic_baseline_dashboard);
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.delete_all -> {
                deleteAll()
                return true
            }

            R.id.change_layout -> {
                isGrid = !isGrid

                if (isGrid) {
                    localMenu.findItem(R.id.change_layout)
                        .setIcon(R.drawable.ic_baseline_view_stream);
                } else {
                    localMenu.findItem(R.id.change_layout)
                        .setIcon(R.drawable.ic_baseline_dashboard);
                }
                editor.putBoolean("isGrid", isGrid)
                editor.apply()
                onResume()

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun deleteAll() {

        val builder =
            AlertDialog.Builder(this)

        builder.setCancelable(false).setTitle("Delete All Data")
            .setMessage("Are you sure you want to delete all data?")

        builder.setPositiveButton(
            "Delete"
        ) { _, _ -> }.setNegativeButton(
            "Cancel"
        ) { _, _ -> }

        val alertDialog = builder.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {

                dataViewModel.deleteAll()
                alertDialog.dismiss()
            }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setOnClickListener { alertDialog.dismiss() }

    }
}