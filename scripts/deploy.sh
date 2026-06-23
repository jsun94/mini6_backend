#!/bin/bash

REPOSITORY=/home/ubuntu/app
PROJECT_NAME=my-backend # 본인의 프로젝트 이름이나 원하는 폴더명으로 수정 가능

echo "> 현재 실행 중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl java | grep jar | awk '{print $1}')

echo "현재 실행 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 실행 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/build/libs/*.jar | grep -v 'plain' | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
nohup java -jar $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &