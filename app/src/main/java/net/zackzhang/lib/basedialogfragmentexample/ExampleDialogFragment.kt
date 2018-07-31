package net.zackzhang.lib.basedialogfragmentexample

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_fragment_simple.*
import net.zackzhang.lib.basedialogfragment.BaseDialogFragment

class ExampleDialogFragment : BaseDialogFragment() {

    companion object {

        private const val ARG_CONTENT = "content"
    }

    var okButtonClickListener: (() -> Unit)? = null

    override fun onCreateContentView(inflater: LayoutInflater, root: ViewGroup) =
            inflater.inflate(R.layout.dialog_fragment_simple, root, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vContentText.text = arguments?.getCharSequence(ARG_CONTENT)

        negativeButtonClickListener = { true }
        positiveButtonClickListener = {
            okButtonClickListener?.invoke()
            true
        }
    }

    class Builder(private val context: Context) {

        private val args = Bundle()

        fun setTitle(title: CharSequence): Builder {
            putTitle(args, title)
            return this
        }

        fun setContent(content: CharSequence): Builder {
            args.putCharSequence(ARG_CONTENT, content)
            return this
        }

        fun build(): ExampleDialogFragment {
            putNegativeButtonText(args, context.getString(android.R.string.cancel))
            putPositiveButtonText(args, context.getString(android.R.string.ok))
            val fragment = ExampleDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}