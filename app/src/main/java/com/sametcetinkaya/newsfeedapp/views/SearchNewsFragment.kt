package com.sametcetinkaya.newsfeedapp.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.sametcetinkaya.newsfeedapp.R
import com.sametcetinkaya.newsfeedapp.adapters.NewsAdapter
import com.sametcetinkaya.newsfeedapp.databinding.SearchNewsFragmentBinding
import com.sametcetinkaya.newsfeedapp.utils.Constants.SEARCH_NEWS_TIME_DELAY
import com.sametcetinkaya.newsfeedapp.utils.Resource
import com.sametcetinkaya.newsfeedapp.viewmodels.SavedNewsViewModel
import com.sametcetinkaya.newsfeedapp.viewmodels.SearchNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.search_news_fragment) {


    lateinit var madapter: NewsAdapter
    private var _binding: SearchNewsFragmentBinding? = null
    val binding get() = _binding!!
    val searchViewModel: SearchNewsViewModel by viewModels()
    val saveViewModel: SavedNewsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchNewsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar?.title = "Keşfet"
        setUpRecyclerView()

        madapter.setOnArticleListener {

            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_newsDetailFragment, bundle
            )
/*            val customTabIntent = CustomTabsIntent.Builder().build()
            customTabIntent.launchUrl(requireContext(),article.url!!.toUri())*/
        }

        madapter.setOnArticleShareListener { articleUrl ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, articleUrl)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, "Share News using: "))
        }

        madapter.setOnArticleSaveListener { article ->
            saveViewModel.insertArticle(article)
            Snackbar.make(view, "Kayıt Başarılı", Snackbar.LENGTH_SHORT).show()
        }


        var job: Job? = null
        binding.searchView.addTextChangedListener { editable ->


            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {

                        binding.chipGroup.clearCheck()
                        searchViewModel.setQuery(editable.toString())
                    } else {
                        madapter.submitList(listOf())
                    }
                }
            }

        }

        searchViewModel.searchQuery.observe(viewLifecycleOwner, {
            searchViewModel.searchForNews(it)
        })

        searchViewModel.searchedNews.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.errorImage.visibility = View.GONE
                    binding.pgBar.visibility = View.VISIBLE

                }
                is Resource.Error -> {

                    binding.errorImage.visibility = View.VISIBLE
                    binding.pgBar.visibility = View.GONE

                }
                is Resource.Success -> {
                    binding.errorImage.visibility = View.GONE
                    binding.pgBar.visibility = View.GONE
                    madapter.submitList(it.data?.articles)
                }
            }
        })




        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->

            val chip: Chip? = group.findViewById<Chip>(checkedId)
            chip?.let {
                binding.searchView.clearFocus()
                binding.searchView.text.clear()
                searchViewModel.setQuery(chip.text.toString())
            }
        }


    }

    fun setUpRecyclerView() {
        madapter = NewsAdapter()
        binding.rvSearchNews.apply {

            adapter = madapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}