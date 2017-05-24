package com.nokia.oss.logging;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.JsonLayout;
import org.apache.logging.log4j.spi.StandardLevel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Plugin(name = "OSSLogger", category = "Core", elementType = "appender", printObject = true)
public class OSSLogger extends AbstractAppender {

	private static List<String> standardLevel = new ArrayList<>();
	ObjectMapper mapper = new ObjectMapper();
	String url = "http://127.0.0.1:9200/log4j2/log4j2";
	
	static{
		for (StandardLevel value : StandardLevel.values()) {
			standardLevel.add(value.name());
		}
	}
	
	protected OSSLogger(String name, Filter filter, Layout<? extends Serializable> layout,boolean ignoreExceptions,String urL) {
		super(name, filter, layout,ignoreExceptions);
		this.url=(urL==null)?url:urL;
	}

	@Override
	public void append(LogEvent arg0) {
		try {
			String exception = null;
			if(arg0.getThrown() != null){
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				arg0.getThrown().printStackTrace(new PrintStream(baos));  
				exception = baos.toString(); 
			}
			
			ObjectNode objNode = mapper.createObjectNode();
			objNode.put("hostName", System.getenv("HOSTNAME"));
			objNode.put("timeStamp", LocalDateTime.now(ZoneId.of("GMT")).toString());
			String levelName = arg0.getLevel().name();
			
			if(standardLevel.contains(levelName)){
				objNode.put("logLevel", levelName);
			}else{
				objNode.put("metric", levelName);
			}
			objNode.put("loggerFile", arg0.getLoggerName());
			objNode.put("message", arg0.getMessage().getFormattedMessage());
			
			//fill the details of thread.
			ObjectNode detail = mapper.createObjectNode();
			objNode.putPOJO("detail", detail);
			detail.put("Thread", arg0.getThreadName());
			detail.put("Thrown", exception);
			
			//fill the customized field.
			Object[] parameters = arg0.getMessage().getParameters();
			if(parameters != null){
				Arrays.asList(parameters).stream().filter(e->e instanceof LoggerCustomizationMap).forEach(e->{
					LoggerCustomizationMap map = (LoggerCustomizationMap)e;
					map.forEach((k,v)->{
						objNode.put(k,v);
					});
				});
			}
			
//			System.out.println(arg0.getLevel().name());
//			System.out.println(objNode.toString());
			System.out.println("url:"+url);
			URLConnection.passingToES(url, objNode.toString());
			
		} catch (Exception e) {
			LOGGER.error(e);
		}
//		System.out.println(getLayout().toSerializable(arg0));
		
	}

	@PluginFactory
    public static OSSLogger createAppender(@PluginAttribute("name") String name,
            @PluginElement("Filter") final Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
            @PluginAttribute("url") String url) {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (layout == null) {
        	LOGGER.info("create DefaultLayout:JsonLayout");
//            layout = PatternLayout.createDefaultLayout();
        	layout = JsonLayout.createDefaultLayout();
        	
        }
        return new OSSLogger(name, filter, layout,ignoreExceptions,url);
    }
}
