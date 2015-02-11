#/bin/bash
if [ -n "$1" ]; then
	case "$1" in
		'all')
			echo "stopping all redis nodes....."
			echo | ps aux|grep -e redis-server | grep -v grep
			pkill redis-server
			;;
		*)
		PSID=`echo $(ps aux|grep -e redis-server.*$1 | grep -v grep | awk '{print $2}')`
		if [ -n "${PSID}" ]; then
			echo "stopping redis node{$1}......"
			kill -15 ${PSID}	
		else
			echo "redis node{$1} is not started."
		fi
	esac
else
	echo "stopping all redis nodes....."
	echo | ps aux|grep -e redis-server | grep -v grep
	pkill redis-server
fi
