package com.toyo.vh.controller;


import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

/**
 * Created by Lsr on 11/26/14.
 */

@Controller
@RequestMapping("/SingUrlTest")
public class SingUrlTest {

    final String SERVICE_ACCOUNT_EMAIL = "13771198627-1rlqtf8o7v4531gp3fo65kd5k5h7f3eo@developer.gserviceaccount.com";
    final String SERVICE_ACCOUNT_PKCS12_FILE_PATH = "power-science-20140719001-7025cff7beb0.p12";
//    final long expiration = System.currentTimeMillis()/1000 + 600;

//    final String BUCKET_NAME = "valdac-construction-aisa";

    PrivateKey key;
    /**
     * 点検ランクを更新
     *
     * @return  点検ランク設定完了フラグ
     * */
    @RequestMapping(value = "/SignTest", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveTenkenrank(@RequestParam("OBJECT_NAME")String OBJECT_NAME,@RequestParam("BUCKET_NAME")String BUCKET_NAME,
                                 HttpSession session) throws IOException {

        try {
            long expiration = System.currentTimeMillis()/1000 + 600;
            key = loadKeyFromPkcs12(SERVICE_ACCOUNT_PKCS12_FILE_PATH, "notasecret".toCharArray());
            System.out.println("======= GET File =========");
            String get_url = this.getSigningURL("GET",OBJECT_NAME,expiration,BUCKET_NAME);
//            URL url = new URL(get_url);
//            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//            httpCon.setRequestMethod("GET");
            System.out.println("GET Request URL " + get_url);
//            System.out.println("GET Response code: " + httpCon.getResponseCode()+" message: "+httpCon.getResponseMessage());
//            renderResponse(httpCon.getInputStream());
            return get_url;
        }
        catch (Exception ex) {
            System.out.println("Error : " + ex);
        }
        return "";
    }
    private void renderResponse(InputStream is) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }

    private String getSigningURL(String verb,String OBJECT_NAME,long expiration,String BUCKET_NAME) throws Exception {
        String url_signature = this.signString(verb + "\n\n\n" + expiration + "\n" + "/" + BUCKET_NAME + "/" + OBJECT_NAME  );
        String signed_url = "http://storage.googleapis.com/" + BUCKET_NAME + "/" + OBJECT_NAME +
                "?GoogleAccessId=" + SERVICE_ACCOUNT_EMAIL +
                "&Expires=" + expiration +
                "&Signature=" + URLEncoder.encode(url_signature, "UTF-8");
        return signed_url;
    }

    private static PrivateKey loadKeyFromPkcs12(String filename, char[] password) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(fis, password);
        return (PrivateKey) ks.getKey("privatekey", password);
    }

    private String signString(String stringToSign) throws Exception {
        if (key == null)
            throw new Exception("Private Key not initalized");
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(key);
        signer.update(stringToSign.getBytes("UTF-8"));
        byte[] rawSignature = signer.sign();
        return new String(Base64.encodeBase64(rawSignature, false), "UTF-8");
    }

}
