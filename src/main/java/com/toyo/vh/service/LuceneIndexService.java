package com.toyo.vh.service;

import com.google.gson.Gson;
import com.toyo.vh.dao.*;
import com.toyo.vh.dto.SearchResultObject;
import com.toyo.vh.entity.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.lucene.util.Version.LUCENE_48;

/**
 * Created by Lsr on 10/29/14.
 */

@Service
public class LuceneIndexService {

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

    //Valve 初期化
    private void initDocValve(IndexWriter w) throws IOException{
        List<Valve> valveList = itemMapper.findAllValve();

        for(int i = 0;i < valveList.size();i++){
            Document doc = new Document();
            doc.add(new StringField("id", "VA"+valveList.get(i).getKikiSysId()+"End", Field.Store.YES));
            doc.add(new TextField("body", valveList.get(i).toText(), Field.Store.YES));
            w.addDocument(doc);
        }
    }

    //Kiki初期化
    private void initDocKiki(IndexWriter w) throws IOException{
        List<Kiki> kikiList = itemMapper.findAllKiki();

        for(int i = 0;i < kikiList.size();i++){
            Document doc = new Document();
            doc.add(new StringField("id", "KI"+kikiList.get(i).getKikiId()+"End", Field.Store.YES));
            doc.add(new TextField("body", kikiList.get(i).toText(), Field.Store.YES));
            w.addDocument(doc);
        }
    }

    //Buhin初期化
    private void initDocBuhin(IndexWriter w) throws IOException{
        List<Buhin> buhinList = itemMapper.findAllBuhin();

        for(int i = 0;i < buhinList.size();i++){
            Document doc = new Document();
            doc.add(new StringField("id", "BU"+buhinList.get(i).getBuhinId()+"End", Field.Store.YES));
            doc.add(new TextField("body", buhinList.get(i).toText(), Field.Store.YES));
            w.addDocument(doc);
        }
    }

    //Valve,Kiki,buhinの関係lucene初期化
    private void initDocKikisystemrelation(IndexWriter w) throws IOException {
        List<Kikisystemrelation> kikisystemrelationList = itemMapper.findAllKikisystemrelation();

        for(int i = 0;i < kikisystemrelationList.size();i++){
            Document doc = new Document();
            doc.add(new StringField("id", "VAKIRE"+kikisystemrelationList.get(i).getId()+"End", Field.Store.YES));
            doc.add(new TextField("body", kikisystemrelationList.get(i).toText(), Field.Store.YES));
            w.addDocument(doc);
        }

    }
    private void initDocKouji(IndexWriter w) throws IOException{
        List<Kouji> koujiList=koujiMapper.findAllKouji();

        for(int i = 0;i < koujiList.size();i++){
            Document doc = new Document();
            doc.add(new StringField("id", "KJ"+koujiList.get(i).getId()+"End", Field.Store.YES));
            doc.add(new TextField("body", koujiList.get(i).toText(), Field.Store.YES));
            w.addDocument(doc);
        }
    }

    private void initDocKoujirelation(IndexWriter w) throws IOException{
        Gson gson=new Gson();
        List<Koujirelation> koujirelationList=koujirelationMapper.findAllKoujirelation();

        for(int i = 0;i < koujirelationList.size();i++){
            Document doc = new Document();
            doc.add(new StringField("id", "KR"+koujirelationList.get(i).getId()+"End", Field.Store.YES));
            doc.add(new TextField("body", koujirelationList.get(i).toText(), Field.Store.YES));
            w.addDocument(doc);
        }
    }


    private void initDocTenkenRireki(IndexWriter w) throws IOException{
        Gson gson=new Gson();
        List<TenkenRireki> tenkenRirekiList=tenkenRirekiMapper.findAllTenkenRireki();

        for(int i = 0;i < tenkenRirekiList.size();i++){
            Document doc = new Document();
            doc.add(new StringField("id", "TR"+tenkenRirekiList.get(i).getId()+"End", Field.Store.YES));
            doc.add(new TextField("body", tenkenRirekiList.get(i).toText(), Field.Store.YES));
            w.addDocument(doc);
        }

    }

