package com.nokia.oss.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.AliasOrIndex.Index;
import org.elasticsearch.common.io.stream.ReleasableBytesStreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Hello World!" );
       new App().launch();
//        XContentBuilder builder =  XContentFactory.jsonBuilder().startObject().array("test", "1","2","3").field("date", new Date()).endObject();
//        System.out.println(builder.string());
//        System.out.println(XContentFactory.jsonBuilder().startArray().startObject().array("test", "1","2","3").field("test", true).endObject().startObject().array("test1", "11","22","33").field("test1", true).endObject().endArray().string());
//        
//        System.out.println(XContentFactory.jsonBuilder().startArray().value("1").value("2").endArray().string());
//        System.out.println(XContentFactory.jsonBuilder().startObject()
//				.array("test", "5", "6").field("name", "123456789").field("a", "88888888").startObject("test").field("test", "88888").endObject().endObject().string());
    }
    
    
    public void launch() throws IOException, InterruptedException, ExecutionException{
    	
    	//it will only find data node,not include master node.
//    	Settings settings = Settings.builder().put("client.transport.sniff", true).build();
    	
    	//multiple address, and round robin to send request.
    	Settings settings =Settings.EMPTY;
    	TransportClient client = new PreBuiltTransportClient(settings)
    	        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.69.182.142"), 9300));
//    	        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));

    	client.connectedNodes().forEach(e->{
    		System.out.println(e.getHostName());
    		System.out.println(e.getAddress());
    		System.out.println(e.getId()+"::"+e.getVersion()+"::"+e.getEphemeralId());
    	});
    	
    	System.out.println("======search=====");
    	search(client);
    	
    	System.out.println("======index=====");
    	index(client);
    	System.out.println("======get=====");
    	get(client);
    	System.out.println("======update=====");
    	update(client);
    	System.out.println("======bulk=====");
    	bulk(client);
    	System.out.println("======mutiGet=====");
    	mutiGet(client);
    	System.out.println("======delete=====");
    	delete(client);
   	// on shutdown
    	client.close();
    	
    }
    
    private void search(TransportClient client) {
		SearchResponse res = client.prepareSearch("a").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery("test", "110"))//Query
				.setPostFilter(QueryBuilders.rangeQuery("date").from("2016-11-10").to("2016-11-11")) // Filter
				.setFrom(0).setSize(60).setExplain(true).get();
//    	SearchResponse res = client.prepareSearch().execute().actionGet();
    	res.getHits().forEach(e->{
    		System.out.println(e.getIndex()+":"+e.getType()+":"+e.getId());
    		System.out.println(e.getSourceAsString());
    	});
		
	}


	private void mutiGet(TransportClient client) {
    	MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
    		    .add("a", "b", "c","c1", "c2", "c3","c5") 
    		    .add("a", "b")          
    		    .get();

    		for (MultiGetItemResponse itemResponse : multiGetItemResponses) { 
    		    GetResponse response = itemResponse.getResponse();
    		    if (response.isExists()) {                      
    		        String json = response.getSourceAsString();
    		        System.out.println(response.getIndex()+" "+response.getType()+" "+response.getId());
    		        System.out.println(json);
    		    }
    		}
	}


	private void bulk(TransportClient client) {
		
		BulkResponse response = client.prepareBulk()
				.add(client.prepareIndex("a", "b","c5").setSource("a", "b"))
				.add(client.prepareUpdate("a", "b", "c2").setScript(new Script("ctx._source.obj.test=\"110\"",ScriptType.INLINE,null,null))).get();
		System.out.println(response.getTook());
		if(response.hasFailures()){
			response.forEach(e->{
				System.out.println(e.getIndex()+" "+e.getType()+" "+e.getId()+" "+e.getFailureMessage());
			});
		}
	}


	private void update(TransportClient client) throws IOException, InterruptedException, ExecutionException {

		/**
		 * normal update, it must be existing index, otherwise will throws an exception.
		 */
//		UpdateRequest updateRequest = new UpdateRequest("a", "b", "c2")
//		        .doc(XContentFactory.jsonBuilder().startObject().array("test", "5","6").field("name","123456789").field("a", "88888888").endObject());
//		UpdateResponse res = client.update(updateRequest).get();
		
		
		/**
		 * upsert means if no index existing, then add it, otherwise update.
		 */
//		IndexRequest indexRequest = new IndexRequest("index", "type", "1")
//		        .source(XContentFactory.jsonBuilder()
//		            .startObject()
//		                .field("name", "Joe Smith")
//		                .field("gender", "male")
//		            .endObject());
//		UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
//		        .doc(XContentFactory.jsonBuilder()
//		            .startObject()
//		                .field("gender", "male")
//		            .endObject())
//		        .upsert(indexRequest); 
//		client.update(updateRequest);
		
		delete(client);
		UpdateResponse res = client.prepareUpdate("a", "b", "c2").setDoc(XContentFactory.jsonBuilder().startObject()
				.array("test", "5", "6").field("name", "123456789").field("a", "88888888").startObject("obj").field("test", "88888").endObject().endObject()).setDocAsUpsert(true).get();

		System.out.println(res.getResult());
		get(client);
	}


	private void delete(TransportClient client) {
		DeleteResponse res = client.prepareDelete("a", "b", "c2").get();
		System.out.println(res.getIndex()+" "+res.getType()+" "+res.getId()+" "+res.getResult());
		get(client);
	}


	private void get(TransportClient client) {
		GetResponse res = client.prepareGet("a","b","c2").get();
		System.out.println(res.getSourceAsString());
	}


	public void index(TransportClient client) throws IOException{
//    	IndexResponse index = client.prepareIndex("a","b","c1").setSource(XContentFactory.jsonBuilder().startObject().array("test", "1","2","3").field("date", new Date()).endObject()).get();
//    	System.out.println(index.getIndex()+" "+index.getType()+" "+index.getId()+" "+index.getResult());
		IndexResponse index1 = client.prepareIndex("a", "b", "c2").setSource(XContentFactory.jsonBuilder().startObject()
				.array("test", "1", "2", "3").startObject("obj").field("name", "zg").field("test", "0000").endObject().field("a", "123").field("date", new Date()).endObject()).get();
		System.out
				.println(index1.getIndex() + " " + index1.getType() + " " + index1.getId() + " " + index1.getResult());
		get(client);
    }
    
    
}
