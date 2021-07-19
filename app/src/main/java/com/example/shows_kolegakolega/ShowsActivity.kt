package com.example.shows_kolegakolega

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_kolegakolega.databinding.ActivityShowsBinding
import com.example.shows_kolegakolega.model.Show

class ShowsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowsActivity::class.java)
        }

    }


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

    private lateinit var binding : ActivityShowsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButtonForEmptyState()
        initRecyclerViwe()
    }

    private fun initButtonForEmptyState() {
        binding.showEmpty.setOnClickListener {
            val intent = EmptyStateActivity.buildIntent(this)
            startActivity(intent)
        }
    }

    private fun initRecyclerViwe() {
        binding.showsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = ShowsAdapter(shows){
            val intent = ShowDetailsActivity.buildIntent(this, it)
            startActivity(intent)
        }

    }
}