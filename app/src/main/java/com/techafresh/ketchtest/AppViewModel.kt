package com.techafresh.ketchtest

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.asFloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.ketch.Ketch
import com.ketch.NotificationConfig
import com.ketch.Request
import com.ketch.Status
import com.techafresh.timbertrack.TimberTrack
import java.io.File

class AppViewModel(context: Context) : ViewModel() {
    var request: Request? = null
    private var length1: Long = 0L
    private var length1Text: String = "0.00 b"

    var requestPdf: Request? = null
    private var length2: Long = 0L
    private var length2Text: String = "0.00 b"

    var requestAudio: Request? = null
    private var length3: Long = 0L
    private var length3Text: String = "0.00 b"

    var requestImage: Request? = null
    private var length4: Long = 0L
    private var length4Text: String = "0.00 b"

    val videoTitle = mutableStateOf("VID 0000011.mp4")
    val buttonText =  mutableStateOf("Download")
    val status =  mutableStateOf("")
    val progressVal = mutableFloatStateOf(0f)
    val progressText = mutableStateOf("0%/0.00b")
    val sizeText = mutableStateOf("")

    val documentTitle = mutableStateOf("Learn python in a week.pdf")
    val buttonText2 =  mutableStateOf("Download")
    val status2 =  mutableStateOf("")
    val progressVal2 = mutableFloatStateOf(0f)
    val progressText2 = mutableStateOf("0%/0.00 b")
    val sizeText2 = mutableStateOf("")

    val audioTitle = mutableStateOf("Havana feat Young Thug.mp3")
    val buttonText3 =  mutableStateOf("Download")
    val status3 =  mutableStateOf("")
    val progressVal3 = mutableFloatStateOf(0f)
    val progressText3 = mutableStateOf("0%/0.00 b")
    val sizeText3 = mutableStateOf("")

    val imageTitle = mutableStateOf("Cover.jpg")
    val buttonText4 =  mutableStateOf("Download")
    val status4 =  mutableStateOf("")
    val progressVal4 = mutableFloatStateOf(0f)
    val progressText4 = mutableStateOf("0%/0.00 b")
    val sizeText4 = mutableStateOf("")

    var ketch : Ketch = Ketch.init(
        context,
        notificationConfig = NotificationConfig(
            enabled = true,
            smallIcon = R.drawable.ic_launcher_foreground
        )
    )

    fun downloadVideo(){
        request = ketch.download(
            url = "https://nw15.seedr.cc/ff_get/5438019981/8.%20Top-k%20and%20Other%20Parameters.mp4?st=OMkSEm6qHn04MUAeZ6l--w&e=1717951148",
            fileName = "VID 0000011.mp4",
            onQueue = {
                videoTitle.value = request?.fileName.toString()
                status.value = Status.QUEUED.toString()
            },
            onStart = { length ->
                length1 = length
                length1Text = Util.getTotalLengthText(length)
                status.value = Status.STARTED.toString()
            },
            onProgress = { progress, speedInBytePerMs ->
                status.value = Status.PROGRESS.toString()
                progressVal.floatValue = progress.toFloat()/100
                TimberTrack.log(message = "$progress", tag = "TimberTrack")
                progressText.value = "$progress%/ $length1Text"
                sizeText.value = Util.getTimeLeftText(
                    speedInBytePerMs,
                    progress,
                    length1
                ) + ", " + Util.getSpeedText(speedInBytePerMs)
            },
            onSuccess = {
                status.value = Status.SUCCESS.toString()
                progressVal.floatValue = 100f
                progressText.value = "100%/$length1Text"
                sizeText.value = ""
                buttonText.value = "Open"
            },
            onFailure = {
                status.value = Status.FAILED.toString()
                progressText.value = it
            },
            onCancel = {
                status.value = Status.CANCELLED.toString()
                progressVal.floatValue = 0f
                progressText.value = ""
            }
        )
    }

