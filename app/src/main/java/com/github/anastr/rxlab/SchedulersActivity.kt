package com.github.anastr.rxlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.controllers.schedulers.*
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.adapter.OperationData
import kotlinx.android.synthetic.main.content_list.*

/**
 * Created by Anas Altair on 7/8/2020.
 */
class SchedulersActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_list)
        title = intent.getStringExtra("title")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val operations = listOf(
            OperationData("mainThread", operationController = MainSchedulerController()),
            OperationData("computation", operationController = ComputationSchedulerController()),
            OperationData("io", operationController = IoSchedulerController()),
            OperationData("single", operationController = SingleSchedulerController()),
            OperationData("other", operationController = OtherSchedulerController())
        )

        recyclerView.adapter = OperationAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}