package com.example.practiceapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.practiceapp.databinding.FragmentCoffeeDetailBinding
import com.example.practiceapp.databinding.FragmentHomeBinding

const val TAG = "CoffeeDetailFragment"
class CoffeeDetailFragment : Fragment() {
    private var _binding: FragmentCoffeeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCoffeeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val title = bundle?.getString("title")
        val image = bundle?.getString("image")
        val description = bundle?.getString("description")

        binding.tvCoffee.setText(title)
        binding.tvDescription.setText(description)
        Glide
            .with(requireContext())
            .load(image)
            .circleCrop()
            .into(binding.ivCoffee);
    }
}