package com.github.anastr.rxlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.data.allOperations
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

        val operations = allOperations().filter {
            when (it.name) {
                "just",
                "fromArray",
                "fromIterable",
                "range",
                "interval",
                "timer",
                "flowOf", -> true
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