    private void initDocKenan(IndexWriter w) throws IOException{
        Gson gson=new Gson();
        List<Kenan> kenanList=kenanMapper.findAllKenan();

        for(int i = 0;i < kenanList.size();i++){
            Document doc = new Document();
            doc.add(new StringField("id", "KEN"+kenanList.get(i).getId()+"End", Field.Store.YES));
            doc.add(new TextField("body", kenanList.get(i).toText(), Field.Store.YES));
            w.addDocument(doc);
        }

    }

    private void initDocKoujiIDTenkenRireki(IndexWriter w) throws IOException{
        Gson gson=new Gson();
        List<TenkenRireki> tenkenRirekiList=tenkenRirekiMapper.findAllTenkenRireki();
        for(int i = 0;i < tenkenRirekiList.size();i++){
            Document doc = new Document();
            doc.add(new StringField("id", "TRKJID"+tenkenRirekiList.get(i).getKoujiId()+"End", Field.Store.YES));
            doc.add(new TextField("body", tenkenRirekiList.get(i).toText(), Field.Store.YES));
            w.addDocument(doc);
        }
    }

    private void initDoc(IndexWriter w) throws IOException {
//
//        Gson gson=new Gson();
//
//
//        long begintime=System.currentTimeMillis();
//        List<Valve> valveList = itemMapper.findAllValve();
//
//        for(int i = 0;i < valveList.size();i++){
//            Document doc = new Document();
//            doc.add(new TextField("id", "VA"+valveList.get(i).getKikiSysId()+"End", Field.Store.YES));
//            doc.add(new TextField("body", valveList.get(i).toText(), Field.Store.YES));
//            w.addDocument(doc);
//        }
//        long valvetime=System.currentTimeMillis();
//        System.out.println("valvetime   ="+(valvetime-begintime));
//
//        List<Kiki> kikiList = itemMapper.findAllKiki();
//
//        for(int i = 0;i < kikiList.size();i++){
//            Document doc = new Document();
//            doc.add(new TextField("id", "KI"+kikiList.get(i).getKikiId()+"End", Field.Store.YES));
//            doc.add(new TextField("body", kikiList.get(i).toText(), Field.Store.YES));
//            w.addDocument(doc);
//        }
//        long kikitime=System.currentTimeMillis();
//        System.out.println("kikitime   ="+(kikitime-valvetime));
//
//        List<Buhin> buhinList = itemMapper.findAllBuhin();
//
//        for(int i = 0;i < buhinList.size();i++){
//            Document doc = new Document();
//            doc.add(new TextField("id", "BU"+buhinList.get(i).getBuhinId()+"End", Field.Store.YES));
//            doc.add(new TextField("body", buhinList.get(i).toText(), Field.Store.YES));
//            w.addDocument(doc);
//        }
//
//        long buhintime=System.currentTimeMillis();
//        System.out.println("buhintime   ="+(buhintime-kikitime));
//
//        List<Kouji> koujiList=koujiMapper.findAllKouji();
//
//        for(int i = 0;i < koujiList.size();i++){
//            Document doc = new Document();
//            doc.add(new TextField("id", "KJ"+koujiList.get(i).getId()+"End", Field.Store.YES));
//            doc.add(new TextField("body", koujiList.get(i).toText(), Field.Store.YES));
//            w.addDocument(doc);
//        }
//
//        long koujitime=System.currentTimeMillis();
//        System.out.println("koujitime   ="+(koujitime-buhintime));
//
//        List<Koujirelation> koujirelationList=koujirelationMapper.findAllKoujirelation();
//
//        for(int i = 0;i < koujirelationList.size();i++){
//            Document doc = new Document();
//            doc.add(new TextField("id", "KR"+koujirelationList.get(i).getId()+"End", Field.Store.YES));
//            doc.add(new TextField("body", koujirelationList.get(i).toText(), Field.Store.YES));
//
//            w.addDocument(doc);
//        }
//
//        long koujirelatime=System.currentTimeMillis();
//        System.out.println("koujirelatime   ="+(koujirelatime-koujitime));
//
//        List<TenkenRireki> tenkenRirekiList=tenkenRirekiMapper.findAllTenkenRireki();
//
//        for(int i = 0;i < tenkenRirekiList.size();i++){
//            Document doc = new Document();
//            doc.add(new TextField("id", "TR"+tenkenRirekiList.get(i).getId()+"End", Field.Store.YES));
//            doc.add(new TextField("body", tenkenRirekiList.get(i).toText(), Field.Store.YES));
//
//            w.addDocument(doc);
//        }
//        long tenkenRirekiTime=System.currentTimeMillis();
//        System.out.println("tenkenRirekiTime   ="+(tenkenRirekiTime-koujirelatime));
//
//        for(int i = 0;i < tenkenRirekiList.size();i++){
//            Document doc = new Document();
//            doc.add(new TextField("id", "TRKJID"+tenkenRirekiList.get(i).getKoujiId()+"End", Field.Store.YES));
//            doc.add(new TextField("body", tenkenRirekiList.get(i).toText(), Field.Store.YES));
//
//            w.addDocument(doc);
//        }
//        long tenkenRirekiByKoujiTime=System.currentTimeMillis();
//        System.out.println("tenkenRirekiTimeByKoujiId   ="+(tenkenRirekiByKoujiTime-tenkenRirekiTime));
    }

