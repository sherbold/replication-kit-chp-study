#!/bin/sh
PLUGIN_PATH=$1
REPOSITORY_PATH=$2
NEW_UUID=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)

cp -R $REPOSITORY_PATH "/dev/shm/$NEW_UUID"

COMMAND="java -jar $PLUGIN_PATH/target/jmweSHARK.jar --input /dev/shm/$NEW_UUID --rev $3 --url $4 --project_name ${5} --db-hostname $6 --db-port $7 --db-database $8"

if [ ! -z ${13+x} ] && [ ${13} != "None" ]; then
    COMMAND="$COMMAND --debug ${13}"
fi

if [ ! -z ${9+x} ] && [ ${9} != "None" ]; then
	COMMAND="$COMMAND --db-user ${9}"
fi

if [ ! -z ${10+x} ] && [ ${10} != "None" ]; then
	COMMAND="$COMMAND --db-password ${10}"
fi

if [ ! -z ${11+x} ] && [ ${11} != "None" ]; then
	COMMAND="$COMMAND --db-authentication ${11}"
fi

if [ ! -z ${12+x} ] && [ ${12} != "None" ]; then
	COMMAND="$COMMAND -ssl"
fi


$COMMAND

rm -rf "/dev/shm/$NEW_UUID"
