package net.zackzhang.lib.basedialogfragment

import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.dialog_fragment_base.*

abstract class BaseDialogFragment : DialogFragment() {

    companion object {

        private const val ARG_TITLE = "title"
        private const val ARG_NEUTRAL_BUTTON_TEXT = "neutral_button_text"
        private const val ARG_NEGATIVE_BUTTON_TEXT = "negative_button_text"
        private const val ARG_POSITIVE_BUTTON_TEXT = "positive_button_text"
        private const val ARG_ADD_HORIZONTAL_MARGINS = "add_horizontal_margins"

        // 当前不支持静态 protected 方法在子类中调用
        fun putTitle(args: Bundle, title: CharSequence) {
            args.putCharSequence(ARG_TITLE, title)
        }

        fun putNeutralButtonText(args: Bundle, neutralButtonText: CharSequence) {
            args.putCharSequence(ARG_NEUTRAL_BUTTON_TEXT, neutralButtonText)
        }

        fun putNegativeButtonText(args: Bundle, negativeButtonText: CharSequence) {
            args.putCharSequence(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
        }

        fun putPositiveButtonText(args: Bundle, positiveButtonText: CharSequence) {
            args.putCharSequence(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
        }

        fun putAddHorizontalMargins(args: Bundle, addHorizontalMargins: Boolean) {
            args.putBoolean(ARG_ADD_HORIZONTAL_MARGINS, addHorizontalMargins)
        }
    }

    /** 指示 view 属性是否已初始化 */
    private var initialized = false

    // 类初始化的时候并不会调用 getter
    var titleText: CharSequence? = null
        set(value) {
            // 更新幕后字段
            if (value == field) return
            field = value
            // 若 view 已初始化，则刷新 view
            if (initialized) {
                updateTitle()
                // 为防止在 onCreate 中 get 后又 put
                arguments!!.putCharSequence(ARG_TITLE, value)
            }
        }
    var neutralButtonText: CharSequence? = null
        set(value) {
            if (value == field) return
            field = value
            if (initialized) {
                updateNeutralButtonText()
                arguments!!.putCharSequence(ARG_NEUTRAL_BUTTON_TEXT, value)
            }
        }
    var neutralButtonClickListener: (() -> Boolean)? = null
    var negativeButtonText: CharSequence? = null
        set(value) {
            if (value == field) return
            field = value
            if (initialized) {
                updateNegativeButtonText()
                arguments!!.putCharSequence(ARG_NEGATIVE_BUTTON_TEXT, value)
            }
        }
    var negativeButtonClickListener: (() -> Boolean)? = null
    var positiveButtonText: CharSequence? = null
        set(value) {
            if (value == field) return
            field = value
            if (initialized) {
                updatePositiveButtonText()
                arguments!!.putCharSequence(ARG_POSITIVE_BUTTON_TEXT, value)
            }
        }
    var positiveButtonClickListener: (() -> Boolean)? = null
    var cancelListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 在这里初始化所有 view 属性，因为可以保证 arguments 不为空
        titleText = arguments!!.getCharSequence(ARG_TITLE)
        neutralButtonText = arguments!!.getCharSequence(ARG_NEUTRAL_BUTTON_TEXT)
        negativeButtonText = arguments!!.getCharSequence(ARG_NEGATIVE_BUTTON_TEXT)
        positiveButtonText = arguments!!.getCharSequence(ARG_POSITIVE_BUTTON_TEXT)

        // view 属性已初始化
        initialized = true
    }

    /** 重写这个方法提供内容区域的view */
    abstract fun onCreateContentView(inflater: LayoutInflater, root: ViewGroup): View

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.dialog_fragment_base, container, false) as ViewGroup
        val content = onCreateContentView(inflater, root)
        val contentLayoutParams = (content.layoutParams as LinearLayout.LayoutParams)
        // 使用 title 的 marginStart 来作为 content 的水平 margin
        if (arguments!!.getBoolean(ARG_ADD_HORIZONTAL_MARGINS, true)) {
            val horizontalMargin = (root.getChildAt(0).layoutParams as LinearLayout.LayoutParams).marginStart
            contentLayoutParams.marginStart = horizontalMargin
            contentLayoutParams.marginEnd = horizontalMargin
        }
        // 使按钮区域不会被内容区域挤出去
        contentLayoutParams.height = 0
        contentLayoutParams.weight = 1f
        root.addView(content, 1)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 通过动态设置内容区域的view宽度来设置dialog宽度（不能直接设置根view的宽度，因为它的LayoutParams为null）
        val point = Point()
        (context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)
        (view as ViewGroup).getChildAt(1).layoutParams.width = (point.x * 0.8f).toInt()

        updateTitle()
        updateNeutralButtonText()
        updateNegativeButtonText()
        updatePositiveButtonText()

        // 按键事件不需要更新，只要相应的listener属性更新就行了
        vNeutralButton.setOnClickListener {
            if (neutralButtonClickListener?.invoke() != false) {
                dismiss()
            }
        }
        vNegativeButton.setOnClickListener {
            if (negativeButtonClickListener?.invoke() != false) {
                dismiss()
            }
        }
        vPositiveButton.setOnClickListener {
            if (positiveButtonClickListener?.invoke() != false) {
                dismiss()
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        cancelListener?.invoke()
    }

    private fun updateTitle() {
        vTitleText.text = titleText
        updateVisibility(vTitleText, titleText != null)
    }

    private fun updateNeutralButtonText() {
        vNeutralButton.text = neutralButtonText
        updateVisibility(vNeutralButton, neutralButtonText != null)
    }

    private fun updateNegativeButtonText() {
        vNegativeButton.text = negativeButtonText
        updateVisibility(vNegativeButton, negativeButtonText != null)
    }

    private fun updatePositiveButtonText() {
        vPositiveButton.text = positiveButtonText
        updateVisibility(vPositiveButton, positiveButtonText != null)
    }

    private fun updateVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }
}
