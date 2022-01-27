package com.github.anastr.rxlab.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.controllers.OperationName
import com.github.anastr.rxlab.controllers.rxjava.FlatMapAndConcatMapController
import com.github.anastr.rxlab.data.allOperations
import com.github.anastr.rxlab.objects.Operation
import kotlinx.android.synthetic.main.content_list.*

/**
 * Created by Anas Altair on 4/8/2020.
 */
class TransformingOperatorsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_list)
        title = intent.getStringExtra("title")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val operations = listOf(
            *allOperations().filter {
                when (it.operationName) {
                    OperationName.map,
                    OperationName.sorted,
                    OperationName.scan,
                    OperationName.scan_2,
                    OperationName.reduce,
                    OperationName.flatMap,
                    OperationName.concatMap,
                    OperationName.switchMap,
                    OperationName.toList,
                    OperationName.buffer -> true
                    else -> false
                }
            }.toTypedArray(),
            Operation(
                OperationName.compare_flatMap_concatMap,
                controller = FlatMapAndConcatMapController()
            ),
        )
            .sortedBy { it.operationName.text }

        recyclerView.adapter = OperationAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
