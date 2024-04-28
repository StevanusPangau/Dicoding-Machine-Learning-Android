package com.learn.cancerapp.ui.articles

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.cancerapp.data.articles.response.ArticlesItem
import com.learn.cancerapp.databinding.FragmentHomeBinding

class ArticleFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticles.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvArticles.addItemDecoration(itemDecoration)

        articleViewModel.listReview.observe(viewLifecycleOwner) { articles ->
            setUsersData(articles)
        }

        articleViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUsersData(users: List<ArticlesItem>) {
        val adapter = ArticleAdapter()
        adapter.submitList(users)
        binding.rvArticles.adapter = adapter

        // Menambahkan event click pada item list
        adapter.setOnItemClickCallback(object : ArticleAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArticlesItem) {
                showSelectedHero(data)
            }
        })
    }

    private fun showSelectedHero(article: ArticlesItem) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}