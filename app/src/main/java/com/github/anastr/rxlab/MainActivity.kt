package com.github.anastr.rxlab

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.adapter.CardAdapter
import com.github.anastr.rxlab.adapter.CardData
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
            CardData("Alphabetical Operators", AlphabeticalOperatorsActivity::class.java),
            CardData("Schedulers & threads", SchedulersActivity::class.java),
            CardData("Creating Operations", CreatingOperatorsActivity::class.java),
            CardData("Filtering Operators", FilteringOperatorsActivity::class.java),
            CardData("Transforming Operators", TransformingOperatorsActivity::class.java),
            CardData("Combining Operators", CombiningOperatorsActivity::class.java),
            CardData("Hot Sources", HotSourcesActivity::class.java),
            CardData("Comparing", ComparingActivity::class.java),
        )

        recyclerView.adapter = CardAdapter(this, operations)
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
