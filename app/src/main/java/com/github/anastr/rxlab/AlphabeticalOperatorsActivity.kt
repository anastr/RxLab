package com.github.anastr.rxlab

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.activities.*
import com.github.anastr.rxlab.adapter.MyAdapter
import com.github.anastr.rxlab.adapter.OperationData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.content_list.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/11/2020.
 */
class AlphabeticalOperatorsActivity: AppCompatActivity() {

    private val allOperations = ArrayList<OperationData>()
    private val operations = ArrayList<OperationData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_list)
        title = intent.getStringExtra("title")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        allOperations.addAll(
            listOf(
                OperationData("concatMap", ConcatMapActivity::class.java),
                OperationData("contains", ContainsActivity::class.java),
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
        )
        operations.addAll(allOperations)

        recyclerView.adapter = MyAdapter(this, operations)
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView
        Observable.create<String> {
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    it.onNext(newText)
                    return false
                }
            })
        }
            .throttleWithTimeout(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ text ->
                operations.clear()
                operations.addAll(allOperations.filter { it.name.contains(text, true) })
                recyclerView.adapter?.notifyDataSetChanged()
            }, {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
