package com.github.anastr.rxlab.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.adapter.OperationAdapter
import com.github.anastr.rxlab.controllers.OperationName
import com.github.anastr.rxlab.controllers.rxjava.CreateController
import com.github.anastr.rxlab.objects.Operation
import kotlinx.android.synthetic.main.content_list.*

/**
 * Created by Anas Altair on 4/10/2020.
 */
class HotSourcesActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_list)
        title = intent.getStringExtra("title")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val operations = listOf(
            Operation(OperationName.hot_create, controller = CreateController())
        )

        recyclerView.adapter = OperationAdapter(this, operations)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
