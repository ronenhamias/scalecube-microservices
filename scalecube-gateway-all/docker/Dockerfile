FROM java:oracle-java8
RUN apt-get update
RUN apt-get install -y dsniff iptables
ENV workdir /opt/oapi
WORKDIR $workdir
VOLUME $workdir/log
ADD superpeer.core.examples $workdir
RUN chmod u+x bin/start.sh
CMD ["bin/start.sh"]