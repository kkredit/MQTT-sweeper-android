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
import edu.gvsu.cis.mqtt_sweeper.DataStores.Topic;
import edu.gvsu.cis.mqtt_sweeper.DataStores.TopicContent;

public class PublishTopic extends AppCompatActivity {

    @BindView(R.id.fabTopic) FloatingActionButton fab;
    @BindView(R.id.addtopic) EditText newTopic;
    @BindView(R.id.payload) EditText message;
    TopicContent.topicItem topic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_topic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Topic = newTopic.getText().toString();
                String Message = message.getText().toString();
                Intent result = new Intent();
                topic.message = Message;
                topicList.add(1,topic);
                Parcelable parcel = Parcels.wrap(topic);
                result.putExtra("Topic",parcel);
                setResult(RESULT_OK,result);
                Snackbar.make(view, "Topic Added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
            }
        });
    }

}
