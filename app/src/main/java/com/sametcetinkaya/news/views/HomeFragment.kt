package com.sametcetinkaya.news.views


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sametcetinkaya.news.R
import com.sametcetinkaya.news.adapter.NewsAdapter
import com.sametcetinkaya.news.model.Article
import com.sametcetinkaya.news.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.search_header_layout.*
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var newsAdapter: NewsAdapter
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var articleList: List<Article>

    private val DEBUG: Boolean = true
    private val TAG = NewsDetailFragment::class.java.simpleName


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeLiveData()
        refreshNews()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_newsDetailFragment,
                bundle
            )
        }
        search_edit_text.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().toLowerCase(Locale.getDefault())
            filterWithQuery(query)
            toggleImageView(query)
        }

        clear_search_query.setOnClickListener {
            search_edit_text.setText("")
        }
    }


    private fun observeLiveData() {
        viewModel.apply {
            newsLiveData.observe(viewLifecycleOwner, Observer {
                newsAdapter.differ.submitList(it)
                articleList = it
            })
        }
    }

    private fun refreshNews() {
        refreshLayout.setOnRefreshListener {
            viewModel.getBreakingNews()
            Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show()
            refreshLayout.isRefreshing = false
        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        news_recycler_view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun Log(message: String) {
        if (DEBUG) {
            android.util.Log.d(TAG, message)
        }
    }


    private fun filterWithQuery(query: String) {
        if (query.isNotEmpty()) {
            val filteredList: List<Article> = onFilterChanged(query)
            attachAdapter(filteredList)
        } else if (query.isEmpty()) {
            attachAdapter(articleList)
        }
    }

    private fun onFilterChanged(filterQuery: String): List<Article> {
        val filteredList = ArrayList<Article>()
        for (currentSport in articleList) {
            if (currentSport.title.toLowerCase(Locale.getDefault()).contains(filterQuery)) {
                filteredList.add(currentSport)
            }
        }
        return filteredList
    }

    private fun toggleImageView(query: String) {
        if (query.isNotEmpty()) {
            clear_search_query.visibility = View.VISIBLE
        } else if (query.isEmpty()) {
            clear_search_query.visibility = View.INVISIBLE
        }
    }

    private fun attachAdapter(list: List<Article>) {
        newsAdapter.differ.submitList(list)
    }
}










