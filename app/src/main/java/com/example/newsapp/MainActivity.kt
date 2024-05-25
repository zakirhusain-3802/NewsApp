package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Repo.NewsRepository
import com.example.newsapp.adapter.ArticleAdapter
import com.example.newsapp.apiservice.RetrofitInstance
import com.example.newsapp.dataclass.NewsResponse
import com.example.newsapp.viewModel.NewsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var auth: FirebaseAuth



    var category = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val newsApiService = RetrofitInstance.provideAPI()
        val newsRepository = NewsRepository(newsApiService)
        newsViewModel = NewsViewModel(newsRepository)

        auth = FirebaseAuth.getInstance()

        newsViewModel.fetchNews("")

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val topics =
            listOf("Top Headlines", "Sports", "Business", "Entertainment", "Health", "Science")
        topics.forEach { topic ->
            tabLayout.addTab(tabLayout.newTab().setText(topic))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val selectedTopic = topics[tab.position]
                category = if (selectedTopic == "Top Headlines") "" else selectedTopic
                newsViewModel.fetchNews(category)

                Toast.makeText(this@MainActivity, "SLected Topic", Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Not used
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Not used
            }
        })
        val searchView:SearchView= findViewById(R.id.search_issues)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    tabLayout.visibility = TabLayout.GONE
                } else {
                    tabLayout.visibility = TabLayout.VISIBLE
                }

                newsViewModel.searchNews(newText.toString())
                return true
            }
        })
        searchView.setOnCloseListener {
            tabLayout.visibility = TabLayout.VISIBLE
            newsViewModel.fetchNews(category)
            searchView.setQuery("", false)
            false
        }


        recyclerView = findViewById(R.id.topHeadlinesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        articleAdapter = ArticleAdapter(emptyList(), this) // Initially, pass an empty list

        // Set the adapter to the RecyclerView
        recyclerView.adapter = articleAdapter

        newsViewModel.newsData.observe(this) { newsResponse ->

            newsResponse?.articles?.let {
                articleAdapter.setData(it)
            }
        }

        newsViewModel.isLoading.observe(this) { isLoading ->
            // Handle loading state (e.g., show/hide a progress bar)
        }


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_out -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        auth.signOut()
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
        val intent= Intent(this,LogInActivity::class.java)
        startActivity(intent)

        // Navigate to login screen or handle UI changes as needed
    }
}
