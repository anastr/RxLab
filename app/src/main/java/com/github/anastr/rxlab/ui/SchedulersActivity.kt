package com.github.anastr.rxlab.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.controllers.OperationName
import com.github.anastr.rxlab.controllers.rxjava.schedulers.*
import com.github.anastr.rxlab.objects.Operation
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
            Operation(OperationName.mainThread, controller = MainSchedulerController()),
            Operation(OperationName.computation, controller = ComputationSchedulerController()),
            Operation(OperationName.io, controller = IoSchedulerController()),
            Operation(OperationName.single, controller = SingleSchedulerController()),
            Operation(OperationName.other, controller = OtherSchedulerController())
        )

        recyclerView.adapter = OperationAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}