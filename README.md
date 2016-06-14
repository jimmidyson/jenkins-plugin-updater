# jenkins-plugin-updater

Run the folling passing the URL of your plugins.txt, you will get the updated list printed out in your terminal..

```
mvn exec:java -Dexec.arguments=https://raw.githubusercontent.com/fabric8io/jenkins-docker/master/plugins.txt
```
