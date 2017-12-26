package me.imzack.lib.basedialogfragment

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.dialog_fragment_base.*
import java.io.Serializable

abstract class BaseDialogFragment : DialogFragment() {

    companion object {

        private val ARG_TITLE = "title"
        private val ARG_NEU_BTN_TEXT = "neu_btn_text"
        private val ARG_NEU_BTN_CLICK_LISTENER = "neu_btn_click_listener"
        private val ARG_NEG_BTN_TEXT = "neg_btn_text"
        private val ARG_NEG_BTN_CLICK_LISTENER = "neg_btn_click_listener"
        private val ARG_POS_BTN_TEXT = "pos_btn_text"
        private val ARG_POS_BTN_CLICK_LISTENER = "pos_btn_click_listener"
        private val ARG_ADD_HORIZONTAL_MARGINS = "add_horizontal_margins"

        // 当前不支持静态protected方法在子类中调用
        fun putBaseArguments(
                arguments: Bundle,
                titleText: CharSequence? = null,
                neutralButtonText: CharSequence? = null,
                neutralButtonClickListener: OnButtonClickListener? = null,
                negativeButtonText: CharSequence? = null,
                negativeButtonClickListener: OnButtonClickListener? = null,
                positiveButtonText: CharSequence? = null,
                positiveButtonClickListener: OnButtonClickListener? = null,
                addHorizontalMargins: Boolean = true
        ) {
            arguments.putCharSequence(ARG_TITLE, titleText)
            arguments.putCharSequence(ARG_NEU_BTN_TEXT, neutralButtonText)
            arguments.putSerializable(ARG_NEU_BTN_CLICK_LISTENER, neutralButtonClickListener)
            arguments.putCharSequence(ARG_NEG_BTN_TEXT, negativeButtonText)
            arguments.putSerializable(ARG_NEG_BTN_CLICK_LISTENER, negativeButtonClickListener)
            arguments.putCharSequence(ARG_POS_BTN_TEXT, positiveButtonText)
            arguments.putSerializable(ARG_POS_BTN_CLICK_LISTENER, positiveButtonClickListener)
            arguments.putBoolean(ARG_ADD_HORIZONTAL_MARGINS, addHorizontalMargins)
        }
    }

    /** 指示 view 是否已初始化 */
    protected var initialized = false

    // 类初始化的时候并不会调用 getter
    // 运行时对属性的更新不会保持，若要保持，需要自行将值存储到 arguments 中
    var titleText: CharSequence? = null
        set(value) {
            // 更新幕后字段
            if (value == field) return
            field = value
            // 若 view 已初始化，则刷新 view
            if (initialized) {
                updateTitle()
            }
        }
    var neutralButtonText: CharSequence? = null
        set(value) {
            if (value == field) return
            field = value
            if (initialized) {
                updateNeutralButtonText()
            }
        }
    var neutralButtonClickListener: OnButtonClickListener? = null
    var negativeButtonText: CharSequence? = null
        set(value) {
            if (value == field) return
            field = value
            if (initialized) {
                updateNegativeButtonText()
            }
        }
    var negativeButtonClickListener: OnButtonClickListener? = null
    var positiveButtonText: CharSequence? = null
        set(value) {
            if (value == field) return
            field = value
            if (initialized) {
                updatePositiveButtonText()
            }
        }
    var positiveButtonClickListener: OnButtonClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 在这里初始化所有 view 属性，因为可以保证 arguments 不为空
        titleText = arguments.getCharSequence(ARG_TITLE)
        negativeButtonText = arguments.getCharSequence(ARG_NEU_BTN_TEXT)
        neutralButtonClickListener = arguments.getSerializable(ARG_NEU_BTN_CLICK_LISTENER) as OnButtonClickListener?
        negativeButtonText = arguments.getCharSequence(ARG_NEG_BTN_TEXT)
        negativeButtonClickListener = arguments.getSerializable(ARG_NEG_BTN_CLICK_LISTENER) as OnButtonClickListener?
        positiveButtonText = arguments.getCharSequence(ARG_POS_BTN_TEXT)
        positiveButtonClickListener = arguments.getSerializable(ARG_POS_BTN_CLICK_LISTENER) as OnButtonClickListener?
    }

    /** 重写这个方法提供内容区域的view */
    abstract fun onCreateContentView(inflater: LayoutInflater, root: ViewGroup): View

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.dialog_fragment_base, container, false) as ViewGroup
        val content = onCreateContentView(inflater, root)
        val contentLayoutParams = (content.layoutParams as LinearLayout.LayoutParams)
        // 使用 title 的 marginStart 来作为 content 的水平 margin
        if (arguments.getBoolean(ARG_ADD_HORIZONTAL_MARGINS)) {
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

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 通过动态设置内容区域的view宽度来设置dialog宽度（不能直接设置根view的宽度，因为它的LayoutParams为null）
        val point = Point()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)
        (view as ViewGroup).getChildAt(1).layoutParams.width = (point.x * 0.8f).toInt()

        initialized = true

        updateTitle()
        updateNeutralButtonText()
        updateNegativeButtonText()
        updatePositiveButtonText()

        // 按键事件不需要更新，只要相应的listener属性更新就行了
        vNeutralButton.setOnClickListener {
            if (neutralButtonClickListener?.onClick() != false) {
                dismiss()
            }
        }
        vNegativeButton.setOnClickListener {
            if (negativeButtonClickListener?.onClick() != false) {
                dismiss()
            }
        }
        vPositiveButton.setOnClickListener {
            if (positiveButtonClickListener?.onClick() != false) {
                dismiss()
            }
        }
    }

    private fun updateTitle() {
        if (titleText == null) {
            vTitleText.visibility = View.GONE
        } else {
            vTitleText.text = titleText
        }
    }

    private fun updateNeutralButtonText() {
        if (neutralButtonText == null) {
            vNeutralButton.visibility = View.GONE
        } else {
            vNeutralButton.text = neutralButtonText
        }
    }

    private fun updateNegativeButtonText() {
        if (negativeButtonText == null) {
            vNegativeButton.visibility = View.GONE
        } else {
            vNegativeButton.text = negativeButtonText
        }
    }

    private fun updatePositiveButtonText() {
        if (positiveButtonText == null) {
            vPositiveButton.visibility = View.GONE
        } else {
            vPositiveButton.text = positiveButtonText
        }
    }

    fun show(manager: FragmentManager) {
        super.show(manager, null)
    }

    // 继承了Serializable，没法写成函数类型
    interface OnButtonClickListener : Serializable {
        /** 按钮按下时调用，返回值表示是否关闭dialog */
        fun onClick(): Boolean
    }
}
