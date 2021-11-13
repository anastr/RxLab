package com.github.anastr.rxlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.data.allOperations
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

        val operations = allOperations().filter {
            when (it.name) {
                "merge",
                "zip 2 observables",
                "zip 3 observables",
                "combineLatest" -> true
                else -> false
            }
        }

        recyclerView.adapter = OperationAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
