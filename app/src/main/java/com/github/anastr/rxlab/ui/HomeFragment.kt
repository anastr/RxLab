package com.github.anastr.rxlab.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.adapter.CardAdapter
import com.github.anastr.rxlab.adapter.CardData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.content_list.*
import java.util.concurrent.TimeUnit


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
            CardData(1, "Alphabetical Operators"),
            CardData(2, "Schedulers & threads"),
            CardData(3, "Creating Operations"),
            CardData(4, "Filtering Operators"),
            CardData(5, "Transforming Operators"),
            CardData(6, "Combining Operators"),
            CardData(7, "Hot Sources"),
            CardData(8, "Comparing"),
        )

        recyclerView.adapter = CardAdapter(requireContext(), operations, this)


        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_about -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAboutFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
            2 -> {
                HomeFragmentDirections.actionHomeFragmentToSchedulersFragment()
            }
            3 -> {
                HomeFragmentDirections.actionHomeFragmentToCreatingOperatorsFragment()
            }
            4 -> {
                HomeFragmentDirections.actionHomeFragmentToFilteringOperatorsFragment()
            }
            5 -> {
                HomeFragmentDirections.actionHomeFragmentToTransformingOperatorsFragment()
            }
            6 -> {
                HomeFragmentDirections.actionHomeFragmentToCombiningOperatorsFragment()
            }
            7 -> {
                HomeFragmentDirections.actionHomeFragmentToHotSourcesFragment()
            }
            8 -> {
                HomeFragmentDirections.actionHomeFragmentToComparingFragment()
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