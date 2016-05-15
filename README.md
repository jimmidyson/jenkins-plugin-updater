# jenkins-plugin-updater


Copy your plugins.txt file to `src/main/resources` and run..

```
mvn exec:java -Dhttps://raw.githubusercontent.com/fabric8io/jenkins-docker/master/plugins.txt
```
