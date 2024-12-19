import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONObject

class MQTTClient : MqttCallback {
    private lateinit var client: MqttClient

    init {
        // Inicializa el cliente MQTT
        client = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
        client.setCallback(this)
        client.connect()
    }

    // Método para publicar un mensaje utilizando Eclipse Paho
    fun publishMessage(topic: String, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            mqttMessage.qos = 1 // Nivel de calidad de servicio
            client.publish(topic, mqttMessage)
            println("Mensaje publicado en el tópico: $topic")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Callback para cuando se recibe un mensaje
    override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
        val message = String(mqttMessage.payload)
        println("Mensaje recibido en el tópico $topic: $message")

        // Validar y procesar el mensaje
        if (validateMessage(message)) {
            processMessage(message)
        } else {
            println("Mensaje inválido: $message")
        }
    }

    // Método para validar la estructura del mensaje recibido
    fun validateMessage(message: String): Boolean {
        return try {
            val jsonObject = JSONObject(message)
            jsonObject.has("field1") && jsonObject.has("field2") // Campos requeridos
        } catch (e: Exception) {
            false
        }
    }

    // Lógica personalizada para procesar el mensaje
    private fun processMessage(message: String) {
        val jsonObject = JSONObject(message)
        val field1 = jsonObject.getString("field1")
        val field2 = jsonObject.getString("field2")
        println("Procesando mensaje: field1 = $field1, field2 = $field2")
        // Mostrar mensaje en la pantalla o realizar otra acción necesaria
    }

    override fun connectionLost(cause: Throwable?) {
        println("Conexión perdida: ${cause?.message}")
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        println("Entrega completada: $token")
    }
}
