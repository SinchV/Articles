package com.example.articles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.articles.viewmodel.ArticleViewModel
import com.example.articles.adapter.ListOfArticleAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    lateinit var alist: RecyclerView
    lateinit var adapter: ListOfArticleAdapter
    lateinit var viewModel: ArticleViewModel

    private var isOldToNewSelected = false
    private var isNewToOldSelected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alist = findViewById(R.id.recycleList)
        alist.layoutManager = LinearLayoutManager(this)

        getAllData()
    }

    private fun getAllData() {

        viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        adapter = ListOfArticleAdapter(emptyList())
        alist.adapter = adapter
        alist.layoutManager = LinearLayoutManager(this)

        viewModel.listData.observe(this) { list ->
            adapter = ListOfArticleAdapter(list)
            alist.adapter = adapter
        }
        viewModel.listData.observe(this) { originalList ->
            if (isFilterApplied()) {
                adapter.updateList(originalList)
            } else {
                adapter = ListOfArticleAdapter(originalList)
            }

        }

        lifecycleScope.launch {
            try {
                val fetchedList = withContext(Dispatchers.IO) {
                    viewModel.fetchListData()
                }
                viewModel.updateListData(fetchedList)
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dropdown, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuOldToNew -> {
                isOldToNewSelected = true
                isNewToOldSelected = false
                viewModel.sortByDateAscending()
                true
            }

            R.id.menuNewToOld -> {
                isNewToOldSelected = true
                isOldToNewSelected = false
                viewModel.sortByDateDescending()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isFilterApplied(): Boolean {
        return isOldToNewSelected || isNewToOldSelected
    }

}