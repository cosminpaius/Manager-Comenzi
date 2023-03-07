all: build

build:
	javac *.java

run: Tema2.java
	java Tema2 $(arg0) $(arg1)
clean:
	rm *.class
