#!/bin/bash
# MySQL 启动脚本（macOS 本地安装版）
MYSQL_HOME="$HOME/local/mysql"
MYSQL_DATA="$HOME/local/mysql/data"

case "$1" in
  start)
    nohup "$MYSQL_HOME/bin/mysqld_safe" --datadir="$MYSQL_DATA" --user=Windows &>/tmp/mysql.log &
    sleep 2
    "$MYSQL_HOME/bin/mysql" -u root -p1234 -e "SELECT 'MySQL 已就绪' AS status;" 2>/dev/null
    ;;
  stop)
    "$MYSQL_HOME/bin/mysqladmin" -u root -p1234 shutdown 2>/dev/null
    echo "MySQL 已停止"
    ;;
  status)
    "$MYSQL_HOME/bin/mysqladmin" -u root -p1234 ping 2>/dev/null && echo "MySQL 运行中" || echo "MySQL 未运行"
    ;;
  *)
    echo "用法: $0 {start|stop|status}"
    ;;
esac
