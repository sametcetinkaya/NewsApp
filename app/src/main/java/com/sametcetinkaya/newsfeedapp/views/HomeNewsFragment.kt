package com.sametcetinkaya.newsfeedapp.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sametcetinkaya.newsfeedapp.R
import com.sametcetinkaya.newsfeedapp.adapters.NewsAdapter
import com.sametcetinkaya.newsfeedapp.databinding.HomeNewsFragmentBinding
import com.sametcetinkaya.newsfeedapp.models.Article
import com.sametcetinkaya.newsfeedapp.utils.Resource
import com.sametcetinkaya.newsfeedapp.viewmodels.HomeNewsViewModel
import com.sametcetinkaya.newsfeedapp.viewmodels.SavedNewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeNewsFragment :Fragment(R.layout.home_news_fragment) {

    val viewModel : HomeNewsViewModel by viewModels()
    val saveViewModel : SavedNewsViewModel by viewModels()
    private var _binding: HomeNewsFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var madapter:NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomeNewsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        binding.toolbar?.title = "Gündem Başlıkları"


        madapter.setOnArticleListener {

            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_homeNewsFragment_to_newsDetailFragment, bundle
            )
/*            val customTabIntent = CustomTabsIntent.Builder().build()
            customTabIntent.launchUrl(requireContext(),article.url!!.toUri())*/

        }

        madapter.setOnArticleSaveListener { article ->
            saveViewModel.insertArticle(article)
            Snackbar.make(view,"Article Saved Succesfully", Snackbar.LENGTH_SHORT).show()
        }

        madapter.setOnArticleShareListener { articleUrl->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,articleUrl)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent,"Share News using: "))
        }

        viewModel.breakingNews.observe(viewLifecycleOwner,{newsResource->
            when(newsResource){
                is Resource.Loading ->{
                    binding.errorImage.visibility = View.GONE
                    binding.pgBar.visibility = View.VISIBLE

                }
                is Resource.Error->{

                    binding.errorImage.visibility = View.VISIBLE
                    binding.pgBar.visibility = View.GONE

                }
                is Resource.Success->{
                    binding.errorImage.visibility = View.GONE
                    binding.pgBar.visibility = View.GONE
                    madapter.submitList(newsResource.data?.articles)
                }
            }
        })

    }
   private fun setUpRecyclerView(){
        madapter = NewsAdapter()
        binding.rvBreakingNews.apply {

            adapter = madapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}