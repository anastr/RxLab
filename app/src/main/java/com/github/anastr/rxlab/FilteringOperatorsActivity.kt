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
            OperationData("contains", ContainsActivity::class.java),
            OperationData("filter", FilterActivity::class.java),
            OperationData("sorted", SortedActivity::class.java),
            OperationData("take", TakeActivity::class.java),
            OperationData("takeLast", TakeLastActivity::class.java),
            OperationData("skip", SkipActivity::class.java),
            OperationData("elementAt", ElementAtActivity::class.java),
            OperationData("distinct", DistinctActivity::class.java),
            OperationData("distinctUntilChanged", DistinctUntilChangedActivity::class.java),
            OperationData("throttleFirst", ThrottleFirstActivity::class.java),
            OperationData("throttleLast", ThrottleLastActivity::class.java),
            OperationData("throttleWithTimeout", ThrottleWithTimeoutActivity::class.java)
        )

        recyclerView.adapter = MyAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
