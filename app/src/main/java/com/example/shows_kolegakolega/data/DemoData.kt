package com.example.shows_kolegakolega.data

import com.example.shows_kolegakolega.R
import com.example.shows_kolegakolega.model.Show

object DemoData {

    val shows = mutableListOf(
        Show("1",
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office " +
                    "employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company.",
            R.drawable.ic_office, mutableListOf()) ,
        Show("2",
            "Stranger Things",
            "In a small town where everyone knows everyone, a peculiar incident starts a chain of events that " +
                    "leads to the disappearance of a child, which begins to tear at the fabric of an otherwise peaceful community. ",
            R.drawable.ic_stranger_things, mutableListOf()),
        Show("3",
            "Krv nije voda",
            "Serija je nadahnuta svakodnevnim životnim pričama koje pogađaju mnoge obitelji, poput nestanka člana obitelji, upadanja u zamku nagomilanih dugova, iznenadnog kraha braka zbog varanja supružnika," +
                    " borbe oko skrbništva nad djecom, ovisnosti o kockanju ili problema s nestašnom djecom. ",
            R.drawable.ic_krv_nije_voda, mutableListOf())
    )


}