package com.github.anastr.rxlab.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.data.allOperations
import com.github.anastr.rxlab.objects.Operation
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.content_list.*
import java.util.concurrent.TimeUnit


class AlphabeticalOperatorsFragment : Fragment() {

    private val allOperations = ArrayList<Operation>()
    private val operations = ArrayList<Operation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_alphabetical_operators, container, false)



        return view;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView
        Observable.create<String> {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    it.onNext(newText)
                    return false
                }
            })
        }
            .throttleWithTimeout(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ text ->
                operations.clear()
                operations.addAll(allOperations.filter {
                    it.operationName.text.contains(
                        text,
                        true
                    )
                })
                recyclerView.adapter?.notifyDataSetChanged()
            }, {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            })

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        allOperations.addAll(
            allOperations().sortedBy { it.operationName.text }
        )
        operations.addAll(allOperations)

        recyclerView.adapter = OperationAdapter(requireContext(), operations)

        setHasOptionsMenu(true)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AlphabeticalOperatorsFragment()
    }
}