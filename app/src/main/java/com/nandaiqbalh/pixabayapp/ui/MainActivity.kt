package com.nandaiqbalh.pixabayapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.nandaiqbalh.pixabayapp.R
import com.nandaiqbalh.pixabayapp.databinding.ActivityMainBinding
import com.nandaiqbalh.pixabayapp.provider.ServiceLocator
import com.nandaiqbalh.pixabayapp.util.viewModelFactory
import com.nandaiqbalh.pixabayapp.wrapper.Resource

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModelFactory {
        MainViewModel(ServiceLocator.provideRepository(applicationContext))
    }

    private val adapter: PostAdapter by lazy {
        PostAdapter {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initList()
        observeData()

    }

    private fun observeData() {
        viewModel.searchResult.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.pbPost.isVisible = true
                    binding.rvPost.isVisible = false
                    binding.tvError.isVisible = false
                }
                is Resource.Error -> {
                    adapter.clearItems()
                    binding.pbPost.isVisible = false
                    binding.rvPost.isVisible = false
                    binding.tvError.isVisible = true
                    it.exception?.message?.let { er ->
                        binding.tvError.text = er
                    }
                }
                is Resource.Empty -> {
                    adapter.clearItems()
                    binding.pbPost.isVisible = false
                    binding.rvPost.isVisible = false
                    binding.tvError.isVisible = true
                    binding.tvError.text = getString(R.string.text_empty_state)
                }
                is Resource.Success -> {
                    it.payload?.posts?.let { data -> adapter.setItems(data) }
                    binding.pbPost.isVisible = false
                    binding.rvPost.isVisible = true
                    binding.tvError.isVisible = false
                }
            }
        }
    }

    private fun initList() {
        binding.rvPost.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val search = menu.findItem(R.id.menu_search_bar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.title_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchPost(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}