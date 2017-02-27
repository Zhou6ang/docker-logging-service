# Build agent image:
1. cd docker-fluent-logger-agent
2. docker build -t docker-fluent-agent .

# Usage:
1. docker run --name docker-fluent-agent -v [DIR]:/fluentd/log -e ES=[Host:Port] -e INDEX=xxx -e TYPE=xxx -d docker-fluent-agent

# Note:

DIR is folder which monitoring all log file under itself, e.g. \`pwd\`/log/

ES=[Host:Port] is Elasticsearch host and port, e.g. ES=10.10.10.10:9200

INDEX is defined index of ES which must be defined by yourself.

TYPE is defined type of ES which must be defined by yourself.
