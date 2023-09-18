package com.example.sleepmate.presentation

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.sleepmate.complication.MyForegroundService
import com.example.sleepmate.complication.UdpClient
import com.example.sleepmate.presentation.theme.SleepmateTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    // UDP 클라이언트
    private val udpClient: UdpClient by lazy {
        // 여기에 버튼을 눌렀을 때 연결될 인터넷 ipAddress 넣기
        UdpClient("192.168.31.160", 9894)
    }

    // 권한 요청
    private val permissions = arrayOf(
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.BODY_SENSORS_BACKGROUND,
        Manifest.permission.FOREGROUND_SERVICE
    )
    private val requestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 어플리케이션에 부여할 권한을 요청
            ActivityCompat.requestPermissions(this, permissions, requestCode)

            // UDP 클라이언트 초기화
            WearApp("Android", context = this, udpClient = udpClient)

            // Foreground 서비스
            startForegroundService()
        }
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(this, MyForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

@Composable
fun WearApp(greetingName: String, context: Context, udpClient: UdpClient) {

    // 화면 구성 부분
    SleepmateTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SendUdpDataButton(udpClient)
        }
    }
}

@Composable
fun SendUdpDataButton(udpClient: UdpClient) {
    Button(
        onClick = {
            Log.d(TAG, "Button Clicked")
            val dataToSend = "전송할 데이터"
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    udpClient.sendData(dataToSend)
                    Log.d("MyApp", "Data sent successfully")
                } catch (e: Exception) {
                    Log.e("MyApp", "Error sending data: ${e.message}", e)
                }
            }
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("UDP 데이터 전송")
    }
}