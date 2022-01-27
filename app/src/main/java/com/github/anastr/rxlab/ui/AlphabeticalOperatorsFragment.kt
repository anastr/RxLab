package com.github.anastr.rxlab.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.data.allOperations
import com.github.anastr.rxlab.objects.Operation
import kotlinx.android.synthetic.main.content_list.*


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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
//        requireActivity().actionBar?.setDisplayShowHomeEnabled(true)

//        title = intent.getStringExtra("title")

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        allOperations.addAll(
            allOperations().sortedBy { it.operationName.text }
        )
        operations.addAll(allOperations)

        recyclerView.adapter = OperationAdapter(requireContext(), operations)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AlphabeticalOperatorsFragment()
    }
}