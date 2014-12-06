.PHONY: all
all:
	javac SonoranCellular/utils/*.java
	javac SonoranCellular/servlets/*.java
	

.PHONY: clean
clean:
	-/bin/rm SonoranCellular/utils/*.class SonoranCellular/servlets/*.class

.PHONY: reset
reset:
	../../../../bin/shutdown.sh
	make clean
	make all
	rm ../../../../logs/catalina.out
	../../../../bin/startup.sh
