package lead.codeoverflow.vkcupfinal.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

fun getFileRealPath(
    context: Context, uri: Uri
): File? {
    val contentResolver = context.contentResolver ?: return null

    // Create file path inside app's data dir
    val filePath = (context.applicationInfo.dataDir + File.separator
            + System.currentTimeMillis())
    val file = File(filePath)
    try {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val outputStream: OutputStream = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()
    } catch (ignore: IOException) {
        return null
    }
    return file
}