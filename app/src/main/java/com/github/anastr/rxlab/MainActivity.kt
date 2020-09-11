package com.github.anastr.rxlab

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.adapter.MyAdapter
import com.github.anastr.rxlab.adapter.OperationData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_list.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val operations = listOf(
            OperationData("Alphabetical Operators", AlphabeticalOperatorsActivity::class.java)
            , OperationData("Schedulers & threads", SchedulersActivity::class.java)
            , OperationData("Creating Operations", CreatingOperatorsActivity::class.java)
            , OperationData("Filtering Operators", FilteringOperatorsActivity::class.java)
            , OperationData("Transforming Operators", TransformingOperatorsActivity::class.java)
            , OperationData("Combining Operators", CombiningOperatorsActivity::class.java)
            , OperationData("Hot Sources", HotSourcesActivity::class.java)
            , OperationData("Comparing", ComparingActivity::class.java)
        )

        recyclerView.adapter = MyAdapter(this, operations)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
