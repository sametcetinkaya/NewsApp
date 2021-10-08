package com.sametcetinkaya.newsfeedapp.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sametcetinkaya.newsfeedapp.R
import com.sametcetinkaya.newsfeedapp.adapters.NewsAdapter
import com.sametcetinkaya.newsfeedapp.databinding.SavedNewsFragmentBinding
import com.sametcetinkaya.newsfeedapp.viewmodels.SavedNewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : Fragment (R.layout.saved_news_fragment){

    private var _binding: SavedNewsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SavedNewsViewModel by viewModels()
    lateinit var sadapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SavedNewsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar?.setTitle("Kayıtlı Haberler")
        setUpRecyclerView()
        viewModel.listSavedNews.observe(viewLifecycleOwner,{
            sadapter.submitList(it)
        })

        sadapter.setOnArticleListener {

            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_newsDetailFragment, bundle
            )
/*            val customTabIntent = CustomTabsIntent.Builder().build()
            customTabIntent.launchUrl(requireContext(),article.url!!.toUri())*/

        }

        sadapter.setOnArticleSaveListener { article ->
            viewModel.deleteArticle(article)
            Snackbar.make(view,"Silme işlemi başarılı", Snackbar.LENGTH_SHORT).show()
        }

        sadapter.setOnArticleShareListener { articleUrl->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,articleUrl)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent,"Share News using: "))
        }


        val callback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val removedArticlePosition = viewHolder.adapterPosition
                val removedArticle = sadapter.currentList[removedArticlePosition]
                viewModel.deleteArticle(removedArticle)
                Snackbar.make(view,"Silme işlemi başarılı", Snackbar.LENGTH_LONG).apply {
                    setAction("Geri Al"){
                        viewModel.insertArticle(removedArticle)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(binding.rvSavedNews)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun setUpRecyclerView(){
        sadapter = NewsAdapter()
        binding.rvSavedNews.apply {

            adapter = sadapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}