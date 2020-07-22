package practice.moviio.ui

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.view_search.view.*
import practice.moviio.R
import practice.moviio.utils.hideKeyboard


class SearchView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_search, this, true)

        open_search_button.setOnClickListener { openSearch() }
        close_search_button.setOnClickListener { closeSearch() }
    }

    private fun openSearch() {
        search_input_text.setText("")
        search_input_text.requestFocus()
        showKeyboard()
        search_open_view.visibility = View.VISIBLE
        val circularReveal = ViewAnimationUtils.createCircularReveal(
                search_open_view,
                (open_search_button.right + open_search_button.left) / 2,
                (open_search_button.top + open_search_button.bottom) / 2,
                0f, width.toFloat()
        )
        circularReveal.duration = 300
        circularReveal.start()
    }

    private fun showKeyboard(){
        val imm: InputMethodManager? = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun closeSearch() {
        context.hideKeyboard(this)
        val circularConceal = ViewAnimationUtils.createCircularReveal(
                search_open_view,
                (open_search_button.right + open_search_button.left) / 2,
                (open_search_button.top + open_search_button.bottom) / 2,
                width.toFloat(), 0f
        )

        circularConceal.duration = 300
        circularConceal.start()
        circularConceal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                search_open_view.visibility = View.INVISIBLE
                search_input_text.setText("")
                circularConceal.removeAllListeners()
            }
        })
    }
}

