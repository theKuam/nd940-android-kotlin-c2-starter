package com.udacity.asteroidradar.main

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber


class MainFragment : Fragment() {

    private val viewModel by lazy {
        val activity = requireNotNull(this.activity)
        val viewModelFactory = MainViewModelFactory(activity.application)
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.picasso = Picasso.with(context)

        val asteroidAdapter = AsteroidAdapter(AsteroidAdapter.AsteroidOnClickListener { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        binding.asteroidRecycler.adapter = asteroidAdapter

        viewModel.getSavedAsteroids()
        viewModel.asteroids.observe(viewLifecycleOwner) {
            Timber.d(it?.toString())
            asteroidAdapter.submitList(it)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.forEach {
            val s = SpannableString(it.title)
            s.setSpan(ForegroundColorSpan(Color.BLACK), 0, s.length, 0)
            it.title = s
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> {
                viewModel.getWeekAsteroids()
            }
            R.id.show_today_menu -> {
                viewModel.getTodayAsteroids()
            }
            R.id.show_saved_menu -> {
                viewModel.getSavedAsteroids()
            }
        }
        return true
    }
}
