package me.imzack.lib.basedialogfragment

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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

        // 当前不支持静态protected方法在子类中调用
        fun putBaseArguments(
                arguments: Bundle,
                titleText: CharSequence? = null,
                neutralButtonText: CharSequence? = null,
                neutralButtonClickListener: OnButtonClickListener? = null,
                negativeButtonText: CharSequence? = null,
                negativeButtonClickListener: OnButtonClickListener? = null,
                positiveButtonText: CharSequence? = null,
                positiveButtonClickListener: OnButtonClickListener? = null
        ) {
            arguments.putCharSequence(ARG_TITLE, titleText)
            arguments.putCharSequence(ARG_NEU_BTN_TEXT, neutralButtonText)
            arguments.putSerializable(ARG_NEU_BTN_CLICK_LISTENER, neutralButtonClickListener)
            arguments.putCharSequence(ARG_NEG_BTN_TEXT, negativeButtonText)
            arguments.putSerializable(ARG_NEG_BTN_CLICK_LISTENER, negativeButtonClickListener)
            arguments.putCharSequence(ARG_POS_BTN_TEXT, positiveButtonText)
            arguments.putSerializable(ARG_POS_BTN_CLICK_LISTENER, positiveButtonClickListener)
        }
    }

    private var viewPropertiesInitialized = false
    private var viewsInitialized = false

    var titleText: CharSequence? = null
        set(value) {
            if (field == value) return
            field = value
            if (viewsInitialized) {
                updateTitle()
            }
            if (viewPropertiesInitialized) {
                arguments.putCharSequence(ARG_TITLE, value)
            }
        }
    var neutralButtonText: CharSequence? = null
        set(value) {
            if (field == value) return
            field = value
            if (viewsInitialized) {
                updateNeutralButtonText()
            }
            if (viewPropertiesInitialized) {
                arguments.putCharSequence(ARG_NEU_BTN_TEXT, value)
            }
        }
    var neutralButtonClickListener: OnButtonClickListener? = null
        set(value) {
            if (field == value) return
            field = value
            if (viewPropertiesInitialized) {
                arguments.putSerializable(ARG_NEU_BTN_CLICK_LISTENER, value)
            }
        }
    var negativeButtonText: CharSequence? = null
        set(value) {
            if (field == value) return
            field = value
            if (viewsInitialized) {
                updateNegativeButtonText()
            }
            if (viewPropertiesInitialized) {
                arguments.putCharSequence(ARG_NEG_BTN_TEXT, value)
            }
        }
    var negativeButtonClickListener: OnButtonClickListener? = null
        set(value) {
            if (field == value) return
            field = value
            if (viewPropertiesInitialized) {
                arguments.putSerializable(ARG_NEG_BTN_CLICK_LISTENER, value)
            }
        }
    var positiveButtonText: CharSequence? = null
        set(value) {
            if (field == value) return
            field = value
            if (viewsInitialized) {
                updatePositiveButtonText()
            }
            if (viewPropertiesInitialized) {
                arguments.putCharSequence(ARG_POS_BTN_TEXT, value)
            }
        }
    var positiveButtonClickListener: OnButtonClickListener? = null
        set(value) {
            if (field == value) return
            field = value
            if (viewPropertiesInitialized) {
                arguments.putSerializable(ARG_POS_BTN_CLICK_LISTENER, value)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        titleText = arguments?.getCharSequence(ARG_TITLE)
        neutralButtonText = arguments?.getCharSequence(ARG_NEU_BTN_TEXT)
        neutralButtonClickListener = arguments?.getSerializable(ARG_NEU_BTN_CLICK_LISTENER) as OnButtonClickListener?
        negativeButtonText = arguments?.getCharSequence(ARG_NEG_BTN_TEXT)
        negativeButtonClickListener = arguments?.getSerializable(ARG_NEG_BTN_CLICK_LISTENER) as OnButtonClickListener?
        positiveButtonText = arguments?.getCharSequence(ARG_POS_BTN_TEXT)
        positiveButtonClickListener = arguments?.getSerializable(ARG_POS_BTN_CLICK_LISTENER) as OnButtonClickListener?

        viewPropertiesInitialized = true
    }

    /** 重写这个方法提供内容区域的view */
    abstract fun onCreateContentView(inflater: LayoutInflater, root: ViewGroup): View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.dialog_fragment_base, container, false) as ViewGroup
        root.addView(onCreateContentView(inflater, root), 1)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //通过动态设置内容区域的view宽度来设置dialog宽度（不能直接设置根view的宽度，因为它的LayoutParams为null）
        val point = Point()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)
        (view as ViewGroup).getChildAt(1).layoutParams.width = (point.x * 0.8f).toInt()

        viewsInitialized = true

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

    override fun onDetach() {
        super.onDetach()
        neutralButtonClickListener = null
        negativeButtonClickListener = null
        positiveButtonClickListener = null
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
