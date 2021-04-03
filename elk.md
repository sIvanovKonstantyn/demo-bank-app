#ELK showcase:

logback dependency:
```
implementation 'net.logstash.logback:logstash-logback-encoder:6.6'
```

logback appender:
```
 <appender name="stash" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <destination>192.168.3.27:4560</destination>

        <!-- encoder is required -->
        <encoder class="net.logstash.logback.encoder.LogstashEncoder" />
 </appender>
```

input logstash config:
```
input {
  tcp {
	port => 4560
	codec => json_lines
  }
}
```

test console output logstash config:
```
output {
  stdout {}
}
```

start logstash command:
```
> logstash.bat -f logstash-sample.conf
```

_after that all modules logs could be accessed in logstash console output_

reconfigure logstash for sending messages to elasticsearch:
```
output {
      elasticsearch { hosts => ["localhost:9200"] }
}
```

then we could find bew indexes in elasticsearch:
```
> http://localhost:9200/_aliases?pretty=true
> http://localhost:9200/new_index/_search
```
after starting kibana all logs could be searched in its UI:
![img_5.png](img_5.png)