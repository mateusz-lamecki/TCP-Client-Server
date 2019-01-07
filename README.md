# [SK2] Projekt zaliczeniowy
#### System wymiany komunikatów publish/subscribe

Autorzy: 132267, 132300

##### Serwer
Serwer został napisany w języku C++.

```
make
./server
```

##### Klient
Klient został napisany w Javie.

```
cd client
mvn clean install
java -jar -Dapple.awt.UIElement="true" client-javaFx/target/client-javaFx-1.0-SNAPSHOT-jar-with-dependencies.jar
```
