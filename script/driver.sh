#!/bin/bash

#driver.sh [filename] [reducer port]

#expect first argument to be filename to process
filename=$1
#compute total number of lines
num_lines=`cat $filename | wc -l`
#assume local host
redHost="127.0.0.1"
#reducer port number - expected as second argument
redPort=$2

#assume system is being run from project directory
cpath="bin/"

cmdSplit="java -classpath $cpath Splitter $filename $num_lines"
cmdStem="java -classpath $cpath Stemmer"
cmdMap="java -classpath $cpath Mapper"
cmdSend="java -classpath $cpath Sender $redHost $redPort"
cmdReduce="java -classpath $cpath Reducer"

 # start reducer
($cmdReduce > reduce_results.txt) &
pid_red=$!

#keep array of pids
pid_arr=(0 0 0 0)

#start all processes in background
for i in 1 2 3 4; do
    #make the command for splitter i
	fullCmdSplit="$cmdSplit $i"
	
	#start all processes
	($fullCmdSplit | $cmdStem | $cmdMap | $cmdSend) &
	pid=$!
	
	#store pid
	idx=`echo "$i - 1" | bc -l`
	pid_arr[$idx]=$pid
	
done

#wait on all pids
for p in "${pid_arr[@]}"; do
	wait $p
done

#stop reducer (there must be a better way ... )
kill $pid_red

