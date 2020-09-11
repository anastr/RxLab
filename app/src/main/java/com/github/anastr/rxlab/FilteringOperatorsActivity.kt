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
class FilteringOperatorsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_list)
        title = intent.getStringExtra("title")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val operations = listOf(
            OperationData("contains", operationController = ContainsController()),
            OperationData("filter", operationController = FilterController()),
            OperationData("sorted", operationController = SortedController()),
            OperationData("take", operationController = TakeController()),
            OperationData("takeLast", operationController = TakeLastController()),
            OperationData("skip", operationController = SkipController()),
            OperationData("elementAt", operationController = ElementAtController()),
            OperationData("distinct", operationController = DistinctController()),
            OperationData("distinctUntilChanged", operationController = DistinctUntilChangedController()),
            OperationData("throttleFirst", operationController = ThrottleFirstController()),
            OperationData("throttleLast", operationController = ThrottleLastController()),
            OperationData("throttleWithTimeout", operationController = ThrottleWithTimeoutController())
        )

        recyclerView.adapter = MyAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
