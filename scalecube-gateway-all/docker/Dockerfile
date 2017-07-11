FROM openjdk-8-jre-alpine
RUN apt-get update
RUN apt-get install -y dsniff iptables
WORKDIR $workdir
VOLUME $workdir/log
RUN chmod u+x run.sh
CMD ["run.sh"]