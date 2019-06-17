package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Broker;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Topic;
import edu.gvsu.cis.mqtt_sweeper.DataStores.TopicContent;

public class PublishTopic extends AppCompatActivity {

    @BindView(R.id.fabTopic) FloatingActionButton fab;
    @BindView(R.id.addtopic) EditText newTopic;
    @BindView(R.id.payload) EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_topic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        fab.setOnClickListener(view -> {
            Intent result = new Intent();
            Topic aTopic = new Topic();
            aTopic.message = message.getText().toString();
            aTopic.topic = newTopic.getText().toString();
            Parcelable parcel = Parcels.wrap(aTopic);
            result.putExtra("Topic_Item",parcel);
            setResult(RESULT_OK,result);
            Snackbar.make(view, "TopicItem Added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            finish();
        });
    }

}
