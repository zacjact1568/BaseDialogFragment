package me.imzack.lib.basedialogfragmentexample

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_fragment_simple.*
import me.imzack.lib.basedialogfragment.BaseDialogFragment

class SimpleDialogFragment : BaseDialogFragment() {

    companion object {

        private val ARG_CONTENT = "content"

        fun newInstance(
                context: Context,
                titleText: CharSequence? = null,
                contentText: CharSequence,
                okButtonClickListener: (() -> Unit)? = null
        ): SimpleDialogFragment {
            val fragment = SimpleDialogFragment()
            val arguments = Bundle()
            putBaseArguments(
                    arguments,
                    titleText,
                    null,
                    null,
                    context.getString(android.R.string.cancel),
                    object : OnButtonClickListener {
                        override fun onClick() = true
                    },
                    context.getString(android.R.string.ok),
                    object : OnButtonClickListener {
                        override fun onClick(): Boolean {
                            okButtonClickListener?.invoke()
                            return true
                        }
                    }
            )
            arguments.putCharSequence(ARG_CONTENT, contentText)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, root: ViewGroup) =
            inflater.inflate(R.layout.dialog_fragment_simple, root, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vContentText.text = arguments?.getCharSequence(ARG_CONTENT)
    }
}