package com.azhar.githubusers.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azhar.githubusers.R
import com.azhar.githubusers.adapter.SearchAdapter
import com.azhar.githubusers.model.search.ModelSearchData
import com.azhar.githubusers.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    var searchAdapter: SearchAdapter? = null
    var searchViewModel: UserViewModel? = null
    var progressDialog: ProgressDialog? = null
    var rvListUser: RecyclerView? = null
    var searchUser: EditText? = null
    var imageClear: ImageView? = null
    var imageFavorite: ImageView? = null
    var layoutEmpty: ConstraintLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchUser = findViewById(R.id.searchUser)
        imageClear = findViewById(R.id.imageClear)
        imageFavorite = findViewById(R.id.imageFavorite)
        rvListUser = findViewById(R.id.rvListUser)
        layoutEmpty = findViewById(R.id.layoutEmpty)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Mohon Tunggu...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Sedang menampilkan data")
        imageClear.setOnClickListener(View.OnClickListener { view: View? ->
            searchUser.getText().clear()
            imageClear.setVisibility(View.GONE)
            layoutEmpty.setVisibility(View.VISIBLE)
            rvListUser.setVisibility(View.GONE)
        })
        imageFavorite.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(intent)
        })

        //method action search
        searchUser.setOnEditorActionListener(OnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            val strUsername = searchUser.getText().toString()
            if (strUsername.isEmpty()) {
                Toast.makeText(this@MainActivity, "Form tidak boleh kosong!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    progressDialog!!.show()
                    searchViewModel!!.setSearchUser(strUsername)
                    val inputMethodManager =
                        v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                    imageClear.setVisibility(View.VISIBLE)
                    layoutEmpty.setVisibility(View.GONE)
                    return@setOnEditorActionListener true
                }
            }
            false
        })
        searchAdapter = SearchAdapter(this)
        rvListUser.setLayoutManager(LinearLayoutManager(this))
        rvListUser.setAdapter(searchAdapter)
        rvListUser.setHasFixedSize(true)

        //method set viewmodel
        searchViewModel = ViewModelProvider(this, NewInstanceFactory()).get(
            UserViewModel::class.java
        )
        searchViewModel!!.resultList.observe(this) { modelSearchData: ArrayList<ModelSearchData?> ->
            progressDialog!!.dismiss()
            if (modelSearchData.size != 0) {
                searchAdapter!!.setSearchUserList(modelSearchData)
            } else {
                Toast.makeText(this@MainActivity, "Pengguna Tidak Ditemukan!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}