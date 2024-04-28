package com.learn.cancerapp.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.cancerapp.data.history.ViewModelFactory
import com.learn.cancerapp.data.history.entity.HistoryEntity
import com.learn.cancerapp.databinding.FragmentHistoryBinding
import com.learn.cancerapp.ui.analysis.ResultActivity

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel = obtainViewModel(this.requireActivity() as AppCompatActivity)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvHistory.layoutManager = LinearLayoutManager(requireActivity())

        // Menampilkan ProgressBar saat data dimuat
        historyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        historyViewModel.getAllHistory().observe(requireActivity()) { favoriteUsers ->
            setUsersData(favoriteUsers)
        }

        return root
    }

    private fun setUsersData(users: List<HistoryEntity>) {
        binding?.let {
            val adapter = HistoryAdapter()
            adapter.submitList(users)
            it.rvHistory.adapter = adapter

            // Menambahkan event click pada item list
            adapter.setOnItemClickCallback(object : HistoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: HistoryEntity) {
                    showSelectedHero(data)
                }
            })
        }
    }

    private fun showSelectedHero(user: HistoryEntity) {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, user.gambar)
        intent.putExtra(ResultActivity.EXTRA_RESULT, user.hasilPrediksi)
        startActivity(intent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}