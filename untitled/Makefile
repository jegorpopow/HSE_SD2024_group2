GRADLE = gradlew
JC = java
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

SRC=src/main/java/org/example

default: all

all: all.class


all.class: $(SRC)/*.java
	$(GRADLE) build

run: export JAVA_BUILD=build/classes/java/main
run: all.class
	$(JC) -cp  build/classes/java/main org.example.Main < src/test/resources/in > src/test/resources/out


clean:
		$(RM) *.class