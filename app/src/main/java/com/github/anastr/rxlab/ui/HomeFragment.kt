package com.github.anastr.rxlab.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.adapter.CardAdapter
import com.github.anastr.rxlab.adapter.CardData
import kotlinx.android.synthetic.main.content_list.*


class HomeFragment : Fragment(), ClickListener {

    private val navController: NavController
        get() = findNavController()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        val operations = listOf(
            CardData(1, "Alphabetical Operators", AlphabeticalOperatorsActivity::class.java),
            CardData(2, "Schedulers & threads", SchedulersActivity::class.java),
            CardData(3, "Creating Operations", CreatingOperatorsActivity::class.java),
            CardData(4, "Filtering Operators", FilteringOperatorsActivity::class.java),
            CardData(5, "Transforming Operators", TransformingOperatorsActivity::class.java),
            CardData(6, "Combining Operators", CombiningOperatorsActivity::class.java),
            CardData(7, "Hot Sources", HotSourcesActivity::class.java),
            CardData(8, "Comparing", ComparingActivity::class.java),
        )

        recyclerView.adapter = CardAdapter(requireContext(), operations, this)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }

    override fun onCardClick(data: CardData) {
        val action: NavDirections? = when (data.id) {
            1 -> {
                HomeFragmentDirections.actionHomeFragmentToAlphabeticalOperatorsFragment()
            }

            else -> null
        }
        action?.let {
            navController.navigate(action.actionId)
        }
    }


}

interface ClickListener {
    fun onCardClick(data: CardData)
}