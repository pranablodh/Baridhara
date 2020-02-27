package com.consistentservices.baridhara.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.consistentservices.baridhara.R;
import com.consistentservices.baridhara.adapterDataBinder.pumpData;
import com.consistentservices.baridhara.miscellaneousFunctions.insertStringPublish;
import com.consistentservices.baridhara.miscellaneousFunctions.insertStringSubscribe;
import com.consistentservices.baridhara.networkFunction.url;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class pumpAdapter extends RecyclerView.Adapter<pumpAdapter.pumpViewHolder>
{
    private Context mCtx;
    private List<pumpData> pumpList;

    private MqttAndroidClient client;
    MqttConnectOptions options;

    //Dialog Box Initialization
    private Dialog sendMessageDialog;
    private CardView dialogCardView;
    private TextView sendMessageHeader;
    private ImageView sendingImage;
    private ImageView okayImage;

    //MQTT PAYLOAD AND BROKER CONFIGURATION
    private static final String MQTTBROKER = url.MQTT_BROKER;
    private static final String USERNAME = url.MQTT_USER_NAME;
    private static final String PASSWORD = url.MQTT_PASSWORD;
    private static final String payloadON = "A1";
    private static final String payloadOFF = "A0";

    //System Functions
    public pumpAdapter(Context mCtx, List<pumpData> pumpList)
    {
        this.mCtx = mCtx;
        this.pumpList = pumpList;
    }

    //System Functions
    @NonNull
    @Override
    public pumpViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.card_layout, null);
        return new pumpViewHolder(view);
    }

    //System Functions
    @Override
    public void onBindViewHolder(@NonNull final pumpViewHolder pumpViewHolder, int i)
    {
        pumpData pData = pumpList.get(i);

        pumpViewHolder.name.setText(pData.getName());
        pumpViewHolder.id.setText(pData.getDeviceID());
        pumpViewHolder.status.setText(pData.getStatus());
        pumpViewHolder.phaseR.setText(pData.getPhaseR());
        pumpViewHolder.phaseY.setText(pData.getPhaseY());
        pumpViewHolder.phaseB.setText(pData.getPhaseB());

        String clientID = MqttClient.generateClientId();
        client = new MqttAndroidClient(mCtx,MQTTBROKER,clientID);

        sendMessageDialog = new Dialog(mCtx);

        //On Click Listener
        pumpViewHolder.on.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                publish(String.valueOf(pumpViewHolder.name.getText()),String.valueOf(pumpViewHolder.id.getText()), payloadON);
            }
        });

        //On Click Listener
        pumpViewHolder.off.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                publish(String.valueOf(pumpViewHolder.name.getText()),String.valueOf(pumpViewHolder.id.getText()), payloadOFF);
            }
        });

        //MQTT Connect Function
        connect(String.valueOf(pumpViewHolder.id.getText()));
    }

    //System Functions
    @Override
    public int getItemCount()
    {
        return pumpList.size();
    }

    //System Functions
    class pumpViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView id;
        TextView status;
        TextView phaseR;
        TextView phaseY;
        TextView phaseB;
        Button on;
        Button off;

        //System Functions
        public pumpViewHolder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.id);
            status = itemView.findViewById(R.id.status);
            phaseR = itemView.findViewById(R.id.phaseR);
            phaseY = itemView.findViewById(R.id.phaseY);
            phaseB = itemView.findViewById(R.id.phaseB);
            on = itemView.findViewById(R.id.on);
            off = itemView.findViewById(R.id.off);
        }
    }

    //Function For MQTT Connect
    public void connect(final String ID)
    {
        if(!client.isConnected())
        {
            String clientID = MqttClient.generateClientId();
            client = new MqttAndroidClient(mCtx,MQTTBROKER,clientID);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());

            try
            {
                IMqttToken token = client.connect(options);
                token.setActionCallback(new IMqttActionListener()
                {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken)
                    {
                        //We are Connected
                        subscribe(ID);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                    {
                        //Something went wrong e.g. connection timeout or firewall problems
                    }
                });
            }
            catch (MqttException e)
            {
                e.printStackTrace();
            }


            //MQTT Callback Listener
            client.setCallback(new MqttCallback()
            {
                @Override
                public void connectionLost(Throwable cause)
                {
                    Toast.makeText(mCtx,"Connection Lost.....",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void messageArrived(final String topic, final MqttMessage message) throws Exception
                {
                    changeDataByFiltering(topic,new String(message.getPayload()));
                    //Toast.makeText(mCtx,new String(message.getPayload()),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token)
                {
                    showDialogMessage("","Message Delivered",1);
                    //Toast.makeText(mCtx,"Message Delivered",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //MQTT Message Publish
    public void publish(String Name, String Topic, String PayLoad)
    {
        if(client.isConnected())
        {
            String topic = Topic;
            String payload = PayLoad;
            String name = Name;

            //Adding PUB/ To Our Topic
            insertStringPublish INSPUB = new insertStringPublish();
            topic = INSPUB.insertString(topic);

            Log.d("MQTT_TOP","PUB:"+topic);

            try
            {
                client.publish(topic, payload.getBytes(), 0, false);
                showDialogMessage(name+": ", "Sending Message",0);
                //Toast.makeText(mCtx, name+": Message Published", Toast.LENGTH_SHORT).show();
            }
            catch (MqttException e)
            {
                e.printStackTrace();
                showDialogMessage(name+": ", "Message Didn't Go Through",2);
                //Toast.makeText(mCtx, name+": Message Didn't Go Through", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //MQTT Subscribe
    public void subscribe(String ID)
    {
        insertStringSubscribe INSSUB = new insertStringSubscribe();
        String id = INSSUB.insertString(ID);
        if (client.isConnected())
        {
            try
            {
                client.subscribe(id, 0);
            }
            catch (MqttException e)
            {
                e.printStackTrace();
            }
        }
    }

    //Function to Update Text View As Per MQTT Payload
    public void changeDataByFiltering(String MQTT_TOPIC, String MQTT_PAYLOAD)
    {
        for(pumpData pD: pumpList)
        {
            //Adding SUB/ To Our Topic
            insertStringSubscribe INSSUB = new insertStringSubscribe();
            String mqtt_topic_from_list = INSSUB.insertString(pD.getDeviceID());
            Log.d("MQTT_TOP","PUB:"+mqtt_topic_from_list);
            if(mqtt_topic_from_list.trim().toLowerCase().equals(MQTT_TOPIC.trim().toLowerCase()))
            {
                try
                {
                    JSONObject mqtt_message = new JSONObject(MQTT_PAYLOAD);
                    pD.setStatus(mqtt_message.getString("Status"));
                    pD.setPhaseR(mqtt_message.getString("phaseR"));
                    pD.setPhaseY(mqtt_message.getString("phaseY"));
                    pD.setPhaseB(mqtt_message.getString("phaseB"));
                    notifyDataSetChanged();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    //Function to Show Dialog
    private void showDialogMessage(String Name ,String Message, int flag)
    {
        sendMessageDialog.dismiss();
        sendMessageDialog.setContentView(R.layout.dialog_sending_message);
        sendMessageDialog.setCancelable(false);
        sendingImage = (ImageView) sendMessageDialog.findViewById(R.id.sendingImage);
        okayImage = (ImageView) sendMessageDialog.findViewById(R.id.okayImage);
        dialogCardView = (CardView) sendMessageDialog.findViewById(R.id.dialogCardView);

        if(flag == 1)
        {
            Glide.with(mCtx).asGif().load(R.drawable.sms_send).into(sendingImage);
            dialogCardView.setCardBackgroundColor(Color.parseColor("#FF814C"));
            okayImage.setBackgroundResource(R.drawable.okay_green);
            okayImage.setVisibility(View.VISIBLE);
        }

        else if(flag == 2)
        {
           Glide.with(mCtx).asGif().load(R.drawable.error).into(sendingImage);
           dialogCardView.setCardBackgroundColor(Color.parseColor("#DE0239"));
           okayImage.setBackgroundResource(R.drawable.okay_red);
           okayImage.setVisibility(View.VISIBLE);
        }

        else
        {
            okayImage.setVisibility(View.INVISIBLE);
        }

        okayImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendMessageDialog.dismiss();
            }
        });
        sendMessageHeader = (TextView) sendMessageDialog.findViewById(R.id.sendMessageHeader);
        sendMessageHeader.setText(String.format("%s%s", Name, Message));
        Objects.requireNonNull(sendMessageDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sendMessageDialog.show();
    }
}
