package com.github.anastr.rxlab.preview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.util.dpToPx
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_operation.*
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min


/**
 * Created by Anas Altair.
 */
class OperationActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    val errorHandler: (Throwable) -> Unit
        get() = { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }

    private var code = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_operation)
        title = intent.getStringExtra("title")

        surfaceView.onError = {
            surfaceView.dispose()
            MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("SurfaceView Error")
                .setMessage(it.message ?: "SurfaceView throw unknown error!")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                .show()
        }


        lifecycleScope.launch {
            val operationController: OperationController?
                    = intent.getSerializableExtra("OperationController") as OperationController?
            operationController?.onCreate(this@OperationActivity)
        }
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
            R.id.action_copy -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("RxLab code", code)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "Code copied", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setCode(code: String) {
        this.code = code
        // get screen height
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels

        // dynamic codeView height, between (80dp, screenHeight/3)
        val params = codeView.layoutParams
        params.height = min(
            max(dpToPx(20f * code.lines().size), dpToPx(80f)), screenHeight / 3f
        ).toInt()
        codeView.layoutParams = params

        codeView.setOptions(
            Options.Default.get(this)
                .withLanguage("java")
                .withCode(code)
//                .disableHighlightAnimation()
                .withTheme(ColorTheme.SOLARIZED_LIGHT))
        codeView.setOnLongClickListener {
            false
        }
    }

    fun addNote(note: String) {
        val textView = TextView(this)
        textView.text = note
        addExtraView(textView)
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