    public void insertRecord(Directory index,String id, String body){
        StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
        IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);
        IndexWriter w = null;

        try {
            Document doc = new Document();
            doc.add(new StringField("id", id+"", Field.Store.YES));
            doc.add(new TextField("body", body, Field.Store.YES));
            w = new IndexWriter(index, config);
            w.addDocument(doc);
            w.close();
        } catch (IOException e) {
            System.out.println("add document failed");
            e.printStackTrace();
        }
    }

    public void deleteRecord(Directory index,String id){
        StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
        IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);
        IndexWriter w = null;
        try {
            Document doc = new Document();
            w = new IndexWriter(index, config);
            w.deleteDocuments(new Term("id",id));
            w.close();
        } catch (IOException e) {
            System.out.println("delete document failed");
            e.printStackTrace();
        }

    }
    public void updateRecord(Directory index,String id, String body){
        deleteRecord(index,id);
        insertRecord(index,id,body);
    }

    public List<SearchResultObject> selectRecordFormId(Directory index,String keywords){
        List<SearchResultObject> resultObjectList = new ArrayList<SearchResultObject>();
        StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);

        String tmpKeyword = "";

        String words[] = keywords.split(" ");

        for(int i = 0;i<words.length;i++){

            //trim
            if(words[i].length()<1)
                continue;

            words[i] = words[i].replace("-"," AND ");
            words[i] = words[i].replace("/"," AND ");

            //judge wheather it is String
            if(words[i].charAt(0) != '0'){
                try{
                    Integer.valueOf(words[i]);
                    words[i] = words[i] + "*";
                } catch (Exception e){
                }
            }

            if(i == words.length-1){
                tmpKeyword = tmpKeyword + words[i];
            } else {
                tmpKeyword = tmpKeyword + words[i] + " AND ";
            }
        }

        String querystr = tmpKeyword;

        Query query = null;
        QueryParser queryParser = new QueryParser(LUCENE_48,"id", analyzer);
        try {
            query = queryParser.parse(querystr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 3. search
        //search　result num
        int hitsPerPage = 100000;
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(index);
        } catch (IOException e) {
            System.out.println("can not find index");
            e.printStackTrace();
        }
        IndexSearcher searcher = new IndexSearcher(reader);

        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        try {
            searcher.search(query, collector);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document document = null;
            try {
                document = searcher.doc(docId);

            } catch (IOException e) {
                e.printStackTrace();
            }
            SearchResultObject tmpObj = new SearchResultObject();
            tmpObj.setId(document.get("id"));
            tmpObj.setBody(document.get("body"));
            resultObjectList.add(tmpObj);
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultObjectList;
    }

    public List<SearchResultObject> selectRecord(Directory index,String keywords){
        List<SearchResultObject> resultObjectList = new ArrayList<SearchResultObject>();
        StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);

        String tmpKeyword = "";
        keywords=keywords.replace("/"," ");//"/がエラーになるので、スペースに変換
        String words[] = keywords.split(" ");

        for(int i = 0;i<words.length;i++){

            //trim
            if(words[i].length()<1)
                continue;

            String subwords[] = words[i].split("-");
            //配列の一つ項目を処理後の文字列を保存する
            String tmpSubKeyword = "";
            //ハイフンで分割
            for (int j = 0; j < subwords.length; j++) {
                //英数字のみの場合、"*"をつける；その以外場合は何もしない
                String subStr=subwords[j];
                Pattern p= Pattern.compile("[a-zA-Z0-9]+");
                Matcher m=p.matcher(subStr);
                if(m.find()){
                    subwords[j] = subwords[j] + "*";
                }

                if(j == subwords.length-1){
                    tmpSubKeyword = tmpSubKeyword + subwords[j];
                } else {
                    tmpSubKeyword = tmpSubKeyword + subwords[j] + " AND ";
                }
            }

            if(i == words.length-1){
                tmpKeyword = tmpKeyword + tmpSubKeyword;
            } else {
                tmpKeyword = tmpKeyword + tmpSubKeyword+" AND ";
            }
        }

        String querystr = tmpKeyword;

        Query query = null;
        QueryParser queryParser = new QueryParser(LUCENE_48,"body", analyzer);
        try {
            query = queryParser.parse(querystr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 3. search
        //search　result num
        int hitsPerPage = 100000;
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(index);
        } catch (IOException e) {
            System.out.println("can not find index");
            e.printStackTrace();
        }
        IndexSearcher searcher = new IndexSearcher(reader);

        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        try {
            searcher.search(query, collector);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document document = null;
            try {
                document = searcher.doc(docId);

            } catch (IOException e) {
                e.printStackTrace();
            }
            SearchResultObject tmpObj = new SearchResultObject();
            tmpObj.setId(document.get("id"));
            tmpObj.setBody(document.get("body"));
            resultObjectList.add(tmpObj);
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultObjectList;
    }

    public Map<String,String> generateLocalIndex() {
        //Kouji,Valva,Kiki
        File indexKoujiFile = null;
        //Valva
        File indexValveFile = null;
        //Kiki
        File indexKikiFile = null;
        //buhin
        File indexBuhinFile = null;
        //Koujirelation
        File indexKoujirelationFile=null;
        //tenkenRireki
        File indexTenkenRireki=null;
        //koujiIDTenkenRireki
        File indexKoujiIDTenkenRireki=null;
        //kenan
        File indexKenan=null;

        String folder="index/";

        try {
            //Valve lucene
            indexValveFile =  new File(folder+"indexValveFile");
            if(!indexValveFile.exists()) {
                System.out.println("indexValveFileファイルがない");
                Directory dir = FSDirectory.open(indexValveFile);

                StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
                IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

                IndexWriter w = null;

                w = new IndexWriter(dir, config);
                initDocValve(w);
                w.close();
            }

            //Kiki lucene
            indexKikiFile =  new File(folder+"indexKikiFile");
            if(!indexKikiFile.exists()) {
                Directory dir = FSDirectory.open(indexKikiFile);

                StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
                IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

                IndexWriter w = null;

                w = new IndexWriter(dir, config);
                initDocKiki(w);
                w.close();
            }

            //Buhin lucene
            indexBuhinFile =  new File(folder+"indexBuhinFile");
            if(!indexBuhinFile.exists()) {
                Directory dir = FSDirectory.open(indexBuhinFile);

                StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
                IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

                IndexWriter w = null;

                w = new IndexWriter(dir, config);
                initDocBuhin(w);
                w.close();
            }

            indexKoujiFile =  new File(folder+"indexKoujiFile");
            if(!indexKoujiFile.exists()) {
                Directory dir = FSDirectory.open(indexKoujiFile);

                StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
                IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

                IndexWriter w = null;

                w = new IndexWriter(dir, config);
                initDocKouji(w);
                w.close();
            }

            indexKoujirelationFile =  new File(folder+"indexKoujirelationFile");
            if(!indexKoujirelationFile.exists()) {
                Directory dir = FSDirectory.open(indexKoujirelationFile);

                StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
                IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

                IndexWriter w = null;

                w = new IndexWriter(dir, config);
                initDocKoujirelation(w);
                w.close();
            }

            indexTenkenRireki =  new File(folder+"indexTenkenRireki");
            if(!indexTenkenRireki.exists()) {
                Directory dir = FSDirectory.open(indexTenkenRireki);

                StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
                IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

                IndexWriter w = null;

                w = new IndexWriter(dir, config);
                initDocTenkenRireki(w);
                w.close();
            }

            indexKoujiIDTenkenRireki =  new File(folder+"indexKoujiIDTenkenRireki");
            if(!indexKoujiIDTenkenRireki.exists()) {
                Directory dir = FSDirectory.open(indexKoujiIDTenkenRireki);

                StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
                IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

                IndexWriter w = null;

                w = new IndexWriter(dir, config);
                initDocKoujiIDTenkenRireki(w);
                w.close();
            }

            indexKenan =  new File(folder+"indexKenan");
            if(!indexKenan.exists()) {
                Directory dir = FSDirectory.open(indexKenan);

                StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
                IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

                IndexWriter w = null;

                w = new IndexWriter(dir, config);
                initDocKenan(w);
                w.close();
            }


        } catch (IOException e) {
            System.out.println("Index create failed");
            e.printStackTrace();
        }

        //directory初期化
        Map<String,String> indexPath = new HashMap<String, String>();
        indexPath.put("indexValveFile",indexValveFile.getAbsolutePath());
        indexPath.put("indexKikiFile",indexKikiFile.getAbsolutePath());
        indexPath.put("indexBuhinFile",indexBuhinFile.getAbsolutePath());
        indexPath.put("indexKoujiFile",indexKoujiFile.getAbsolutePath());
        indexPath.put("indexKoujirelationFile",indexKoujirelationFile.getAbsolutePath());
        indexPath.put("indexTenkenRireki",indexTenkenRireki.getAbsolutePath());
        indexPath.put("indexKoujiIDTenkenRireki",indexKoujiIDTenkenRireki.getAbsolutePath());
        indexPath.put("indexKenan",indexKenan.getAbsolutePath());

        return indexPath;
    }

    public void remakeIndex() {
        File indexFile = null;
        try {
            indexFile =  new File("indexKoujiDir");
            Directory dir = FSDirectory.open(indexFile);

            StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
            IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

            IndexWriter w = null;

            w = new IndexWriter(dir, config);
            initDoc(w);
            w.close();
            System.out.println(indexFile.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("Index remake failed");
            e.printStackTrace();
        }

    }

    public List<SearchResultObject> makeHightLight(String keyword, List<SearchResultObject> tmpResults) {
        List<SearchResultObject> results = new ArrayList<SearchResultObject>();

        String keywords[] = keyword.split(" ");
        for (int i = 0; i < tmpResults.size(); i++) {
            SearchResultObject tmpSRO = tmpResults.get(i);
            String body = tmpSRO.getBody();
            boolean contains = false;
            for (int j = 0; j < keywords.length; j++) {
                //trim
                if(keywords[j].length()<1) {
                    continue;
                } else {
                    String parts[] = body.split(keywords[j]);
                    if(parts.length < 2){
                        //不包含
                        continue;
                    } else {
                        body = body.replace(keywords[j],"$"+keywords[j]+">?");
                        contains = true;
                    }
                }
            }
            if(contains){
                body = body.replace("$","<font color='red'>");
                body = body.replace(">?","</font>");

                tmpSRO.setBody(body);
                results.add(tmpSRO);
            }
        }

        return results;
    }
}
