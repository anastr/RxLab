package com.github.anastr.rxlab

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
            , OperationData("Creating Operations", CreatingOperatorsActivity::class.java)
            , OperationData("Filtering Operators", FilteringOperatorsActivity::class.java)
            , OperationData("Transforming Operators", TransformingOperatorsActivity::class.java)
            , OperationData("Combining Operators", CombiningOperatorsActivity::class.java)
            , OperationData("Hot Sources", HotSourcesActivity::class.java)
            , OperationData("Comparing", ComparingActivity::class.java)
        )

        recyclerView.adapter = MyAdapter(this, operations)

        fab.setOnClickListener {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
