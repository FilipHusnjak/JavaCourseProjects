Kako bi se ispravno mogla koristiti biblioteka lsystems.jar treba ovo pokrenuti u cmd-u èime æe se instalirati u .m2 direktoriju:

mvn install:install-file -Dfile=<path-to-downloaded-file> -DgroupId=lsystems -DartifactId=lsystems -Dversion=1 -Dpackaging=jar

<path-to-downloaded-file> je lokacija skinute biblioteke lsystems.jar

Nakon toga treba osvjeziti projekt, a ako se Eclipse i dalje buni na pom.xml datoteku treba obrisati dependency za koji se buni i onda ga ponovno zalijepiti ili napraviti UNDO.