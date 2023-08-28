package com.tutorials.eu.favdish.view.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tutorials.eu.favdish.R
import com.tutorials.eu.favdish.application.FavDishApplication
import com.tutorials.eu.favdish.databinding.FragmentRandomDishBinding
import com.tutorials.eu.favdish.model.entities.FavDish
import com.tutorials.eu.favdish.model.entities.RandomDish
import com.tutorials.eu.favdish.utils.Constants
import com.tutorials.eu.favdish.viewmodel.FavDishViewModel
import com.tutorials.eu.favdish.viewmodel.FavDishViewModelFactory
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
        binding!!.srlRandomDish.setOnRefreshListener {
            mRandomDishViewModel.getRandomRecipeFromAPI()
        }
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(
            viewLifecycleOwner,
            Observer { randomDishResponse ->
                randomDishResponse?.let {
                    Log.i("Random Dish Response", "${randomDishResponse.recipes[0]}")
                    if (binding!!.srlRandomDish.isRefreshing) {
                        binding!!.srlRandomDish.isRefreshing = false
                    }
                    setRandomDishResponseInUI(randomDishResponse.recipes[0])
                }
            })
        mRandomDishViewModel.randomDishLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Random Dish API Error", "$dataError")
                    if (binding!!.srlRandomDish.isRefreshing) {
                        binding!!.srlRandomDish.isRefreshing = false
                    }
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

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe) {
        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(binding!!.ivDishImage)

        binding!!.tvTitle.text = recipe.title

        var dishType: String = "Other"

        if (recipe.dishTypes.isNotEmpty()) {
            dishType = recipe.dishTypes[0]
            binding!!.tvType.text = dishType
        }

        binding!!.tvCategory.text = "Other"

        var ingredients = ""
        for (value in recipe.extendedIngredients) {
            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
                ingredients = ingredients + ", \n" + value.original
            }
        }

        binding!!.tvIngredients.text = ingredients

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding!!.tvCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            @Suppress("DEPRECATION")
            binding!!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        binding!!.ivFavoriteDish.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_favorite_unselected,
                null
            )
        )

        var addedToFavorite = false

        binding!!.tvCookingTime.text =
            resources.getString(
                R.string.lbl_estimate_cooking_time,
                recipe.readyInMinutes.toString()
            )

        binding!!.ivFavoriteDish.setOnClickListener {
            if (addedToFavorite) {
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_already_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val randomDishDetails = FavDish(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "Other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )
                val mFavDishViewModel: FavDishViewModel by viewModels() {
                    FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
                }
                mFavDishViewModel.insert(randomDishDetails)

                addedToFavorite = true


                binding!!.ivFavoriteDish.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_favorite_selected,
                        null
                    )
                )
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}