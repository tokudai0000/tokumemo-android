package com.example.tokumemo.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.tokumemo.R

class PRFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_pr, container, false)

        val backButton = view.findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            // 子フラグメントマネージャーを取得
            val childFragmentManager = parentFragment?.childFragmentManager

            // 子フラグメントを終了する
            if (childFragmentManager != null) {
                val childFragment = childFragmentManager.findFragmentById(R.id.child_fragment_container)
                if (childFragment != null) {
                    childFragmentManager.beginTransaction()
                        .remove(childFragment)
                        .commit()
                }
            }
        }


        return view

    }
}
