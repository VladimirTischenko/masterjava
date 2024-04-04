set LB_HOME=C:\programs\liquibase-4.27.0
call %LB_HOME%\liquibase.bat --driver=org.postgresql.Driver ^
--classpath=%LB_HOME%\lib ^
--changeLogFile=databaseChangeLog.sql ^
--url="jdbc:postgresql://localhost:5432/masterjava" ^
--username=user ^
--password=password ^
migrate