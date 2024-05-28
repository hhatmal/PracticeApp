
package com.example.practiceapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practiceapp.CoffeeRowItemUi
import com.example.practiceapp.R
import com.example.practiceapp.databinding.FragmentHomeBinding
import com.example.practiceapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var rvRowClickListener: CustomAdapter.ItemClickListener
    private var adapter: CustomAdapter? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvRowClickListener = CustomAdapter.ItemClickListener { coffeeName ->
            viewModel.coffeeSelected(coffeeName)
        }

        adapter = CustomAdapter(rvRowClickListener)
        binding.rvCoffee.layoutManager = LinearLayoutManager(context)
        binding.rvCoffee.adapter = adapter

        viewModel.getCoffeeListLiveData().observe(viewLifecycleOwner, { coffeeCardUiList ->
            adapter?.updateData(coffeeCardUiList)
        })

        viewModel.getCoffeeSelectedEventLiveData().observe(viewLifecycleOwner, { coffeeDetailUi ->
            val detailFragment = CoffeeDetailFragment()
            val bundle = Bundle()
            bundle.putString("title", coffeeDetailUi.title)
            bundle.putString("image", coffeeDetailUi.image)
            bundle.putString("description", coffeeDetailUi.description)

            detailFragment.arguments = bundle
            parentFragmentManager.commit {
                replace(R.id.fragment, detailFragment)
                    .addToBackStack(null)
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchData()
    }

    class CustomAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        fun interface ItemClickListener {
            fun onClick(coffeeName: String)
        }

        val coffeeRowItemUiList: MutableList<CoffeeRowItemUi> = mutableListOf()

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
            val textView: TextView
            val imageView: ImageView

            init {
                textView = view.findViewById(R.id.tv_coffee)
                imageView = view.findViewById(R.id.iv_coffee)
                view.setOnClickListener {
                    coffeeRowItemUiList.get(adapterPosition).title?.let { listener.onClick(it) } }
            }
        }

        override fun onCreateViewHolder(
            viewgroup: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val view = LayoutInflater.from(viewgroup.context)
                .inflate(R.layout.coffee_row_item, viewgroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = coffeeRowItemUiList[position].title
            Glide
                .with(viewHolder.imageView.context)
                .load(coffeeRowItemUiList[position].image)
                .circleCrop()
                .into(viewHolder.imageView);
        }

        override fun getItemCount(): Int {
            return coffeeRowItemUiList.size;
        }

        fun updateData(updatedCoffeeRowItemUiList: List<CoffeeRowItemUi>) {
            coffeeRowItemUiList.clear()
            coffeeRowItemUiList.addAll(updatedCoffeeRowItemUiList)
            notifyDataSetChanged()
        }
    }
}