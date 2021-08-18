package lead.codeoverflow.vkcupfinal.extension

import android.widget.SeekBar

fun SeekBar.addProgressListener(onProgress: (Int) -> Unit, onStopTouch: (Int) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onProgress.invoke(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            onStopTouch.invoke(seekBar?.progress ?: progress)
        }

    })
}