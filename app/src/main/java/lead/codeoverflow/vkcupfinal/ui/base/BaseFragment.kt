package lead.codeoverflow.vkcupfinal.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    abstract val layoutResId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(layoutResId, container, false)
    }

    fun showToast(@StringRes stringId: Int) {
        Toast.makeText(requireContext(), stringId, Toast.LENGTH_LONG).show()
    }

    fun showToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_LONG).show()
    }

    fun showSnackbar(@StringRes stringId: Int, view: View? = this.view) {
        view?.let {
            Snackbar.make(it, stringId, Snackbar.LENGTH_LONG).show()
        }
    }


    fun showSnackbar(string: String, view: View? = this.view) {
        view?.let {
            Snackbar.make(it, string, Snackbar.LENGTH_LONG).show()
        }
    }

}