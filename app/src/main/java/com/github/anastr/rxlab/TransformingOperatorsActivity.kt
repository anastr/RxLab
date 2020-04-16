package com.github.anastr.rxlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.activities.*
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
            OperationData("map", MapActivity::class.java),
            OperationData("sorted", SortedActivity::class.java),
            OperationData("scan", ScanActivity::class.java),
            OperationData("scan with initialValue", Scan2Activity::class.java),
            OperationData("reduce", ReduceActivity::class.java),
            OperationData("flatMap", FlatMapActivity::class.java),
            OperationData("concatMap", ConcatMapActivity::class.java),
            OperationData("switchMap", SwitchMapActivity::class.java),
            OperationData("flatMap and concatMap", FlatMapAndConcatMapActivity::class.java)
        )

        recyclerView.adapter = MyAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
