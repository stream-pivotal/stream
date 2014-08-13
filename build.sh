BUILD=~/gem/cedar/build
CP=.:$BUILD/product/lib/gemfire.jar:$BUILD/product/lib/antlr.jar:$BUILD/tests/classes
rm -rf classes
rm -rf lib
mkdir classes
mkdir lib
echo "## Started Compiling stream source files using classpath $BUILD."
javac -cp $CP src/com/gemstone/gemfire/stream/*.java -d classes
javac -cp $CP src/com/gemstone/gemfire/stream/internal/*.java -d classes
jar cf lib/stream.jar classes/com/gemstone/gemfire/stream/*class classes/com/gemstone/gemfire/stream/internal/*.class
echo "## Finished Compiling stream source files using classpath $BUILD."


