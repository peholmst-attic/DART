package net.pkhapps.dart.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ActivityHome extends AppCompatActivity {

    private final BlockingDeque<String> deque = new LinkedBlockingDeque<>();
    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpConnectionFactory();
        publishToAMQP();
        setUpPubButton();

        final Handler incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                TextView tv = (TextView) findViewById(R.id.textView);
                Date now = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                tv.append(ft.format(now) + ' ' + message + '\n');
            }
        };
        subscribe(incomingMessageHandler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpConnectionFactory() {
        String uri = "amqp://guest:guest@192.168.1.121";
        try {
            connectionFactory.setAutomaticRecoveryEnabled(false);
            connectionFactory.setUri(uri);
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private void publishToAMQP() {
        final Runnable job = new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = connectionFactory.newConnection();
                    Channel ch = connection.createChannel();
                    ch.confirmSelect();

                    while (true) { // TODO This is ugly
                        String message = deque.takeFirst();
                        try {
                            ch.basicPublish("amq.fanout", "chat", null, message.getBytes());
                            ch.waitForConfirmsOrDie();
                        } catch (Exception ex) {
                            deque.putFirst(message);
                        }
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    return;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    executorService.schedule(this, 5, TimeUnit.SECONDS);
                }
            }
        };
        executorService.submit(job);
    }

    private void setUpPubButton() {
        Button button = (Button) findViewById(R.id.publish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.text);
                publishMessage(et.getText().toString());
                et.setText("");
            }
        });
    }

    private void publishMessage(String message) {
        try {
            deque.putLast(message);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void subscribe(final Handler handler) {
        final Runnable job = new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = connectionFactory.newConnection();
                    Channel channel = connection.createChannel();
                    channel.basicQos(1);
                    AMQP.Queue.DeclareOk q = channel.queueDeclare();
                    channel.queueBind(q.getQueue(), "amq.fanout", "chat");
                    QueueingConsumer consumer = new QueueingConsumer(channel);
                    channel.basicConsume(q.getQueue(), true, consumer);

                    while (true) { // TODO This is ugly
                        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                        String message = new String(delivery.getBody());
                        Message msg = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", message);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    return;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    executorService.schedule(this, 5, TimeUnit.SECONDS);
                }
            }
        };
        executorService.submit(job);
    }
}
