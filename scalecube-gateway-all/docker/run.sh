#!/bin/bash

java \
-Dio.netty.leakDetection.level=PARANOID \
-Djava.net.preferIPv4Stack=true \
-Dhazelcast.jmx=true \
-Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=5678 \
-Dlog4j.configurationFile=conf/log4j2.xml \
-DAsyncLogger.WaitStrategy=Sleep \
-cp "../target/*" io.scalecube.gateway.all.GatewayAll `pwd` \