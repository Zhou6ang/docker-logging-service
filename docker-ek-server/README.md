# Build server image:
1. cd docker-ek-server
2. docker build -t docker-ek-server . 

# Usage:
1. sysctl -w vm.max_map_count=262144
2. docke run --name docker-ek-server -p 9200:9200 -p 5601:5601 -d docker-ek-server
# Note:
 default port is 9200 for elasticsearch and 5601 for kibana.
