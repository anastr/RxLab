package com.github.anastr.rxlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.adapter.OperationData
import com.github.anastr.rxlab.controllers.FlatMapAndConcatMapController
import com.github.anastr.rxlab.controllers.Scan2Controller
import com.github.anastr.rxlab.data.allOperations
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
                when (it.name) {
                    "map",
                    "sorted",
                    "scan",
                    "reduce",
                    "flatMap",
                    "concatMap",
                    "switchMap",
                    "toList",
                    "buffer" -> true
                    else -> false
                }
            }.toTypedArray(),
            OperationData("scan with initialValue", operationController = Scan2Controller()),
            OperationData(
                "flatMap and concatMap",
                operationController = FlatMapAndConcatMapController()
            ),
        )
            .sortedBy { it.name }

        recyclerView.adapter = OperationAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
