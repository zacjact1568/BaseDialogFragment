package me.imzack.lib.basedialogfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_fragment_simple.*

class SimpleDialogFragment : BaseDialogFragment() {

    companion object {

        private val ARG_MESSAGE = "message"

        fun newInstance(
                context: Context,
                titleText: CharSequence? = null,
                messageText: CharSequence,
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
                        override fun onClick(): Boolean {
                            return true
                        }
                    },
                    context.getString(android.R.string.ok),
                    object : OnButtonClickListener {
                        override fun onClick(): Boolean {
                            okButtonClickListener?.invoke()
                            return true
                        }
                    }
            )
            arguments.putCharSequence(ARG_MESSAGE, messageText)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, root: ViewGroup) =
            inflater.inflate(R.layout.dialog_fragment_simple, root, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vMessageText.text = arguments?.getCharSequence(ARG_MESSAGE)
    }
}
