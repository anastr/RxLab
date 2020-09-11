package com.github.anastr.rxlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.controllers.*
import com.github.anastr.rxlab.adapter.MyAdapter
import com.github.anastr.rxlab.adapter.OperationData
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
            OperationData("map", operationController = MapController()),
            OperationData("sorted", operationController = SortedController()),
            OperationData("scan", operationController = ScanController()),
            OperationData("scan with initialValue", operationController = Scan2Controller()),
            OperationData("reduce", operationController = ReduceController()),
            OperationData("flatMap", operationController = FlatMapController()),
            OperationData("concatMap", operationController = ConcatMapController()),
            OperationData("switchMap", operationController = SwitchMapController()),
            OperationData("toList", operationController = ToListController()),
            OperationData("buffer", operationController = BufferController()),
            OperationData("flatMap and concatMap", operationController = FlatMapAndConcatMapActivity())
        )

        recyclerView.adapter = MyAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
