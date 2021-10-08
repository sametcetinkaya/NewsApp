package com.sametcetinkaya.newsfeedapp.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.sametcetinkaya.newsfeedapp.R
import com.sametcetinkaya.newsfeedapp.databinding.NewsDetailFragmentBinding
import com.sametcetinkaya.newsfeedapp.viewmodels.SavedNewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : Fragment(R.layout.news_detail_fragment) {

    private val viewModel: SavedNewsViewModel by viewModels()
    private val DEBUG: Boolean = true
    private val TAG = NewsDetailFragment::class.java.simpleName
    private val args: NewsDetailFragmentArgs by navArgs()
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim) }
    private var clicked = false
    private var shareIntent = Intent()

    private var _binding: NewsDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = NewsDetailFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        val mUrl = article.url
        binding.toolbar?.title = "Sizin İçin Haberler"
        binding.toolbar?.subtitle = mUrl
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }
        binding.fabAdd.setOnClickListener {
            onAddButtonClicked()
        }
        binding.fabShare.setOnClickListener {
            article.url?.let {
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT,article.url)
                startActivity(Intent.createChooser(shareIntent, "Share text via"))
            }
        }
        binding.fabFav.setOnClickListener {
            try {
                viewModel.insertArticle(article)
                Snackbar.make(view, "New saved successfully", Snackbar.LENGTH_SHORT).show()
            }catch (e:Exception){
                Log("error"+e.message)
            }

        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked){
            binding.fabFav.visibility = View.VISIBLE
            binding.fabShare.visibility = View.VISIBLE
        }else{
            binding.fabFav.visibility = View.INVISIBLE
            binding.fabShare.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean){
        if(!clicked){
            binding.fabFav.startAnimation(fromBottom)
            binding.fabShare.startAnimation(fromBottom)
            binding.fabAdd.startAnimation(rotateOpen)
        }else{
            binding.fabFav.startAnimation(toBottom)
            binding.fabShare.startAnimation(toBottom)
            binding.fabAdd.startAnimation(rotateClose)
        }
    }

    private fun Log(message: String) {
        if (DEBUG) {
            android.util.Log.d(TAG, message)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}