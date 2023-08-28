package com.tutorials.eu.favdish.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tutorials.eu.favdish.R
import com.tutorials.eu.favdish.databinding.FragmentRandomDishBinding
import com.tutorials.eu.favdish.viewmodel.NotificationsViewModel
import com.tutorials.eu.favdish.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var mRandomDishViewModel: RandomDishViewModel

    private var binding: FragmentRandomDishBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)
        mRandomDishViewModel.getRandomRecipeFromAPI()
        randomDishViewModelObserver()
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(
            viewLifecycleOwner,
            Observer { randomDishResponse ->
                randomDishResponse?.let {
                    Log.i("Random Dish Response", "${randomDishResponse.recipes[0]}")
                }
            })
        mRandomDishViewModel.randomDishLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Random Dish API Error", "$dataError")
                }
            })
        mRandomDishViewModel.loadRandomDish.observe(
            viewLifecycleOwner,
            Observer { loadRandomDish ->
                loadRandomDish?.let {
                    Log.i("Random Dish Loading", "$loadRandomDish")
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}