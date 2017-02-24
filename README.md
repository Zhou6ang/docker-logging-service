# Logging-service
This will consist of server and agent, agent for log collection, server for log centralized and analyzed and display.

# Build server image:
1. cd docker-ek-server
2. docker build -t docker-ek-server . 

# Build agent image:
1. cd docker-fluent-logger-agent
2. docker build -t docker-fluent-agent .

# Usage:
1. sysctl -w vm.max_map_count=262144
2. docke run --name docker-ek-server -p 9200:9200 -p 5601:5601 -d docker-ek-server
3. docker run --name docker-fluent-agent -v [DIR]:/fluentd/log -e ES=[Host:Port] -e INDEX=xxx -e TYPE=xxx -d docker-fluent-agent
# Note:
docker-ek-logger-server image, default port is 9200 for elasticsearch and 5601 for kibana.

DIR is folder which monitoring all log file under itself, e.g. `pwd`/log/

ES=[Host:Port] is Elasticsearch host and port, e.g. ES=10.10.10.10:9200

INDEX is defined index of ES which must be defined by yourself.

TYPE is defined type of ES which must be defined by yourself.
