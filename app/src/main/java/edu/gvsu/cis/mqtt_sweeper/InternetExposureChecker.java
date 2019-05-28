package edu.gvsu.cis.mqtt_sweeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import com.fooock.shodan.ShodanRestApi;
import com.fooock.shodan.model.host.Host;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class InternetExposureChecker {

    public interface IEC_Handler {
        void iecOnComplete();
        void iecOnError(Throwable e);
        void iecReceiveAnswer(boolean connected);
    }

    private ShodanRestApi m_apiHandle;
    private IEC_Handler m_handler;

    public InternetExposureChecker(String apiKey, IEC_Handler handler) {
        m_apiHandle = new ShodanRestApi(apiKey);
        m_handler = handler;
    }

    public boolean getExposed() {
        String ip;
        try {
            ip = this.getIp();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        m_apiHandle.hostByIp(false,true, ip)
            .subscribe(new DisposableObserver<Host>() {
                @Override
                public void onComplete() {
                    m_handler.iecOnComplete();
                }

                @Override
                public void onError(Throwable e) {
                    m_handler.iecOnError(e);
                }

                @Override
                public void onNext(Host hostReport) {
                    // TODO: use report to determine if the server is visible
                    m_handler.iecReceiveAnswer(true);
                }
            });
        return true;
    }

    // Credit to https://stackoverflow.com/a/14541376/11407115
    public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
