package com.example.gatopedia.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gatopedia.databinding.FragmentHomeBinding
import com.example.gatopedia.view.adapter.CatBreedAdapter
import com.example.gatopedia.viewmodel.HomeViewModel

private const val TWO_CARDS_IN_LINE = 2

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val adapter = configureAdapter()

        observeUpdateImage(adapter)
        observerError()
        handleSearchBreedByName()

        viewModel.fetchCatList()

    }

    private fun handleSearchBreedByName() {
        binding.searchHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchBreedsByName(it)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?) = false
        })
    }

    private fun observeUpdateImage(adapter: CatBreedAdapter) {
        viewModel.catImages.observe(viewLifecycleOwner, Observer { catImages ->
            catImages?.let { adapter.updateData(it) }
        })
    }

    private fun configureAdapter(): CatBreedAdapter {
        val adapter = CatBreedAdapter(emptyList())
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), TWO_CARDS_IN_LINE)
        binding.recyclerView.adapter = adapter
        return adapter
    }

    private fun observerError() {
        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    // Prevents memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}