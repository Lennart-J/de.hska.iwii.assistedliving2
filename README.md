# de.hska.iwii.assistedliving2

## Einrichtung des Projekts (Windows)

* Eclipse for Java EE Developers (empfohlen)
* git clone in workspace
* in Eclipse import -> git -> local repository
* OpenCV 3.0 herunterladen
  * opencv\build\java\x64 in Path
  * als User Library in Eclipse einrichten und im Projekt einfügen
* Tomcat 8.0
  * opencv.jar in den lib Ordner
  * setenv.bat in bin Ordner
*  (falls noch nicht ausführbar) In Eclipse unter Show View -> Problems eine Warning fixen die mit dem exposen der OpenCV Library zu tun hat
  * Automatic fix -> zweite Option auswählen

### setenv.bat
> set JAVA_OPTS="-Djava.library.path=""C:\opencv\build\java\x64;C:\opencv\build\x64\vc11\bin"""

### Das Projekt ist nur ausführbar über den Proxy der HS!
* Proxy einrichten in src/servlets/credentials.properties Zugangsdaten eintragen
