# Shen-JVM

Shen-JVM is a compiler of [Shen](http://shenlanguage.org/) programming language to Java bytecode written in Java 1.6.

Shen is a portable functional programming language developed by [Mark Tarver](http://marktarver.com/) that offers
* Pattern matching
* Lambda calculus consistency
* Macros for defining domain specific languages
* Optional lazy evaluation
* Optional static type checking based on [Sequent calculus](https://en.wikipedia.org/wiki/Sequent_calculus)
* An integrated fully functional Prolog
* An inbuilt compiler-compiler, Shen-YACC

Other ports of Shen by the Shen-JVM author includes
* [Shen-C](https://github.com/otabat/shen-c)
* [iOS version of Shen-C](https://chatolab.wordpress.com/2017/07/10/shen-programming-language-for-ios/), which is a full featured Shen REPL with a customized keyboard for both iPhone and iPad available on the App Store

## Download from releases
1. Download the [latest JAR file]

## Build from sources
Since the author uses IntelliJ IDEA to develop Shen-JVM, it is recommended for the build.
1. Download Shen-JVM project files
```
git clone https://github.com/otabat/shen-jvm
```
2. Open Shen-JVM project in IntelliJ IDEA
3. Run KLMain, which compiles Kλ source files to Java class files
4. Create a JAR file (Build -> Build Artifacts -> Build), which the file will be created as `PROJECT_HOME/out/artifacts/shen_jvm_jar/shen-jvm.jar`

## Run Shen-JVM (from releases)
```
java -jar shen-jvm-xxx.jar
```
or if rlwrap is installed
```
rlwrap java -jar shen-jvm-xxx.jar
```
When running the Shen test suite, it is recommended to increase the heap size and thread stack size using the Java options such as below.
Especially the thread stack size is important and otherwise a stack overflow might occur.
```
java -Xms200m -Xmx2g -Xss4m -jar shen-jvm-xxx.jar
```

## Run Shen-JVM (built from sources)
```
java -jar PROJECT_HOME/out/artifacts/shen_jvm_jar/shen-jvm.jar
```
or the shell script using rlwrap with some JVM options
```
PROJECT_HOME/bin/shen-jvm
```

## Run Shen Test
```shen
(cd "shen/test")
(load "README.shen")
(load "tests.shen")
```
or the one-liner
```shen
(cd "shen/test")(load "README.shen")(load "tests.shen")
```

## Quit Shen-JVM
```shen
(quit)
```
or with an exit status
```shen
(exit 1)
```

## Learn Shen
* [Official website of Shen](http://shenlanguage.org/)
* [The Shen OS Kernel Manual](http://shenlanguage.org/learn-shen/index.html)
* [The Official Shen Standard](http://www.shenlanguage.org/learn-shen/shendoc.htm)
* [Shen Community Wiki](https://github.com/Shen-Language/wiki/wiki)
* [The Book of Shen: third edition](https://www.amazon.co.uk/Book-Shen-Third-Mark-Tarver/dp/1784562130)

## License
#### Shen
Copyright (c) 2010-2015, Mark Tarver  
Shen is released under the [BSD License](https://github.com/otabat/shen-jvm/tree/master/src/shen/license.txt).  

#### Shen-JVM
Copyright (c) 2017, Tatsuya Tsuda  
Shen-JVM is released under the [MIT License](http://www.opensource.org/licenses/MIT).
