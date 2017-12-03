package me.imzack.lib.basedialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_fragment_message.*

class MessageDialogFragment : BaseDialogFragment() {

    companion object {

        private val ARG_MESSAGE = "message"

        fun newInstance(
                messageText: CharSequence,
                titleText: CharSequence? = null,
                neutralButtonText: CharSequence? = null,
                neutralButtonClickListener: OnButtonClickListener? = null,
                negativeButtonText: CharSequence? = null,
                negativeButtonClickListener: OnButtonClickListener? = null,
                positiveButtonText: CharSequence? = null,
                positiveButtonClickListener: OnButtonClickListener? = null
        ): MessageDialogFragment {
            val dialogFragment = MessageDialogFragment()
            val arguments = dialogFragment.arguments
            arguments.putCharSequence(ARG_MESSAGE, messageText)
            putBaseArguments(
                    arguments,
                    titleText,
                    dialogFragment.getString(android.R.string.cancel),
                    neutralButtonClickListener,
                    dialogFragment.getString(android.R.string.cancel),
                    negativeButtonClickListener,
                    dialogFragment.getString(android.R.string.ok),
                    positiveButtonClickListener
            )
            return dialogFragment
        }
    }

    // 内容必须有
    var messageText: CharSequence = ""
        set(value) {
            if (field == value) return
            field = value
            if (initialized) {
                updateMessage()
                arguments.putCharSequence(ARG_MESSAGE, value)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        if (args != null) {
            messageText = args.getCharSequence(ARG_MESSAGE)

            initialized = true
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, root: ViewGroup) =
            inflater.inflate(R.layout.dialog_fragment_message, root, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateMessage()
    }

    private fun updateMessage() {
        vMessageText.text = messageText
    }
}
