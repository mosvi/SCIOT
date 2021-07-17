import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ReceiveConsumer {

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.155");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
        channel.queueDeclare("iot/logs", false, false, false, null);

        channel.basicConsume("iot/logs",true,(consumerTag,message)->{
            String str = new String(message.getBody(),"UTF-8");

            System.out.println(str);

        }, consumerTag ->{});
    }
}
