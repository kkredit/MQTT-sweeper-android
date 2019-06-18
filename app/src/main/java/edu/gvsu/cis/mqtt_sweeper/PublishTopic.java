package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Topic;

public class PublishTopic extends AppCompatActivity {

    @BindView(R.id.cTopic) Button addTopic;
    @BindView(R.id.topicHeading)
    EditText newTopic;
    @BindView(R.id.payload)
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_topic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.cTopic)
    public void addTopic() {
        Intent result = new Intent();
        Topic aTopic = new Topic();
        aTopic.message = message.getText().toString();
        aTopic.topic = newTopic.getText().toString();
        Parcelable parcel = Parcels.wrap(aTopic);
        result.putExtra("Topic_Item", parcel);
        setResult(RESULT_OK, result);
        Toast.makeText(this, "Topic A", Toast.LENGTH_SHORT).show();
        finish();
    }
}
