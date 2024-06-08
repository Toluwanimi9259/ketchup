package com.techafresh.ketchtest


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.asFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.ketch.Ketch
import com.ketch.NotificationConfig
import com.ketch.Request
import com.ketch.Status
import com.techafresh.ketchtest.ui.theme.KetchTestTheme
import com.techafresh.timbertrack.TimberTrack
import dev.jianastrero.compose_permissions.composePermission
import java.io.File

class MainActivity : ComponentActivity() {

    private lateinit var appViewModel : AppViewModel

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appViewModelFactory = AppViewModelFactory(this)

        appViewModel = ViewModelProvider(this@MainActivity, appViewModelFactory)[AppViewModel::class.java]

        setContent {
            KetchTestTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UI(appViewModel)
                }
            }
        }
    }

//    @Preview(showBackground = true)
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE, Build.VERSION_CODES.R)
    @Composable
    fun UI(
        viewModel: AppViewModel
    ) {
        val permissions = composePermission(
//            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        )

    Column(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = viewModel.videoTitle.value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = viewModel.status.value, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))
            LinearProgressIndicator(progress = viewModel.progressVal.floatValue, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(text = viewModel.progressText.value, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = viewModel.sizeText.value,
                        fontSize = 15.sp
                    )
                }
                Button(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {

                        if (viewModel.buttonText.value == "Cancel") {
                            if (viewModel.request != null) {
                                viewModel.ketch.cancel(viewModel.request!!.id)
                            }
                        } else if (viewModel.buttonText.value == "Open") {
                            if (viewModel.request != null) {
                                openFile(
                                    this@MainActivity,
                                    viewModel.request!!.path,
                                    viewModel.request!!.fileName
                                )
                            }
                        } else { // Download
                            if (permissions.isGranted){
                                TimberTrack.log("Downloading...")
                                viewModel.downloadVideo()
                            }else{
                                TimberTrack.log("Requesting Permissions...")
                                permissions.request()
                                viewModel.downloadVideo()
                            }
                        }
                    }
                ) {
                    Text(text = viewModel.buttonText.value)
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = viewModel.documentTitle.value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = viewModel.status2.value, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))
            LinearProgressIndicator(progress = viewModel.progressVal2.floatValue, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(text = viewModel.progressText2.value, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = viewModel.sizeText2.value,
                        fontSize = 15.sp
                    )
                }
                Button(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {

                        if (viewModel.buttonText2.value == "Cancel") {
                            if (viewModel.requestPdf != null) {
                                viewModel.ketch.cancel(viewModel.request!!.id)
                            }
                        } else if (viewModel.buttonText2.value == "Open") {
                            if (viewModel.requestPdf != null) {
                                openFile(
                                    this@MainActivity,
                                    viewModel.requestPdf!!.path,
                                    viewModel.requestPdf!!.fileName
                                )
                            }
                        } else { // Download
                            if (permissions.isGranted){
                                TimberTrack.log("Downloading...")
                                viewModel.downloadDocument()
                            }else{
                                TimberTrack.log("Requesting Permissions...")
                                permissions.request()
                                viewModel.downloadDocument()
                            }
                        }
                    }
                ) {
                    Text(text = viewModel.buttonText2.value)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = viewModel.audioTitle.value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = viewModel.status3.value, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))
            LinearProgressIndicator(
                progress = viewModel.progressVal3.floatValue,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(text = viewModel.progressText3.value, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = viewModel.sizeText3.value,
                        fontSize = 15.sp
                    )
                }
                Button(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {

                        if (viewModel.buttonText3.value == "Cancel") {
                            if (viewModel.requestAudio != null) {
                                viewModel.ketch.cancel(viewModel.request!!.id)
                            }
                        } else if (viewModel.buttonText3.value == "Open") {
                            if (viewModel.requestAudio != null) {
                                openFile(
                                    this@MainActivity,
                                    viewModel.requestAudio!!.path,
                                    viewModel.requestAudio!!.fileName
                                )
                            }
                        } else { // Download
                            if (permissions.isGranted) {
                                TimberTrack.log("Downloading...")
                                viewModel.downloadAudio()
                            } else {
                                TimberTrack.log("Requesting Permissions...")
                                permissions.request()
                                viewModel.downloadAudio()
                            }
                        }
                    }
                ) {
                    Text(text = viewModel.buttonText3.value)
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = viewModel.imageTitle.value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = viewModel.status4.value, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))
            LinearProgressIndicator(progress = viewModel.progressVal4.floatValue, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(text = viewModel.progressText4.value, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = viewModel.sizeText4.value,
                        fontSize = 15.sp
                    )
                }
                Button(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {

                        if (viewModel.buttonText4.value == "Cancel") {
                            if (viewModel.requestImage != null) {
                                viewModel.ketch.cancel(viewModel.request!!.id)
                            }
                        } else if (viewModel.buttonText4.value == "Open") {
                            if (viewModel.requestImage != null) {
                                openFile(
                                    this@MainActivity,
                                    viewModel.requestImage!!.path,
                                    viewModel.requestImage!!.fileName
                                )
                            }
                        } else { // Download
                            if (permissions.isGranted){
                                TimberTrack.log("Downloading...")
                                viewModel.downloadImage()
                            }else{
                                TimberTrack.log("Requesting Permissions...")
                                permissions.request()
                                viewModel.downloadImage()
                            }
                        }
                    }
                ) {
                    Text(text = viewModel.buttonText4.value)
                }
            }
        }
    }
    }



    private fun openFile(context: Context?, path: String, fileName: String) {
        val file = File(path, fileName)
        if (file.exists()) {
            val uri = context?.applicationContext?.let {
                FileProvider.getUriForFile(
                    it,
                    it.packageName + ".provider",
                    file
                )
            }
            if (uri != null) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, context.contentResolver.getType(uri))
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                try {
                    ContextCompat.startActivity(context, intent, null)
                } catch (ignore: Exception) {
                }
            }
        }
    }

}

