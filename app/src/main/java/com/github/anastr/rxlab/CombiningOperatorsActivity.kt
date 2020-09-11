package com.github.anastr.rxlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.controllers.CombineLatestController
import com.github.anastr.rxlab.controllers.MergeController
import com.github.anastr.rxlab.controllers.Zip2Controller
import com.github.anastr.rxlab.controllers.Zip3Controller
import com.github.anastr.rxlab.adapter.MyAdapter
import com.github.anastr.rxlab.adapter.OperationData
import kotlinx.android.synthetic.main.content_list.*

/**
 * Created by Anas Altair on 4/16/2020.
 */
class CombiningOperatorsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_list)
        title = intent.getStringExtra("title")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val operations = listOf(
            OperationData("merge", operationController = MergeController()),
            OperationData("zip 2 observables", operationController = Zip2Controller()),
            OperationData("zip 3 observables", operationController = Zip3Controller()),
            OperationData("combineLatest", operationController = CombineLatestController())
        )

        recyclerView.adapter = MyAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
