package com.github.anastr.rxlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.activities.*
import com.github.anastr.rxlab.adapter.MyAdapter
import com.github.anastr.rxlab.adapter.OperationData
import kotlinx.android.synthetic.main.content_list.*

/**
 * Created by Anas Altair on 4/11/2020.
 */
class AlphabeticalOperatorsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_list)
        title = intent.getStringExtra("title")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val operations = listOf(
            OperationData("concatMap", ConcatMapActivity::class.java),
            OperationData("create", CreateActivity::class.java),
            OperationData("distinct", DistinctActivity::class.java),
            OperationData("distinctUntilChanged", DistinctUntilChangedActivity::class.java),
            OperationData("elementAt", ElementAtActivity::class.java),
            OperationData("filter", FilterActivity::class.java),
            OperationData("flatMap", FlatMapActivity::class.java),
            OperationData("fromArray", FromArrayActivity::class.java),
            OperationData("fromIterable", FromIterableActivity::class.java),
            OperationData("interval", IntervalActivity::class.java),
            OperationData("just", JustActivity::class.java),
            OperationData("map", MapActivity::class.java),
            OperationData("merge", MergeActivity::class.java),
            OperationData("range", RangeActivity::class.java),
            OperationData("reduce", ReduceActivity::class.java),
            OperationData("scan", ScanActivity::class.java),
            OperationData("scan2", Scan2Activity::class.java),
            OperationData("skip", SkipActivity::class.java),
            OperationData("sorted", SortedActivity::class.java),
            OperationData("switchMap", SwitchMapActivity::class.java),
            OperationData("take", TakeActivity::class.java),
            OperationData("takeLast", TakeLastActivity::class.java),
            OperationData("throttleFirst", ThrottleFirstActivity::class.java),
            OperationData("throttleLast", ThrottleLastActivity::class.java),
            OperationData("throttleWithTimeout", ThrottleWithTimeoutActivity::class.java),
            OperationData("zip2", Zip2Activity::class.java),
            OperationData("zip3", Zip3Activity::class.java)
        )

        recyclerView.adapter = MyAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
