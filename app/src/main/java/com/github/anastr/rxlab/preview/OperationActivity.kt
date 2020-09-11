package com.github.anastr.rxlab.preview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.anastr.rxlab.R
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_operation.*

class OperationActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    val errorHandler: (Throwable) -> Unit
        get() = { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_operation)
        title = intent.getStringExtra("title")

        val operationController: OperationController?
                = intent.getSerializableExtra("OperationController") as OperationController?
        operationController?.activity = this
        operationController?.onCreate()
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_operations, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reload -> {
                recreate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setCode(code: String) {
        codeView.setOptions(
            Options.Default.get(this)
                .withLanguage("java")
                .withCode(code)
//                .disableHighlightAnimation()
                .withTheme(ColorTheme.SOLARIZED_LIGHT))
    }

    fun addNote(note: String) {
        val textView = TextView(this)
        textView.text = note
        addExtraView(textView)
    }

    fun showFab() {
        fab.visibility = View.VISIBLE
    }

    fun setFabClickListener(listener: (View) -> Unit) {
        fab.setOnClickListener(listener)
    }

    fun addExtraView(view: View) {
        extra_layout.addView(view)
    }

    override fun onPause() {
        super.onPause()
        surfaceView.pause()
    }

    override fun onResume() {
        super.onResume()
        surfaceView.resume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        surfaceView.dispose()
    }
}
