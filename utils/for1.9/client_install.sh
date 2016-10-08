#! /bin/bash

# OpenDolphin Client Install Script
#
# Usage:
#   sh ./client_install.sh
#
# == License (MIT License)
#
# Copyright (c) 2010 Tomohiro NISHIMURA
# Copyright (c) 2010 Good-Day, Inc.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#

if [ `id -u` -eq 0 ]; then
  echo "一般ユーザ権限で実行してください." 2>&1
  exit 1
fi

set -e

TODAY=`date +%Y%m%d`

DESTDIR=${1:-"$HOME/OpenDolphin"}

OLDCLIENTDIR=${1:-"$HOME/OpenDolphinClient"}
OLDSERVERDIR="/opt/OpenDolphin"
BACKUPDIR="$DESTDIR/Archive"
INSTALL_PROGRAM='bin'
PASSWORDFILE='.pgpasswd'

CLIENT_JAR='client/OpenDolphin.jar'
CLIENT_LIB='client/lib'
DEFAULT_PLUGINS='client/installed_plugins'

DESKTOP=$(xdg-user-dir DESKTOP 2>/dev/null || true)
if [ -z "$DESKTOP" ]; then
  DESKTOP="$HOME/Desktop"
fi
OLD_DESKTOP_FILE="$DESKTOP/OpenDolphinClient.desktop"
OLD_SERVER_FILE="$DESKTOP/OpenDolphinServer.desktop"
DESKTOP_FILE="$DESKTOP/OpenDolphin.desktop"

brings_up() {
  mkdir -p "$DESTDIR"
  cp "$CLIENT_JAR" "$DESTDIR"
  cp -r "$INSTALL_PROGRAM" "$DESTDIR"
  if [ -f "$PASSWORDFILE" ]; then
    mv "$PASSWORDFILE" "$DESTDIR"
  fi
  cp -r "$CLIENT_LIB" "$DESTDIR"
  cp -r "$DEFAULT_PLUGINS" "$DESTDIR"
  cp -r images "$DESTDIR"
  if [ ! -f "$DESTDIR/run.sh" ]; then
    cat > "$DESTDIR/run.sh" <<'EOF'
#!/bin/sh
cd $(dirname $0)
java -jar ./OpenDolphin.jar
EOF
    chmod +x "$DESTDIR/run.sh"
  fi
}

confirm() {
  printf "$1 [y/N] ? "
  read res
  case "$res" in
    [Yy]*)
      return 0
      ;;
    *)
      return 1
      ;;
  esac
}

confirm_backup() {
  set +x
  if [ ! -d "$1" ]; then
    :
  elif [ ! -f "$2" ] || confirm "バックアップは既に存在します: ${2}\n本当に上書きしますか"; then
    return 0
  fi
  set -x
  return 1
}

backup() {
  mkdir -p "$BACKUPDIR"
  TARGET="$BACKUPDIR/${OLDCLIENTDIR##*/}.$TODAY.tar.gz"
  if confirm_backup "$OLDCLIENTDIR" "$TARGET" ; then
    tar czf "$TARGET" -C "$HOME" ${OLDCLIENTDIR##*/}
  fi
  TARGET="$BACKUPDIR/OpenDolphin18Server.$TODAY.tar.gz"
  if confirm_backup "$OLDSERVERDIR" "$TARGET" ; then
    sudo tar czf "$TARGET" -C "/opt" "${OLDSERVERDIR##*/}"
  fi
}

rearrange_old_icons() {
  if [ -f "$OLD_DESKTOP_FILE" ]; then
    rm "$OLD_DESKTOP_FILE"
  fi
  if [ -f "$OLD_SERVER_FILE" ]; then
    sed -e 's/Name=OpenDolphin Server/Name=OpenDolphin1.8 Server/' "$OLD_SERVER_FILE" > ${OLD_SERVER_FILE%.desktop}18.desktop
    rm -f $OLD_SERVER_FILE
  fi
}

create_new_icon() {
  if [ ! -f "$DESKTOP_FILE" ]; then
    cat > "$DESKTOP_FILE" <<EOF
#!/usr/bin/env xdg-open
[Desktop Entry]
Version=1.0
Encoding=UTF-8
Type=Application
Name=OpenDolphin
Exec=$DESTDIR/run.sh
Icon=$DESTDIR/images/OpenDolphin.ico
StartupNotify=true
Terminal=false
EOF
    chmod +x "$DESKTOP_FILE"
  fi
}

echo "OpenDolphin のインストールを開始します."
set -x
cd "$(dirname "$0")"
brings_up
backup
rearrange_old_icons
create_new_icon
set +x
echo "OpenDolphin のインストールが完了しました."

# Extra script

POSTGRESDS_FILE="$OLDSERVERDIR/jboss-4.2.2.GA/server/default/deploy/postgres-ds.xml"

if [ -f "$POSTGRESDS_FILE" ]; then
  message="\n\nデータベースのパスワードを使用する際は、以下のパスワードを使用してください.\nOpenDolphinのホスト設定にて表示されたパスワードを入力してください.\n\n"
  printf "パスワード: "
  sudo sed -ne 's/.*<password>\(.*\)<\/password>.*/\1/p' "$POSTGRESDS_FILE"

  PGHBACONF="/etc/postgresql/`aptitude search '~n^postgresql-8..$' | tail -n1 |cut -d' ' -f4 | cut -d'-' -f2`/main/pg_hba.conf"
  echo
  echo
  echo "データベースのパスワード設定を有効にするには、"
  echo "$PGHBACONF の IPv4 local connections に、"
  echo "以下から必要なものを追記してください"
  echo
  echo "host dolphin all 127.0.0.1/32 md5"
  for ipaddress in  `{ LC_MESSAGES=C /sbin/ifconfig; } | sed -ne 's/.*inet addr:\([0-9.]*\).*/\1/p'`
  do
    echo "host dolphin all $ipaddress md5"
  done
  echo
  echo
fi

