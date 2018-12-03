#!/bin/bash
#
#
###################################
#环境变量及程序执行参数
#需要根据实际环境以及Java程序名称来修改这些参数
###################################

source /etc/profile

#JDK所在路径: 
#JAVA_HOME=/usr/java/laster

# 自动设置ulimit
ulimit -s 20480

#Java程序所在的目录
cd `dirname $0`
APP_HOME=`pwd`

#应用名称
APP_NAME=API

#使用哪个环境下的配置文件dev、test、prod
PROFILES=prod

#http端口号
PORT=""


#启动的Main方法
MAIN_CLASS=com.feitai.admin.web.AdminApplication

#程序的pid保存的文件
PID_FILE="$APP_HOME/pid"


#java虚拟机启动参数
JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true"




#日志文件目录，由于logback不能自动创建目录，因此需要程序启动时帮它创建
LOGS_DIR="$APP_HOME/logs"
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
    #echo "created logs directory: path=$LOGS_DIR"
fi
STDOUT_FILE=$LOGS_DIR/out.log


#启动等待的最大时间,单位秒
START_WAIT_TIMEOUT=20

GET_PID_BY_APP_HOME() {
   echo `ps -ef|grep java|grep ${APP_HOME}|awk '{print $2}'`
}


###################################
#(函数)启动程序
###################################
start() {
   PID=$(GET_PID_BY_APP_HOME)

   if [ x"$PID" != x ];then
      echo "Warning: ${APP_NAME} Server already started!"
      status
   else
   
      echo "Starting ${APP_NAME} Server ..."
      echo
      #debug时可以打开下面的echo查看详细信息
      #echo "JAVA_OPTS: $JAVA_OPTS"
      #echo
      #echo "JAVA_MEM_OPTS: $JAVA_MEM_OPTS"
      #echo
      #echo "CLASSPATH: $CLASSPATH"
      #echo

      if [[ $PROFILES == "prod" ]]; then
          JAVA_MEM_OPTS=" -server -Xms1024m -Xmx2g -Xmn256m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70"
      else
          JAVA_MEM_OPTS=" -server -Xms256m -Xmx1g -Xmn128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70"
      fi

	  lib=$PWD:$PWD/config/
        for jarpath in `ls lib/*.jar`
        do
                lib=$lib:$PWD/$jarpath
        done
      #echo "${JAVA_HOME}/bin/java ${JAVA_OPTS} ${JAVA_MEM_OPTS} ${JAVA_DEBUG_OPTS} -cp ${lib} -Xms128m -Xmx800m ${MAIN_CLASS} --spring.profiles.active=${PROFILES}>>${STDOUT_FILE} 2>&1 &"
      nohup $JAVA_HOME/bin/java $JAVA_OPTS $JAVA_MEM_OPTS -cp $lib  $MAIN_CLASS --spring.profiles.active=$PROFILES $PORT >>$STDOUT_FILE 2>&1 &
      pid=$!
    	echo $pid > $PID_FILE
    	#wait $pid

      sleep 3
      PID=$(GET_PID_BY_APP_HOME)

      starttime=0
      while  [ x"$PID" == x ]; do
         if [[ "$starttime" -lt ${START_WAIT_TIMEOUT} ]]; then
            sleep 1
            ((starttime++))
            echo -e ".\c"
            PID=$(GET_PID_BY_APP_HOME)
         else

            echo "${APP_NAME} Server failed to start"
            echo "The PID doesn\'t exits in ${START_WAIT_TIMEOUT} seconds!"
            echo "check logs/out.log to see the details"
            status
            exit -1
         fi
      done

      echo
      echo "${APP_NAME} Server start successfully!"
      status
   fi
   
}


###################################
#(函数)停止程序
###################################
stop() {
	 
	 PID=`cat $PID_FILE`
	 
	 if [ x"$PID" == x ];then
       PID=$(GET_PID_BY_APP_HOME)
       if [ x"$PID" == x ];then
          status
          return
       fi
   fi

   echo "trying to stop ${APP_NAME} Server (pid=$PID) ..."
   kill -15 $PID
   echo ''>pid
   sleep 3
   PID=$(GET_PID_BY_APP_HOME)
   while [ x"$PID" != x ]; do
      echo -n "."
      kill $PID
      sleep 1
      PID=$(GET_PID_BY_APP_HOME)
   done

   echo
   echo "OK: BDCI Server stopped"
   status
}

###################################
#(函数)检查程序运行状态
###################################
status() {
   PID=$(GET_PID_BY_APP_HOME)

   echo "================================"

   if [ x"$PID" != x ]; then
      echo "${APP_NAME} Server is running:"
   else
      echo "${APP_NAME} Server is NOT running!"
   fi

        echo "    |"
        PID=$(GET_PID_BY_APP_HOME)
        if [ x"$PID" != x ]; then
      echo "    |-- ${APP_NAME} is process $PID"
   else
      echo "    |-- ${APP_NAME} is NOT running!"
   fi

   echo "    |"

   echo "================================"
}

###################################
#(函数)打印系统环境参数
###################################
info() {
   echo
   echo "************* [OS] ***************"
   echo "OS Release: " `head -n 1 /etc/issue`
   echo "OS Infomation: " `uname -a`
   echo
   echo "*************  [JVM]  ***************"
   echo "JAVA_HOME: $JAVA_HOME"
   echo "JAVA_OPTS: $JAVA_OPTS"
   echo "JAVA_MEM_OPTS: $JAVA_MEM_OPTS"
   echo
   echo "*************  [CLASSPATH]  ***************"
   echo "CLASSPATH: $CLASSPATH"
   echo
   echo "*************  [APPLICATION]  ***************"
   echo "JAVA_MAIN_CLASS: $APP_MAIN_CLASS"
   echo "APP_HOME=$APP_HOME"
   echo "****************************"
}



extend_opts(){
   #echo "extend_opts starting.."
   while getopts "a:p:m:f:" optname
    do
      #echo "The $optname is $OPTARG"
      case "$optname" in
        "a")
          echo "Option $optname(PROFILES) is $OPTARG"
          PROFILES=$OPTARG

          ;;
        "p")
          echo "Option $optname(PROFILES) is $OPTARG"
          PORT="--server.port=$OPTARG"
          ;;
        "f")
          echo "Option $optname(WAR_FILE) is $OPTARG"
          WAR_FILE=$OPTARG
          ;;
        "m")
          echo "Option $optname(MAIN_CLASS) is $OPTARG"
          if [ "$OPTARG" == "h5" ];then
            MAIN_CLASS="com.feitai.jieya.server.H5Application"
          fi
          echo "main application is $MAIN_CLASS"
          ;;
        "?")
          echo "Usage: args [-p] [-a]"
          echo "-p means http port"
          echo "-a means profiles active"
          echo "-m means main application"
          exit 1
          ;;
        *)
        # Should not occur
          echo "Unknown error while processing options"
          ;;
      esac
    done
}

extend_opts "$@"

###################################
#读取脚本的最后一个参数，进行判断
#参数取值范围：{start|stop|restart|status|info}
#如参数不在指定范围之内，则打印帮助信息
###################################
args=$#
case "${!args}" in
   'start')
      start
      ;;
   'stop')
     stop
     ;;
   'restart')
     stop
     start
     ;;
   'status')
     status
     ;;
   'info')
     info
     ;;
  *)
     echo "Usage: $0 {start|stop|restart|status|info}"
     exit 1
esac
                   

