package com.example.tokumemo.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tokumemo.GetImage
import com.example.tokumemo.R

class PRFragment : Fragment(), View.OnTouchListener {

    lateinit var imageStr: HomeViewModel.PublicRelations

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

        val imageView = view.findViewById<ImageView>(R.id.pr_image_view)
        val imageTask: GetImage = GetImage(imageView)
        imageTask.execute(imageStr.imageURL)

        var text = view.findViewById<TextView>(R.id.textView5)
        text.text = imageStr.introduction


        return view

    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return true // 下のフラグメントのタップを無効化するために true を返す
    }
}
