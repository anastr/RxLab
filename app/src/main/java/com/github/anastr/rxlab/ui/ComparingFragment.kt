package com.github.anastr.rxlab.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.controllers.OperationName
import com.github.anastr.rxlab.controllers.rxjava.FlatMapAndConcatMapController
import com.github.anastr.rxlab.objects.Operation
import kotlinx.android.synthetic.main.content_list.*


class ComparingFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comparing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        val operations = listOf(
            Operation(
                OperationName.compare_flatMap_concatMap,
                controller = FlatMapAndConcatMapController()
            )
        )

        recyclerView.adapter = OperationAdapter(requireContext(), operations)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ComparingFragment()
    }
}