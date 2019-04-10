Kako bi se ispravno mogla koristiti biblioteka lsystems.jar treba ovo pokrenuti u cmd-u nakon pozicioniranja u root direktorij ovog projekta. Takoðer prije pokretanja treba staviti lsystems.jar datoteku
u hw-0036506711\lib direktorij:

mvn install::install-file -Dfile=lib\lsystems.jar -DgroupId=hr.fer.zemris.java.lsystems -DartifactId=lsystems -Dversion=1 -Dpackaging=jar

Nakon toga treba osvjeziti projekt, a ako se Eclipse i dalje buni na pom.xml datoteku treba obrisati dependency za koji se buni i onda ga ponovno zalijepiti ili napraviti UNDO.