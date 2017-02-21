#!/bin/sh
sh /elasticsearch-5.2.1/bin/elasticsearch &
sleep 30
sh /kibana-5.2.1/bin/kibana
