package com.github.anastr.rxlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.adapter.MyAdapter
import com.github.anastr.rxlab.adapter.OperationData
import com.github.anastr.rxlab.controllers.*
import kotlinx.android.synthetic.main.content_list.*

class CreatingOperatorsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_list)
        title = intent.getStringExtra("title")
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val operations = listOf(OperationData("just", operationController = JustController()),
            OperationData("fromArray", operationController = FromArrayController()),
            OperationData("fromIterable", operationController = FromIterableController()),
            OperationData("range", operationController = RangeController()),
            OperationData("interval", operationController = IntervalController()),
            OperationData("timer", operationController = TimerController())
        )
        
        recyclerView.adapter = MyAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
