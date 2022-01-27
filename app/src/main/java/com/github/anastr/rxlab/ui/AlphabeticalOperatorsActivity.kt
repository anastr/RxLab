package com.github.anastr.rxlab.ui

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.data.allOperations
import com.github.anastr.rxlab.objects.Operation
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.content_list.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/11/2020.
 */
class AlphabeticalOperatorsActivity: AppCompatActivity() {

    private val allOperations = ArrayList<Operation>()
    private val operations = ArrayList<Operation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_list)
        title = intent.getStringExtra("title")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        allOperations.addAll(
            allOperations().sortedBy { it.operationName.text }
        )
        operations.addAll(allOperations)

        recyclerView.adapter = OperationAdapter(this, operations)
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
                operations.addAll(allOperations.filter { it.operationName.text.contains(text, true) })
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
