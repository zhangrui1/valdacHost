package com.toyo.vh.controller;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Map;

/**
 * Created by zhangrui on 15/01/14.
 * 画像パスから画像を取得し、PDFに保存する
 */
public class Pdfview extends AbstractIText5PdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document, PdfWriter writer, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        Paragraph header = new Paragraph(new Chunk(""));
        document.add(header);

        String bucket="";
        if(!(model.isEmpty())){
                for(String key : model.keySet()){
                    if("id".equals(key)){
                    }else{
//                    URL path=new URL("http://storage.googleapis.com/valdac-aisa/"+key);
                        String path=getImagePath(key,"valdac-aisa");
                        //図面Pathを取得
                        Image img=Image.getInstance(path);
                        //画像をA4サイズ(210*297)にする,元サイズは1238*1751
                        img.scaleAbsolute(605,856);
                        document.add(img);
                    }
                }
        }

    }

    //Image Pathを取得する
    final String SERVICE_ACCOUNT_EMAIL = "13771198627-1rlqtf8o7v4531gp3fo65kd5k5h7f3eo@developer.gserviceaccount.com";
    final String SERVICE_ACCOUNT_PKCS12_FILE_PATH = "power-science-20140719001-7025cff7beb0.p12";
    PrivateKey key;
    public String getImagePath(String OBJECT_NAME,String BUCKET_NAME) throws Exception {
        long expiration = System.currentTimeMillis()/1000 + 600;
        key = loadKeyFromPkcs12(SERVICE_ACCOUNT_PKCS12_FILE_PATH, "notasecret".toCharArray());
        System.out.println("======= GET File =========");
        String get_url = this.getSigningURL("GET",OBJECT_NAME,expiration,BUCKET_NAME);
        return get_url;
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
        return new String(com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64(rawSignature, false), "UTF-8");
    }
}
