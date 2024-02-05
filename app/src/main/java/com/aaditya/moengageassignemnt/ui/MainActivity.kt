package com.aaditya.moengageassignemnt.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaditya.moengageassignemnt.R
import com.aaditya.moengageassignemnt.adapter.NewsAdapter
import com.aaditya.moengageassignemnt.util.NetworkHelper
import com.aaditya.moengageassignemnt.viewmodel.NewsViewModel

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        adapter = NewsAdapter(this@MainActivity)

        val sortRadioGroup: RadioGroup = findViewById(R.id.sortRadioGroup)

        sortRadioGroup.check(R.id.newToOldRadioButton)

        sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.oldToNewRadioButton -> newsViewModel.sortByOldToNew()
                R.id.newToOldRadioButton -> newsViewModel.sortByNewToOld()
            }
        }

        val recyclerView: RecyclerView = findViewById(R.id.rv_news)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        
        if (NetworkHelper.isInternetAvailable(this)) {
            // Fetch news only if internet is available
            newsViewModel.newsList.observe(this, Observer { articles ->
                adapter.setNewsList(articles)
            })

            newsViewModel.fetchNews()
        } else {
            // Show a Toast message if internet is not available
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }
}