    fun downloadDocument(){
        requestPdf = ketch.download(
            url = "https://nw14.seedr.cc/ff_get/5441430150/Learn%20Python%20In%20A%20Week%20And%20Master%20It.pdf?st=sr3TH3HtuFLMEO4BCZumsA&e=1717963551",
            fileName = "Learn python in a week.pdf",
            onQueue = {
                documentTitle.value = requestPdf?.fileName.toString()
                status2.value = Status.QUEUED.toString()
            },
            onStart = { length ->
                length2 = length
                length2Text = Util.getTotalLengthText(length)
                status2.value = Status.STARTED.toString()
            },
            onProgress = { progress, speedInBytePerMs ->
                status2.value = Status.PROGRESS.toString()
                progressVal2.floatValue = progress.toFloat()/100
                TimberTrack.log(message = "$progress", tag = "TimberTrack")
                progressText2.value = "$progress%/ $length2Text"
                sizeText2.value = Util.getTimeLeftText(
                    speedInBytePerMs,
                    progress,
                    length2
                ) + ", " + Util.getSpeedText(speedInBytePerMs)
            },
            onSuccess = {
                status2.value = Status.SUCCESS.toString()
                progressVal2.floatValue = 100f
                progressText2.value = "100%/$length2Text"
                sizeText2.value = ""
                buttonText2.value = "Open"
            },
            onFailure = {
                status2.value = Status.FAILED.toString()
                progressText2.value = it
            },
            onCancel = {
                status2.value = Status.CANCELLED.toString()
                progressVal2.floatValue = 0f
                progressText2.value = ""
            }
        )
    }

    fun downloadAudio(){
        requestAudio = ketch.download(
            url = "https://de21.seedr.cc/ff_get/5441437260/01%20Havana%20(feat.%20Young%20Thug).mp3?st=gzeGDZ-yS4rNRTAx5ysvIQ&e=1717963099",
            fileName = "Havana feat Young Thug.mp3",
            onQueue = {
                audioTitle.value = requestAudio?.fileName.toString()
                status3.value = Status.QUEUED.toString()
            },
            onStart = { length ->
                length3 = length
                length3Text = Util.getTotalLengthText(length)
                status3.value = Status.STARTED.toString()
            },
            onProgress = { progress, speedInBytePerMs ->
                status3.value = Status.PROGRESS.toString()
                progressVal3.floatValue = progress.toFloat()/100
                TimberTrack.log(message = "$progress", tag = "TimberTrack")
                progressText3.value = "$progress%/ $length3Text"
                sizeText3.value = Util.getTimeLeftText(
                    speedInBytePerMs,
                    progress,
                    length3
                ) + ", " + Util.getSpeedText(speedInBytePerMs)
            },
            onSuccess = {
                status3.value = Status.SUCCESS.toString()
                progressVal3.floatValue = 100f
                progressText3.value = "100%/$length3Text"
                sizeText3.value = ""
                buttonText3.value = "Open"
            },
            onFailure = {
                status3.value = Status.FAILED.toString()
                progressText3.value = it
            },
            onCancel = {
                status3.value = Status.CANCELLED.toString()
                progressVal3.floatValue = 0f
                progressText3.value = ""
            }
        )
    }

    fun downloadImage(){
        requestImage = ketch.download(
            url = "https://de21.seedr.cc/ff_get/5441437261/Cover.jpg?st=a0_I7GgY_MBA0-00lbIXhg&e=1717963372",
            fileName = "Cover.jpg",
            onQueue = {
                imageTitle.value = requestImage?.fileName.toString()
                status4.value = Status.QUEUED.toString()
            },
            onStart = { length ->
                length4 = length
                length4Text = Util.getTotalLengthText(length)
                status4.value = Status.STARTED.toString()
            },
            onProgress = { progress, speedInBytePerMs ->
                status4.value = Status.PROGRESS.toString()
                progressVal4.floatValue = progress.toFloat()/100
                TimberTrack.log(message = "$progress", tag = "TimberTrack")
                progressText4.value = "$progress%/ $length4Text"
                sizeText4.value = Util.getTimeLeftText(
                    speedInBytePerMs,
                    progress,
                    length4
                ) + ", " + Util.getSpeedText(speedInBytePerMs)
            },
            onSuccess = {
                status4.value = Status.SUCCESS.toString()
                progressVal4.floatValue = 100f
                progressText4.value = "100%/$length4Text"
                sizeText4.value = ""
                buttonText4.value = "Open"
            },
            onFailure = {
                status4.value = Status.FAILED.toString()
                progressText4.value = it
            },
            onCancel = {
                status4.value = Status.CANCELLED.toString()
                progressVal4.floatValue = 0f
                progressText4.value = ""
            }
        )
    }

}