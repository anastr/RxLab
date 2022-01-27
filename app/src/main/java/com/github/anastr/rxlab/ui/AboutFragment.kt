package com.github.anastr.rxlab.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.anastr.rxlab.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_github_profile.setOnClickListener { openUrl("https://github.com/anastr") }
        button_linked_in.setOnClickListener { openUrl("https://linkedin.com/in/anas-altair") }
        button_github_project.setOnClickListener { openUrl("https://github.com/anastr/RxLab") }
        button_flat_icon.setOnClickListener { openUrl("https://www.flaticon.com/authors/freepik") }

    }
    private fun openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW).apply { this.data = Uri.parse(url) })

    companion object {
        @JvmStatic
        fun newInstance() =
            AboutFragment()
    }
}