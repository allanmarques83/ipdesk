package server;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;

import resources.Constants;

public class SSLServerCert
{
    public static SSLServerSocket getServerSocketWithCert(String path_to_cert, 
        String password_cert) throws 
            IOException,
            KeyManagementException, 
            NoSuchAlgorithmException, 
            CertificateException, 
            KeyStoreException, 
            UnrecoverableKeyException 
    {
        InputStream pathStream = SSLServerCert.class.getResourceAsStream(
            path_to_cert);
            
        X509TrustManager[] trust_manager;
        X509KeyManager[] key_manager;
        
        KeyStore key_store = KeyStore.getInstance("JKS");
        key_store.load(pathStream, password_cert.toCharArray());

        trust_manager = get_trust_manager(key_store);
        key_manager = get_key_manager(key_store, password_cert);

        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(key_manager, trust_manager, null);

        SSLServerSocketFactory socketFactory = 
            (SSLServerSocketFactory) ctx.getServerSocketFactory();

        SSLServerSocket ssocket = 
            (SSLServerSocket) socketFactory.createServerSocket();
        
        ssocket.setReceiveBufferSize(Constants.MAX_BYTES_SEND);
        
        return ssocket;
    }

    private static X509TrustManager[] get_trust_manager(KeyStore keystore) 
        throws 
        NoSuchAlgorithmException, 
        KeyStoreException 
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
        System.out.println("sasa");
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