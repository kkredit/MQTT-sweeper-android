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
    private String m_ipAddr;

    InternetExposureChecker(String apiKey, IEC_Handler handler) {
        m_apiHandle = new ShodanRestApi(apiKey);
        m_handler = handler;
    }

    private void setIpAddr(String ip) {
        m_ipAddr = ip;
    }

    private String getIpAddr() {
        return m_ipAddr;
    }

    private ShodanRestApi getApiHandle() {
        return m_apiHandle;
    }

    private IEC_Handler getIecHandler() {
        return m_handler;
    }

    public boolean getExposed() {
        try {
            new GetIpTask().execute(this);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class GetIpTask extends AsyncTask<InternetExposureChecker, Integer, String> {
        protected void onPostExecute(String result) {
            System.out.println("IP address is: " + result);
        }

        // Credit to https://stackoverflow.com/a/14541376/11407115
        protected String doInBackground(InternetExposureChecker... iecs) {
            BufferedReader in = null;
            try {
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));
                String ip = in.readLine();
                iecs[0].setIpAddr(ip);
                new GetShodanScan().execute(iecs);
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

    private class GetShodanScan extends AsyncTask<InternetExposureChecker, Integer, Void> {
        protected void onPostExecute(Boolean result) {
            System.out.println("Is this IP addr hosting MQTT services? " + (result ? "YES" : "NO"));
        }

        protected Void doInBackground(InternetExposureChecker... iecs) {
            InternetExposureChecker iec = iecs[0];
            IEC_Handler handler = iec.getIecHandler();
            iec.getApiHandle().hostByIp(false,true, iec.getIpAddr())
                    .subscribe(new DisposableObserver<Host>() {
                        @Override
                        public void onComplete() {
                            handler.iecOnComplete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            handler.iecOnError(e);
                        }

                        @Override
                        public void onNext(Host hostReport) {
                            boolean hasUnencrypted = false;
                            boolean hasEncrypted = false;

                            for (int port : hostReport.getPorts()) {
                                hasUnencrypted |= (PORT_NO_TLS == port);
                                hasEncrypted |= (PORT_TLS == port);
                            }

                            System.out.println("IEC answer:");
                            System.out.println("\thasUnencrypted: " + (hasUnencrypted ? "TRUE" : "FALSE"));
                            System.out.println("\thasEncrypted: " + (hasEncrypted ? "TRUE" : "FALSE"));
                            handler.iecReceiveAnswer(hasUnencrypted || hasEncrypted);
                        }
                    });
            return null;
        }
    }
}
