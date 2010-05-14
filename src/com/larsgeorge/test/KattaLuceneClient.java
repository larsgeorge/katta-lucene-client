package com.larsgeorge.test;

import net.sf.katta.lib.lucene.Hit;
import net.sf.katta.lib.lucene.Hits;
import net.sf.katta.lib.lucene.LuceneClient;
import net.sf.katta.util.ZkConfiguration;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import java.util.Arrays;
import java.util.Map;

public class KattaLuceneClient {

  public static void main(String[] args) {
    try {
      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
      Query query = new QueryParser(Version.LUCENE_CURRENT, args[1], analyzer).parse(args[2]);

      // assumes "/katta.zk.properties" available on classpath!
      ZkConfiguration conf = new ZkConfiguration();
      LuceneClient luceneClient = new LuceneClient(conf);
      Hits hits = luceneClient.search(query, Arrays.asList(args[0]).toArray(new String[1]), 99);

      int num = 0;
      for (Hit hit : hits.getHits()) {
        MapWritable mw = luceneClient.getDetails(hit);
        for (Map.Entry<Writable, Writable> entry : mw.entrySet()) {
          System.out.println("[" + (num++) + "] key -> " + entry.getKey() + ", value -> " + entry.getValue());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
