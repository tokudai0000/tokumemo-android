package com.tokudai0000.tokumemo.common
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.tokudai0000.tokumemo.R

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
        // ボタン押下時のデフォルトの影を削除
        stateListAnimator = null
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
        translationY = 5f // ボタンを2f下に移動
    }

    private fun buttonReleased() {
        translationY = 0f // ボタンを元の位置に戻す
    }

    fun setupButton(
        title: String,
        textColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE,
        borderColor: Int = ContextCompat.getColor(context, R.color.custom_blue),
        fontSize: Float = 19f,
        tag: Int = 0,
        verticalMargin: Float = 18f,
        horizontalMargin: Float = 26f
    ) {
        setText(title)
        setTextColor(textColor)
        setTypeface(typeface, Typeface.BOLD)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

        // パディングを設定するためのdpからpxへの変換
        val verticalPaddingPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, verticalMargin, resources.displayMetrics).toInt()
        val horizontalPaddingPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, horizontalMargin, resources.displayMetrics).toInt()

        // パディングを設定
        setPadding(horizontalPaddingPx, verticalPaddingPx, horizontalPaddingPx, verticalPaddingPx)


        // ボタン本体
        val buttonDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(backgroundColor)
            cornerRadius = 30f
            setStroke(5, borderColor)
        }

        // "影"を模倣するためのDrawable
        val shadowDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(borderColor)
            cornerRadius = 35f
        }

        // LayerDrawableを使用して、影とボタンを重ねる
        val layers = LayerDrawable(arrayOf(shadowDrawable, buttonDrawable)).apply {
            setLayerInset(1, 0, 0, 0, 5)
        }

        background = layers


    }
}