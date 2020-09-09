import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;


public class SSLCert {

    public static SSLSocket getSocketWithCert(
            InetAddress ip, 
            int port, 
            String pathToCert, 
            String passwordFromCert) throws Exception {

		InputStream pathStream = Main.class.getResourceAsStream(pathToCert);//new FileInputStream(pathToCert);
		X509TrustManager[] tmm;
        X509KeyManager[] key_manager;

		KeyStore ks  = KeyStore.getInstance("jks");
		ks.load(pathStream, passwordFromCert.toCharArray());
		tmm=tm(ks);
		key_manager = get_key_manager(ks, passwordFromCert);
		SSLContext ctx = SSLContext.getInstance("SSL");
		ctx.init(key_manager, tmm, null);

		SSLSocketFactory SocketFactory = (SSLSocketFactory) ctx.getSocketFactory();
		SSLSocket socket = (SSLSocket) SocketFactory.createSocket(ip, port);
		return socket;
    }
    
	private static X509TrustManager[] tm(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException 
	{
		TrustManagerFactory trustMgrFactory = TrustManagerFactory.getInstance("SUNX509");
        trustMgrFactory.init(keystore);
        TrustManager trustManagers[] = trustMgrFactory.getTrustManagers();
        for (int i = 0; i < trustManagers.length; i++) 
		{
            if (trustManagers[i] instanceof X509TrustManager) 
			{
            	X509TrustManager[] tr = new X509TrustManager[1];
            	tr[0] = (X509TrustManager) trustManagers[i];
                return tr;
            }
        }
        return null;
	}
	
	private static X509KeyManager[] get_key_manager(KeyStore keystore, String 
        password) 
            throws 
            NoSuchAlgorithmException, 
		    KeyStoreException, 
		    UnrecoverableKeyException {

		KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance("SunX509");

        keyMgrFactory.init(keystore, password.toCharArray());

        KeyManager keyManagers[] = keyMgrFactory.getKeyManagers();

        for (int i = 0; i < keyManagers.length; i++) 
		{
            if (keyManagers[i] instanceof X509KeyManager) 
			{
            	X509KeyManager[] kr = new X509KeyManager[1];
            	kr[0] = (X509KeyManager) keyManagers[i];
                return kr;
            }
        }
        System.out.println("sasa");
        return null;
    }
    
}