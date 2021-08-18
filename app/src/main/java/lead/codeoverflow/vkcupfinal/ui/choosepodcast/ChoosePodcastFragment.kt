package lead.codeoverflow.vkcupfinal.ui.choosepodcast

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_choose_podcast.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.ui.base.BaseFragment
import lead.codeoverflow.vkcupfinal.utils.getFileRealPath
import java.io.File

class ChoosePodcastFragment : BaseFragment() {
    override val layoutResId: Int = R.layout.fragment_choose_podcast

    private var jsonStr = ""

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                uri?.let {
                    val path = getFileRealPath(requireContext(), it)
                    val file = File(path.toString())
                    etJsonLink.setText(file.name)
                    jsonStr = file.readText()
                }
            }
        }

    private val requestPermissionExternal =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            if (!permission.containsValue(false)) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                val chooser =
                    Intent.createChooser(intent, getString(R.string.choose_podcast_json_title))
                activityResultLauncher.launch(chooser)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.choose_permission_external_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etJsonLink.setOnClickListener {
            val permissions = mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            requestPermissionExternal.launch(permissions.toTypedArray())
        }
        btnSave.setOnClickListener {
            if (etRssLink.text.isNotBlank()) {
                findNavController().navigate(
                    ChoosePodcastFragmentDirections.actionChoosePodcastFragmentToPodcastFragment(
                        etRssLink.text.toString(),
                        jsonStr
                    )
                )
            }
        }
    }

}