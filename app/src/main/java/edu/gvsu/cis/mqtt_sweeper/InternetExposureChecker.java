package edu.gvsu.cis.mqtt_sweeper;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.fooock.shodan.ShodanRestApi;
import com.fooock.shodan.model.host.Host;

import io.reactivex.observers.DisposableObserver;

public class InternetExposureChecker {

    private static final int PORT_NO_TLS = 1883;
    private static final int PORT_TLS = 8883;

    public interface IEC_Handler {
        void iecOnComplete();
        void iecOnError(Throwable e);
        void iecReceiveAnswer(boolean connected);
    }

    private ShodanRestApi m_apiHandle;
    private IEC_Handler m_handler;

    InternetExposureChecker(String apiKey, IEC_Handler handler) {
        m_apiHandle = new ShodanRestApi(apiKey);
        m_handler = handler;
    }

    public boolean getExposed() {
//        String ip;
        try {
            new GetIpTask().execute();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

//        m_apiHandle.hostByIp(false,true, ip)
//            .subscribe(new DisposableObserver<Host>() {
//                @Override
//                public void onComplete() {
//                    m_handler.iecOnComplete();
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    m_handler.iecOnError(e);
//                }
//
//                @Override
//                public void onNext(Host hostReport) {
//                    boolean hasUnencrypted = false;
//                    boolean hasEncrypted = false;
//
//                    for (int port : hostReport.getPorts()) {
//                        hasUnencrypted |= (PORT_NO_TLS == port);
//                        hasEncrypted |= (PORT_TLS == port);
//                    }
//
//                    System.out.println("IEC answer:");
//                    System.out.println("\thasUnencrypted: " + (hasUnencrypted ? "TRUE" : "FALSE"));
//                    System.out.println("\thasEncrypted: " + (hasEncrypted ? "TRUE" : "FALSE"));
//                    m_handler.iecReceiveAnswer(hasUnencrypted || hasEncrypted);
//                }
//            });
        return true;
    }

    private class GetIpTask extends AsyncTask<Void, Integer, String> {
        protected void onPostExecute(String result) {
            System.out.println("IP address is: " + result);
        }

        // Credit to https://stackoverflow.com/a/14541376/11407115
        protected String doInBackground(Void... voids) {
            BufferedReader in = null;
            try {
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));
                String ip = in.readLine();
                return ip;
            } catch (Exception e) {
                e.printStackTrace();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                return null;
            }
        }
    }
}
