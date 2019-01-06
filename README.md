# [SK2] Projekt zaliczeniowy
#### System wymiany komunikatów publish/subscribe

Autorzy: 132267, 132300

##### Serwer
Serwer został napisany w języku C++.

Przykład uruchomienia serwera na porcie 3552:

```
make clean && make
./server 3552
```

##### Klient
Klient zostal napisany w Javie.

```
mvn clean install
java -jar -Dapple.awt.UIElement="true" client/client-javaFx/target/client-javaFx-1.0-SNAPSHOT.jar -h
```
