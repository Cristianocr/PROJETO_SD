package edu.ufp.inf.sd.rabbitmq.chatgui;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import edu.ufp.inf.sd.rabbitmq.util.RabbitUtils;
import edu.ufp.inf.sd.rabbitmq.client.Client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rui
 */
public class Observer {

    //Reference for gui
    private ObserverGuiClient gui;

    //Preferences for exchange...
    private Channel channelToRabbitMq;
    private String exchangeName;
    private BuiltinExchangeType exchangeType;
    //private final String[] exchangeBindingKeys;
    private String messageFormat;

    //Store received message to be get by gui
    private String receivedMessage;

    private int playerID;
    private Client client;
    public int count = 0;
    public ArrayList<Integer> players = new ArrayList<>();

    public int getPlayerNumber() {
        return players.size();
    }

    public ArrayList<Integer> getPlayersTotal() {
        return this.players;
    }

    public int getPlayerID() {
        return playerID;
    }


    public Observer(ObserverGuiClient gui) {
        this.gui = gui;
    }

    /**
     * @param gui
     */
    public Observer(ObserverGuiClient gui, String host, int port, String user, String pass, String exchangeName, BuiltinExchangeType exchangeType, String messageFormat, int playerID, Client client) throws IOException, TimeoutException {
        this.gui = gui;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, " going to attach observer to host: " + host + "...");

        Connection connection = RabbitUtils.newConnection2Server(host, port, user, pass);
        this.channelToRabbitMq = RabbitUtils.createChannel2Server(connection);

        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        //String[] bindingKeys={"",""};
        //this.exchangeBindingKeys=bindingKeys;
        this.messageFormat = messageFormat;
        this.client = client;
        this.playerID = playerID;
        players.add(playerID);

        bindExchangeToChannelRabbitMQ();
        attachConsumerToChannelExchangeWithKey();

    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Binds the channel to given exchange name and type.
     */
    private void bindExchangeToChannelRabbitMQ() throws IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Declaring Exchange '" + this.exchangeName + "' with type " + this.exchangeType);

        /* TODO: Declare exchange type  */
        this.channelToRabbitMq.exchangeDeclare(exchangeName + "observer", BuiltinExchangeType.FANOUT);

    }

    /**
     * Creates a Consumer associated with an unnamed queue.
     */
    public void attachConsumerToChannelExchangeWithKey() {
        try {

            String queueName = this.channelToRabbitMq.queueDeclare().getQueue();


            String routingKey = "";
            channelToRabbitMq.queueBind(queueName, exchangeName + "observer", routingKey);
            channelToRabbitMq.queuePurge(queueName);


            Logger.getLogger(this.getClass().getName()).log(Level.INFO, " Created consumerChannel bound to Exchange " + this.exchangeName + "...");

            /* Use a DeliverCallback lambda function instead of DefaultConsumer to receive messages from queue;
               DeliverCallback is an interface which provides a single method:
                void handle(String tag, Delivery delivery) throws IOException; */
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), messageFormat);

                //Store the received message
                setReceivedMessage(message);
                System.out.println(" [x] Consumer Tag [" + consumerTag + "] - Received '" + message + "'");


            };
            CancelCallback cancelCallback = consumerTag -> {
                System.out.println(" [x] Consumer Tag [" + consumerTag + "] - Cancel Callback invoked!");
            };

            channelToRabbitMq.basicConsume(queueName, true, deliverCallback, cancelCallback);

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Publish messages to existing exchange instead of the nameless one.
     * - The routingKey is empty ("") since the fanout exchange ignores it.
     * - Messages will be lost if no queue is bound to the exchange yet.
     * - Basic properties can be: MessageProperties.PERSISTENT_TEXT_PLAIN, etc.
     */
    public void sendMessage(String msgToSend) throws IOException {
        //RoutingKey will be ignored by FANOUT exchange
        String routingKey = "";
        BasicProperties prop = MessageProperties.PERSISTENT_TEXT_PLAIN;

        channelToRabbitMq.basicPublish(exchangeName + "observer", routingKey, prop, msgToSend.getBytes(StandardCharsets.UTF_8));

    }

    /**
     * @return the most recent message received from the broker
     */
    public String getReceivedMessage() {
        return receivedMessage;
    }

    /**
     * @param receivedMessage the received message to set
     */
    public void setReceivedMessage(String receivedMessage) {
        this.receivedMessage = receivedMessage;
    }

    public int getCount() {
        return count;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
