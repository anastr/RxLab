package com.github.anastr.rxlab

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.controllers.*
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
                OperationData("buffer", operationController = BufferController()),
                OperationData("combineLatest", operationController = CombineLatestController()),
                OperationData("concatMap", operationController = ConcatMapController()),
                OperationData("contains", operationController = ContainsController()),
                OperationData("create", operationController = CreateController()),
                OperationData("distinct", operationController = DistinctController()),
                OperationData("distinctUntilChanged", operationController = DistinctUntilChangedController()),
                OperationData("elementAt", operationController = ElementAtController()),
                OperationData("filter", operationController = FilterController()),
                OperationData("flatMap", operationController = FlatMapController()),
                OperationData("fromArray", operationController = FromArrayController()),
                OperationData("fromIterable", operationController = FromIterableController()),
                OperationData("interval", operationController = IntervalController()),
                OperationData("just", operationController = JustController()),
                OperationData("map", operationController = MapController()),
                OperationData("merge", operationController = MergeController()),
                OperationData("range", operationController = RangeController()),
                OperationData("reduce", operationController = ReduceController()),
                OperationData("scan", operationController = ScanController()),
                OperationData("scan2", operationController = Scan2Controller()),
                OperationData("skip", operationController = SkipController()),
                OperationData("sorted", operationController = SortedController()),
                OperationData("switchMap", operationController = SwitchMapController()),
                OperationData("take", operationController = TakeController()),
                OperationData("takeLast", operationController = TakeLastController()),
                OperationData("throttleFirst", operationController = ThrottleFirstController()),
                OperationData("throttleLast", operationController = ThrottleLastController()),
                OperationData("throttleWithTimeout", operationController = ThrottleWithTimeoutController()),
                OperationData("toList", operationController = ToListController()),
                OperationData("zip2", operationController = Zip2Controller()),
                OperationData("zip3", operationController = Zip3Controller())
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
