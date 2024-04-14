#删除上次部署包
rm -rf /root/application/RealtimeBus/
echo "删除上次部署包"

#关闭指定端口的进程-这里是8083
kill -9 `netstat -nlp | grep :8083 | awk '{print $7}' | awk -F"/" '{ print $1 }'`
wait
echo "已停止8083"
#创建一个文件夹用户存放构建包解压后的文件
mkdir -p /root/application
wait
echo "创建目录/root/application"
#解压构建包
tar zxvf /root/app/package.tgz -C /root/application/
wait
echo "解压至/root/application/"
#后台启动并打印启动日志
nohup java -jar /root/application/RealtimeBus/target/mall-tiny-1.0.0-SNAPSHOT.jar > /home/RealtimeBusStartLog 2>&1 &
echo "部署成功"
#删除构建包,下次上传的构建包不会覆盖,所以必须删除
rm -rf /root/app/package.tgz
echo "删除本次构建包"
