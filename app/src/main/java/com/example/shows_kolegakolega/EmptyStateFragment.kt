package com.example.shows_kolegakolega

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shows_kolegakolega.databinding.ActivityEmptyStateBinding
import com.example.shows_kolegakolega.databinding.ActivityShowsBinding

class EmptyStateFragment : Fragment() {

    private var _binding: ActivityEmptyStateBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityEmptyStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hideEmpty.setOnClickListener {
            findNavController().navigate(R.id.empty_state_to_shows)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}