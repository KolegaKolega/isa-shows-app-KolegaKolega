package com.example.shows_kolegakolega

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_kolegakolega.databinding.ActivityShowsBinding
import com.example.shows_kolegakolega.model.Show

class ShowsFragment : Fragment() {

    private val shows = listOf(
        Show("1",
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office " +
                    "employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company.",
        R.drawable.ic_office) ,
        Show("2",
        "Stranger Things",
        "In a small town where everyone knows everyone, a peculiar incident starts a chain of events that " +
                "leads to the disappearance of a child, which begins to tear at the fabric of an otherwise peaceful community. ",
        R.drawable.ic_stranger_things),
        Show("3",
            "Krv nije voda",
            "Serija je nadahnuta svakodnevnim životnim pričama koje pogađaju mnoge obitelji, poput nestanka člana obitelji, upadanja u zamku nagomilanih dugova, iznenadnog kraha braka zbog varanja supružnika," +
                    " borbe oko skrbništva nad djecom, ovisnosti o kockanju ili problema s nestašnom djecom. ",
            R.drawable.ic_krv_nije_voda)
    )

    private var _binding: ActivityShowsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        initButtonForEmptyState()
        initRecyclerView()
        initLogOutButton()
    }

    private fun initLogOutButton() {
        binding.logOutButton.setOnClickListener {
            findNavController().navigate(R.id.shows_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initButtonForEmptyState() {
        binding.showEmpty.setOnClickListener {
            findNavController().navigate(R.id.shows_to_empty_state)
        }
    }

    private fun initRecyclerView() {
       binding.showsRecycler.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = ShowsAdapter(shows){
            val action = ShowsFragmentDirections.showsToDetails(
                it.name,
                it.description,
                it.image
            )
            findNavController().navigate(action)
        }

    }
}