# Agent configuration
Note: currently the agent ONLY support java programming, we will support more programming soon later.
I. Adding dependency jar to your own project, you can choose one of below ways:
	1. Add below dependency to pom.xml of your project.
		<dependency>
			<groupId>com.nokia.oss.logger</groupId>
			<artifactId>oss-logger</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>
	2. Download jar from http://scm0.access.nokiasiemensnetworks.com/isource/mvnroot/merlinpp/mpp/latest/com/nokia/oss/logger/oss-logger/1.0-SNAPSHOT/
	3. Build it locally from source code which location is ./java/oss-logger via execute: mvn clean install 
II. Adding below configuration to your Log4j2.xml project.(Note: currently not support log4j.xml)
	Example:
		<Configuration status="WARN" monitorInterval="30" packages="com.nokia.oss.logging">
		…………
		<Appenders>
			<OSSLogger name="mylogger" url="http://HOST.OF.SERVER:PORT/PROJECT_INDEX/PROJECT_TYPE/PROJECT_ID"/>
		</Appenders>
		<Root level="info">      
			<AppenderRef ref="mylogger"/>        
		</Root>

	1.	Add packages="com.nokia.oss.logging" to <Configuration/>
	2.	Add <OSSLogger/> to <Appenders/> as name is mylogger
	3.	Add <AppenderRef ref="mylogger"/>  to <Root/>
III. Or you can refer to example folder. More detail info: https://confluence.int.net.nokia.com/display/rangers/Centralized+Logging+Service 