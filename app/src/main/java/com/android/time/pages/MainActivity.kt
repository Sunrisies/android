package com.android.time.pages

import IMqttCallback
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.time.ui.theme.TimeTheme
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
//    try {
//        val persistence = MemoryPersistence()
//        val client = MqttClient("tcp://broker.emqx.io:1883", "2121122121", persistence)
//
//        client.setCallback(object : MqttCallback {
//            override fun connectionLost(cause: Throwable) {
//                System.out.println("连接丢失: $cause")
//
//            }
//
//            override fun deliveryComplete(token: IMqttDeliveryToken) {
//                System.out.println("消息发送完成: $token")
//            }
//
//            override fun messageArrived(topic: String, message: MqttMessage) {
//                System.out.println("接收到消息: $topic, $message")
//            }
//        })
//        try {
//            client.connect()
//            client.subscribe("D925070003", 1)
//            val message = MqttMessage("Hello MQTT".toByteArray())
//            client.publish("925070003", message)
//            client.disconnect()
//        } catch (e: MqttException) {
//            System.out.println("12121221: ${e}")
//            e.printStackTrace()
//        }
//    }catch (e:MqttException){
//        System.out.println("eeeeee:${e}")
//    }


    val mqttClientWrapper = MqttClientWrapper.getInstance("tcp://broker.emqx.io:1883", "21312312321", )
    mqttClientWrapper.setCallback(object : IMqttCallback {
        override fun onConnectionLost(cause: Throwable) {
            println("连接丢失: $cause")
        }

        override fun onDeliveryComplete(token: IMqttDeliveryToken) {
            println("消息发送完成: $token")
        }

        override fun onMessageArrived(topic: String, message: MqttMessage) {
            println("接收到消息: $topic, $message")
        }
    })

    mqttClientWrapper.connect()
    mqttClientWrapper.subscribe("D925070003")
    mqttClientWrapper.publish("925070003","Hello MQTT")
//    mqttClientWrapper.disconnect()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TimeTheme {
        Greeting("Android")
    }
}