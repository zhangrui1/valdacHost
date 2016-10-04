package com.toyo.vh.controller;

import com.toyo.vh.dao.*;
import com.toyo.vh.entity.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by 維瑞 on 2015/07/03.
 */
@Controller
@RequestMapping("/lucene")
public class LuceneController {

    @Resource
    ItemMapper itemMapper;
    @Resource
    TenkenRirekiMapper tenkenRirekiMapper;
    @Resource
    KoujiMapper koujiMapper;
    @Resource
    KoujirelationMapper koujirelationMapper;
    @Resource
    KenanMapper kenanMapper;

    /** 弁Lucene作成*/
    @RequestMapping(value = "/luceneValve", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String luceneValve() throws IOException {
        //初期化
        String folder="index/";
        //Kouji,Valva,Kiki
        File indexValveFile = null;

        //Valve lucene
        indexValveFile =  new File(folder+"indexValveFile");
        if(!indexValveFile.exists()) {
            Directory dir = FSDirectory.open(indexValveFile);

            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);

            IndexWriter w = null;

            w = new IndexWriter(dir, config);
            List<Valve> valveList = itemMapper.findAllValve();

            for(int i = 0;i < valveList.size();i++){
                Document doc = new Document();
                doc.add(new StringField("id", "VA"+valveList.get(i).getKikiSysId()+"End", Field.Store.YES));
                doc.add(new TextField("body", valveList.get(i).toText(), Field.Store.YES));
                w.addDocument(doc);
            }

            w.close();
        }

       return "lucene";
    }
    /** 弁機器Lucene作成*/
    @RequestMapping(value = "/luceneKiki", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String luceneKiki() throws IOException {
        //初期化
        String folder="index/";
        //Kouji,Valva,Kiki
        File indexKikiFile = null;

        //Valve lucene
        indexKikiFile =  new File(folder+"indexKikiFile");
        if(!indexKikiFile.exists()) {
            Directory dir = FSDirectory.open(indexKikiFile);

            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);

            IndexWriter w = null;

            w = new IndexWriter(dir, config);
            List<Kiki> kikiList = itemMapper.findAllKiki();

            for(int i = 0;i < kikiList.size();i++){
                Document doc = new Document();
                doc.add(new StringField("id", "KI"+kikiList.get(i).getKikiId()+"End", Field.Store.YES));
                doc.add(new TextField("body", kikiList.get(i).toText(), Field.Store.YES));
                w.addDocument(doc);
            }

            w.close();
        }

        return "lucene";
    }
    /** 弁部品Lucene作成*/
    @RequestMapping(value = "/luceneBuhin", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String luceneBuhin() throws IOException {
        //初期化
        String folder="index/";
        //Kouji,Valva,Kiki
        File indexBuhinFile = null;

        //Valve lucene
        indexBuhinFile =  new File(folder+"indexBuhinFile");
        if(!indexBuhinFile.exists()) {
            Directory dir = FSDirectory.open(indexBuhinFile);

            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);

            IndexWriter w = null;

            w = new IndexWriter(dir, config);
            List<Buhin> buhinList = itemMapper.findAllBuhin();

            for(int i = 0;i < buhinList.size();i++){
                Document doc = new Document();
                doc.add(new StringField("id", "BU"+buhinList.get(i).getBuhinId()+"End", Field.Store.YES));
                doc.add(new TextField("body", buhinList.get(i).toText(), Field.Store.YES));
                w.addDocument(doc);
            }

            w.close();
        }

        return "lucene";
    }
    /** 工事Lucene作成*/
    @RequestMapping(value = "/luceneKouji", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String luceneKouji() throws IOException {
        //初期化
        String folder="index/";
        //Kouji,Valva,Kiki
        File indexKoujiFile = null;

        indexKoujiFile =  new File(folder+"indexKoujiFile");
        if(!indexKoujiFile.exists()) {
            Directory dir = FSDirectory.open(indexKoujiFile);

            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);

            IndexWriter w = null;

            w = new IndexWriter(dir, config);
            List<Kouji> koujiList=koujiMapper.findAllKouji();

            for(int i = 0;i < koujiList.size();i++){
                Document doc = new Document();
                doc.add(new StringField("id", "KJ"+koujiList.get(i).getId()+"End", Field.Store.YES));
                doc.add(new TextField("body", koujiList.get(i).toText(), Field.Store.YES));
                w.addDocument(doc);
            }
            w.close();
        }

        return "lucene";
    }
    /** 工事relation Lucene作成*/
    @RequestMapping(value = "/luceneKoujirelation", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String luceneKoujirelation() throws IOException {
        //初期化
        String folder="index/";
        //Kouji,Valva,Kiki
        File indexKoujirelationFile = null;

        indexKoujirelationFile =  new File(folder+"indexKoujirelationFile");
        if(!indexKoujirelationFile.exists()) {
            Directory dir = FSDirectory.open(indexKoujirelationFile);

            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);

            IndexWriter w = null;

            w = new IndexWriter(dir, config);
            List<Koujirelation> koujirelationList=koujirelationMapper.findAllKoujirelation();

            for(int i = 0;i < koujirelationList.size();i++){
                Document doc = new Document();
                doc.add(new StringField("id", "KR"+koujirelationList.get(i).getId()+"End", Field.Store.YES));
                doc.add(new TextField("body", koujirelationList.get(i).toText(), Field.Store.YES));
                w.addDocument(doc);
            }
            w.close();
        }

        return "lucene";
    }
    /** 工事TenkenRireki Lucene作成*/
    @RequestMapping(value = "/luceneTenkenRireki", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String luceneTenkenRireki() throws IOException {
        //初期化
        String folder="index/";
        //tenkenRireki
        File indexTenkenRireki = null;

        indexTenkenRireki =  new File(folder+"indexTenkenRireki");
        if(!indexTenkenRireki.exists()) {
            Directory dir = FSDirectory.open(indexTenkenRireki);

            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);

            IndexWriter w = null;

            w = new IndexWriter(dir, config);
            List<TenkenRireki> tenkenRirekiList=tenkenRirekiMapper.findAllTenkenRireki();

            for(int i = 0;i < tenkenRirekiList.size();i++){
                Document doc = new Document();
                doc.add(new StringField("id", "TR"+tenkenRirekiList.get(i).getId()+"End", Field.Store.YES));
                doc.add(new TextField("body", tenkenRirekiList.get(i).toText(), Field.Store.YES));
                w.addDocument(doc);
            }
            w.close();
        }

        return "lucene";
    }
    /** 工事TenkenRireki Lucene作成*/
    @RequestMapping(value = "/luceneKoujiIDTenkenRireki", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String luceneKoujiIDTenkenRireki() throws IOException {
        //初期化
        String folder="index/";
        //tenkenRireki
        File indexKoujiIDTenkenRireki = null;

        indexKoujiIDTenkenRireki =  new File(folder+"indexKoujiIDTenkenRireki");
        if(!indexKoujiIDTenkenRireki.exists()) {
            Directory dir = FSDirectory.open(indexKoujiIDTenkenRireki);

            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);

            IndexWriter w = null;

            w = new IndexWriter(dir, config);
            List<TenkenRireki> tenkenRirekiList=tenkenRirekiMapper.findAllTenkenRireki();
            for(int i = 0;i < tenkenRirekiList.size();i++){
                Document doc = new Document();
                doc.add(new StringField("id", "TRKJID"+tenkenRirekiList.get(i).getKoujiId()+"End", Field.Store.YES));
                doc.add(new TextField("body", tenkenRirekiList.get(i).toText(), Field.Store.YES));
                w.addDocument(doc);
            }
            w.close();
        }

        return "lucene";
    }
    /** 工事Kenan Lucene作成*/
    @RequestMapping(value = "/luceneKenan", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String luceneKenan() throws IOException {
        //初期化
        String folder="index/";
        //Kenan
        File indexKenan = null;

        indexKenan =  new File(folder+"indexKenan");
        if(!indexKenan.exists()) {
            Directory dir = FSDirectory.open(indexKenan);

            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);

            IndexWriter w = null;

            w = new IndexWriter(dir, config);
            List<Kenan> kenanList=kenanMapper.findAllKenan();

            for(int i = 0;i < kenanList.size();i++){
                Document doc = new Document();
                doc.add(new StringField("id", "KEN"+kenanList.get(i).getId()+"End", Field.Store.YES));
                doc.add(new TextField("body", kenanList.get(i).toText(), Field.Store.YES));
                w.addDocument(doc);
            }
            w.close();
        }

        return "lucene";
    }
}
