#!/bin/bash

current=`pwd`
mkdir -p /tmp/jmweSHARK/
cp -R ../src /tmp/jmweSHARK
cp * /tmp/jmweSHARK
cd /tmp/jmweSHARK/

tar -cvf "$current/jmweSHARK_plugin.tar" --exclude=*.tar --exclude=build_plugin.sh *
