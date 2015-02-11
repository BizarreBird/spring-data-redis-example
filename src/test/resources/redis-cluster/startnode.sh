#!/bin/sh
if [ ! -n "$1" ]; then
	echo "please input the node to start"
	exit 1
elif [ "$1" = "all" ]; then
	for x in $( ls -l . |awk '/^d/ {print $NF}' ) 
	do		
		cd $x
		echo "starting redis node{$x}......"
		./startnode.sh
		cd ..
	done
elif [ ! -d "$1" ]; then
        echo "node{"$1"} is not exist."
        exit 1
else
	echo "starting redis node{"$1"}...... "	
	cd $1
	./startnode.sh
fi
