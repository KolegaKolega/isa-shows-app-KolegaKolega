package com.example.shows_kolegakolega

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_kolegakolega.databinding.ActivityShowsBinding
import com.example.shows_kolegakolega.model.Show

class ShowsFragment : Fragment() {

    private var _binding: ActivityShowsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: ShowsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initShows()
        viewModel.getShowsLiveData().observe(viewLifecycleOwner, {shows ->
            initRecyclerView(shows)
        })
        initButtonForEmptyState()
        initLogOutButton()
    }

    private fun initLogOutButton() {
        binding.logOutBtn.setOnClickListener {
            with(activity?.getPreferences(Context.MODE_PRIVATE)?.edit()){
                this?.clear()
                this?.commit()
            }
            findNavController().navigate(R.id.shows_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initButtonForEmptyState() {
        binding.showEmpty.setOnClickListener {
            val recyclerVisibility = binding.showsRecycler.isVisible
            binding.showsRecycler.isVisible = binding.camera.isVisible
            binding.camera.isVisible = recyclerVisibility
            binding.textCamera.isVisible = recyclerVisibility
        }
    }

    private fun initRecyclerView(shows: List<Show>) {
       binding.showsRecycler.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = ShowsAdapter(shows){
            val action = ShowsFragmentDirections.showsToDetails(it.id)
            findNavController().navigate(action)
        }

    }
}