import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
public interface IMqttCallback {
    fun onConnectionLost(cause: Throwable)
    fun onDeliveryComplete(token: IMqttDeliveryToken)
    fun onMessageArrived(topic: String, message: MqttMessage)
}
class MqttClientWrapper private constructor(val brokerAddress: String, val clientId: String) {

    companion object {
        // 单例对象
        private var instance: MqttClientWrapper? = null

        // 获取单例对象的方法
        fun getInstance(brokerAddress: String, clientId: String): MqttClientWrapper {
            return instance ?: synchronized(MqttClientWrapper::class) {
                instance ?: MqttClientWrapper(brokerAddress, clientId)
            }.also { instance = it }
        }
    }
    private var callback: IMqttCallback? = null

    private var client: MqttClient? = null

    init {
        val persistence = MemoryPersistence()

        client = MqttClient(brokerAddress, clientId,persistence)
        client?.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                callback?.onConnectionLost(cause)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                callback?.onDeliveryComplete(token)
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                callback?.onMessageArrived(topic, message)
            }
        })
    }
    fun setCallback(callback: IMqttCallback) {
        this.callback = callback
    }
    fun connect() {
        try {
            client?.connect()
        } catch (e: MqttException) {
            println("连接失败: ${e.message}")
        }
    }

    fun subscribe(topic:String) {
        try {
            client?.subscribe(topic, 1)
        } catch (e: MqttException) {
            println("订阅失败: ${e.message}")
        }
    }

    fun publish(topic:String,message: String) {
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

