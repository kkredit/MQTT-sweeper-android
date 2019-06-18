package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar m_toolbar;
    @BindView(R.id.github_kevin) Button mButtonGithubKevin;
    @BindView(R.id.github_brian) Button mButtonGithubBrian;
    @BindView(R.id.button_wiki) Button mButtonMqttWiki;
    @BindView(R.id.button_priv_policy) Button mButtonPrivPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(m_toolbar);

        mButtonGithubKevin.setText(getString(R.string.kevin_github));
        mButtonGithubBrian.setText(getString(R.string.brian_github));
        mButtonMqttWiki.setText(getString(R.string.read_more_on_wikipedia));
        mButtonPrivPolicy.setText(getString(R.string.priv_policy_online));
    }

    @OnClick(R.id.github_kevin)
    void onClickGithubKevin() {
        launchUri("https://github.com/kkredit");
    }

    @OnClick(R.id.github_brian)
    void onClickGithubBrian() {
        launchUri("https://github.com/Brinjenga");
    }

    @OnClick(R.id.button_wiki)
    void onClickMqttWiki() {
        launchUri("https://en.wikipedia.org/wiki/MQTT");
    }

    @OnClick(R.id.button_priv_policy)
    void onClickPrivPolicy() {
        launchUri("https://github.com/kkredit/MQTT-sweeper-android/blob/master/privacy_policy.md#privacy-policy");
    }

    private void launchUri(String uri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Uri.decode(uri)));
        startActivity(browserIntent);
    }
}
