package com.sametcetinkaya.news.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.sametcetinkaya.news.R
import com.sametcetinkaya.news.viewmodels.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news_detail.*

@AndroidEntryPoint
class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private val viewModel: HomeViewModel by viewModels()
    private val DEBUG: Boolean = true
    private val TAG = NewsDetailFragment::class.java.simpleName
    private val args: NewsDetailFragmentArgs by navArgs()
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim) }
    private var clicked = false
    private var shareIntent = Intent()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.article
        Log("articlex"+article)
        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }
        fabAdd.setOnClickListener {
            onAddButtonClicked()
        }
        fabShare.setOnClickListener {
            article.url?.let {
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT,article.url)
                startActivity(Intent.createChooser(shareIntent, "Share text via"))
            }
        }
        fabFav.setOnClickListener {
            Log("clicked")
            Log("article"+article)
            try {
                viewModel.saveNews(article)
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
            fabFav.visibility = View.VISIBLE
            fabShare.visibility = View.VISIBLE
        }else{
            fabFav.visibility = View.INVISIBLE
            fabShare.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean){
        if(!clicked){
            fabFav.startAnimation(fromBottom)
            fabShare.startAnimation(fromBottom)
            fabAdd.startAnimation(rotateOpen)
        }else{
            fabFav.startAnimation(toBottom)
            fabShare.startAnimation(toBottom)
            fabAdd.startAnimation(rotateClose)
        }
    }

    private fun Log(message: String) {
        if (DEBUG) {
            Log.d(TAG, message)
        }

    }

}


