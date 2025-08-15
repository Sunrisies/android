package com.android.time.utils
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttException
class MqttClientWrapper(val brokerAddress: String, val clientId: String, val topi c: String) {
    private var client: MqttClient? = null

    init {
        client = MqttClient(brokerAddress, clientId)
        client?.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                println("连接丢失: $cause")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                println("消息发送完成: $token")
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                println("接收到消息: $topic, $message")
            }
        })
    }

    fun connect() {
        try {
            client?.connect()
        } catch (e: MqttException) {
            println("连接失败: ${e.message}")
        }
    }

    fun subscribe() {
        try {
            client?.subscribe(topic, 1)
        } catch (e: MqttException) {
            println("订阅失败: ${e.message}")
        }
    }

    fun publish(message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            client?.publish(topic, mqttMessage)
        } catch (e: MqttException) {
            println("发布消息失败: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            client?.disconnect()
        } catch (e: MqttException) {
            println("断开连接失败: ${e.message}")
        }
    }
}