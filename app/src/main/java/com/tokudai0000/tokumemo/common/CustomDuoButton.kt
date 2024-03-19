package com.tokudai0000.tokumemo.common
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class CustomDuoButton : AppCompatButton {

    var onTap: ((Int) -> Unit)? = null
    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }
    private fun initialize() {
        setTextColor(Color.BLACK)
        text = "Button"
    }

    // タッチイベントのハンドリング
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> buttonPressed()
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                buttonReleased()
                onTap?.invoke(tag as? Int ?: 0) // タグが設定されていればそれを使い、なければ0を渡す
            }
        }
        return super.onTouchEvent(event)
    }

    private fun buttonPressed() {
        // ボタンが押された時の処理。例えば、影の不透明度を下げるなど
        alpha = 0.5f // 例
    }

    private fun buttonReleased() {
        // ボタンが離された時の処理。例えば、影の不透明度を元に戻すなど
        alpha = 1.0f // 例
    }

    fun setupButton(
        title: String,
        textColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE,
        borderColor: Int = ContextCompat.getColor(context, android.R.color.holo_blue_light),
        fontSize: Float = 18f,
        tag: Int = 0,
        verticalMargin: Float = 25f,
        horizontalMargin: Float = 50f
    ) {
        setText(title)
        setTextColor(textColor)
        setBackgroundColor(backgroundColor)
        // フォントサイズ、ボーダー、マージンなどの設定
    }
}