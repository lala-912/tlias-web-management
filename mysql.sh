#!/bin/bash
# MySQL 启动脚本（macOS 本地安装版）
# 密码通过环境变量传入，避免命令行暴露
MYSQL_HOME="$HOME/local/mysql"
MYSQL_DATA="$HOME/local/mysql/data"
MYSQL_PWD="${MYSQL_PWD:-1234}"
export MYSQL_PWD

case "$1" in
  start)
    nohup "$MYSQL_HOME/bin/mysqld_safe" --datadir="$MYSQL_DATA" --user=Windows &>/tmp/mysql.log &
    sleep 2
    "$MYSQL_HOME/bin/mysql" -u root -e "SELECT 'MySQL 已就绪' AS status;" 2>/dev/null
    ;;
  stop)
    "$MYSQL_HOME/bin/mysqladmin" -u root shutdown 2>/dev/null
    echo "MySQL 已停止"
    ;;
  status)
    "$MYSQL_HOME/bin/mysqladmin" -u root ping 2>/dev/null && echo "MySQL 运行中" || echo "MySQL 未运行"
    ;;
  *)
    echo "用法: $0 {start|stop|status}"
    echo "提示: 可通过 MYSQL_PWD 环境变量设置密码，默认 1234"
    ;;
esac
