#!/usr/bin/ruby1.8 -Ku
# -*- coding: utf-8 -*-
=begin
= installer of OpenDolphin Server
== usage
In the installer directory:

 $ sudo ./server-install.sh

and run if client needs too:

 $ ./client-install.sh

When server installed, client-install.sh creates server launcher
on your desktop too.


After install:
* log out and log in again. (for opendolphin group privilege)
* run /opt/OpenDolphin/util/open-dolphin-server.rb

== prepare
 $ ruby1.8 open-dolphin-server-install.rb pack path/to/installer

or without directory (create ./OpenDolphin-installer)

 $ ruby1.8 open-dolphin-server-install.rb pack


== License (MIT License)

Copyright (c) 2009, 2010 Kazuhiro NISHIYAMA
Copyright (c) 2009, 2010 Good-Day, Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

=end

$LOAD_PATH.push File.dirname(__FILE__)
require "fileutils"
require "ipaddr"

module OpenDolphinServerInstaller
  FU = FileUtils::Verbose
  APT = "aptitude".freeze
  INSTALL_DIR = "/opt/OpenDolphin"
  JBOSS_DEFAULT = "#{INSTALL_DIR}/jboss-4.2.2.GA/server/default"
  INIT_DB_DIR = "#{INSTALL_DIR}/init-db"

  case File.read("/etc/debian_version").chomp
  when "4.0"
    DISTRIBUTION = "etch".freeze
  else
    DISTRIBUTION = `lsb_release -cs`.to_s.chomp.freeze
  end

  case DISTRIBUTION
  when "etch"
    PG_VERSION = "8.1".freeze
  when "hardy"
    PG_VERSION = "8.3".freeze
  else
    PG_VERSION = `aptitude search '~n^postgresql-8..$'`[/8\.\d/].freeze
  end

  class InstallAbort < Exception; end

  Messages = Hash.new{|h,k| proc{|message|message} }
  Messages["ja"] = Hash.new{|h,k| k }
  Messages["ja"]["FAILED: %p"] = "実行失敗: %p"
  Messages["ja"]["OK: %s unchanged"] = "OK: %s の変更なし"
  Messages["ja"]["Apply above change (Y/N) [default=Y] ? "] =
    "上の変更を適用しますか? (Y/N) [default=Y] "
  Messages["ja"]["WARNING: %s unchanged"] =
    "WARNING: %s を変更しませんでした。"
  Messages["ja"]["This program needs root privilege."] =
    "このプログラムには管理者権限が必要です。"
  Messages["ja"]["OK: debian-backports-keyring already installed."] =
    "OK: debian-backports-keyring がインストール済みです。"
  Messages["ja"]["OK: sun-java6-jdk is installable"] =
    "OK: sun-java6-jdk がインストール可能です。"
  Messages["ja"]["FAILED: pg_hba.conf: failed to allow dolphin"] =
    "FAILED: pg_hba.conf: dolphin の接続許可に失敗しました。"
  Messages["ja"]["WARNING: createuser dolphin failed. OK if dolphin user already created."] =
    "WARNING: 「createuser dolphin」に失敗しました。既に作成済みの場合は問題ありません。"
  Messages["ja"]["WARNING: createdb dolphin failed. OK if dolphin database already created."] =
    "WARNING: 「createdb dolphin」に失敗しました。既に作成済みの場合は問題ありません。"

  Messages["ja"]["OpenDolphin Server for Initialize Dolphin Database"] =
    "OpenDolphin Server (データベース初期設定用)"
  Messages["ja"]["Wait for starting %s"] =
    "%s の起動待ちです。"
  Messages["ja"]["Wait for stopping %s"] =
    "%s の終了待ちです。"

  module Util
    module_function

    def xsystem(*args)
      STDERR.puts args.join(" ")
      system(*args) or raise InstallAbort, (_("FAILED: %p") % [args])
    end

    def xsystem_without_raise(*args)
      STDERR.puts args.join(" ")
      system(*args)
    end

    if /^ja/ =~ ENV["LANG"]
      MESSAGES = Messages["ja"]
    else
      MESSAGES = Messages["en"]
    end

    def _(message)
      MESSAGES[message]
    end

    def confirm(message)
      STDERR.write message
      /^n/i !~ gets
    end

    def replace_file(path, force=false)
      new_path = "#{path}.new"
      content = File.open(path, 'rb') {|f| f.read }
      new_content = yield content.freeze
      if content == new_content
        puts(_("OK: %s unchanged") % path)
        return
      end
      system("cp", "-a", path, new_path) # preserve permissions, owner, etc.
      File.open(new_path, 'wb') {|f| f.write new_content }
      xsystem_without_raise("diff", "-u", path, new_path)
      unless force
        unless confirm(_("Apply above change (Y/N) [default=Y] ? "))
          puts(_("WARNING: %s unchanged") % path)
          return
        end
      end
      FU.mv(path, "#{path}.#{Time.now.strftime('%Y%m%d%H%M%S')}")
      FU.mv(new_path, path)
    ensure
      FU.rm_f(new_path)
    end
  end

  extend Util

  module_function

  def need_root_privilege
    if Process.uid != 0
      STDERR.puts(_("This program needs root privilege."))
      exit(false)
    end
  end

  def install_bpo_keyring
    if /^ii/ =~ `dpkg -l debian-backports-keyring`
      puts(_("OK: debian-backports-keyring already installed."))
      return
    end
    keyring = Dir["*/debian-backports-keyring_*_all.deb"].sort[-1]
    xsystem("dpkg", "-i", keyring)
  end

  def sun_java6_installable?
    / sun-java6-jdk / =~ `aptitude search sun-java6-jdk`
  end

  def set_bpo_apt_line
    if sun_java6_installable?
      puts(_("OK: sun-java6-jdk is installable"))
      return
    end
    replace_file("/etc/apt/sources.list") do |content|
      content + <<-'LIST'

deb http://backports.good-day.net/ etch-backports main contrib non-free
      LIST
    end
  end

  def apt_update
    xsystem(APT, "update")
  end

  def apt_upgrade
    xsystem(APT, "upgrade")
  end

  def apt_install_packages
    packages = [
      "postgresql-#{PG_VERSION}",
      #"ant", # use newer ant in archive instead
      #"ant-optional",
      "libopenssl-ruby1.8",
      "libvte-ruby",
      "libnotify-bin",
      "libglade2-ruby",
      "libgettext-ruby1.8",
    ]
    if DISTRIBUTION == "etch" # etch only
      packages.push "libgtk-trayicon-ruby1.8"
      %w"bin jre jdk fonts plugin demo doc source".each do |s|
        packages.push "sun-java5-#{s}_" # '_' means 'purge'
      end
      %w"bin jre jdk fonts plugin demo".each do |s|
        packages.push "sun-java6-#{s}"
      end
    else
      %w"jre-headless jre jdk demo".each do |s|
        packages.push "openjdk-6-#{s}"
      end
    end
    xsystem(APT, "install", *packages)
  end

  def postgresql_conf__listen_address_all
    replace_file("/etc/postgresql/#{PG_VERSION}/main/postgresql.conf") do |content|
      content.sub(/^\#? *(listen_addresses\s+=\s+)'(.+?)'/) { "#$1'*'" }
    end
  end

  def get_inet_addr_and_prefixlen
    addr, mask = []
    `env LANG=C /sbin/ifconfig`.scan(/inet addr:([^ ]+) .+ Mask:([^ ]+)/) do
      addr, mask = $~.captures
      break if addr != "127.0.0.1"
    end
    mask_i = IPAddr.new(mask).to_i
    prefixlen = (0...32).inject(0){|n,i|n+mask_i[i]}
    [addr, prefixlen]
  end

  def pg_hba_conf__allow_dolphin(ipaddr, prefixlen)
    replace_file("/etc/postgresql/#{PG_VERSION}/main/pg_hba.conf") do |content|
      [
        # ["dolphin", "dolphin", "127.0.0.1/32"],
        ["dolphin", "dolphin", "#{ipaddr}/#{prefixlen}"],
      ].each do |data|
        line = "host    %-11s %-11s %-21s md5\n" % data
        unless content.include?(line)
          content = content.sub(/^.+127\.0\.0\.1\/32.+/) { line + $& }
        end
        unless content.include?(line)
          raise InstallAbort, _("FAILED: pg_hba.conf: failed to allow dolphin")
        end
      end
      content
    end
  end

  def restart_postgresql
    xsystem("/etc/init.d/postgresql-#{PG_VERSION}", "restart")
  end

  def pack_OpenDolphin(installer_dir)
    deployed = File.expand_path("../jboss-4.2.2.GA/server/default/deploy/OpenDolphinServer.jar", File.dirname(__FILE__))
    unless File.exist?(deployed)
      STDERR.puts "You should build before pack. Please run ant."
      exit false
    end
    FU.mkpath("#{installer_dir}/util/debian-backports-keyring")
    FU.mkpath("#{installer_dir}/util/archive")
    FU.mkpath("#{installer_dir}/util/open-dolphin-backup-usb-storage")
    xsystem(*%W"tar --owner=root --group=root --exclude=.svn --exclude='*.log' -zvcf #{installer_dir}/OpenDolphin-Server.tar.gz jboss-4.2.2.GA")
    xsystem(*%W"tar --owner=root --group=root --exclude=.svn -zvcf #{installer_dir}/OpenDolphin-init-db.tar.gz build.xml base/dist client/dist settings")
    xsystem(*%W"tar --owner=root --group=root --exclude=.svn -zvcf #{installer_dir}/OpenDolphin-lib.tar.gz lib")
    xsystem(*%W"tar --owner=root --group=root --exclude=.svn -zvcf #{installer_dir}/OpenDolphin-client.tar.gz client")
    util_dir = File.expand_path(File.dirname(__FILE__))
    FU.cp(Dir["#{util_dir}/*.rb"], "#{installer_dir}/util")
    FU.cp(Dir["#{util_dir}/archive/*.tar.*"], "#{installer_dir}/util/archive")
    FU.cp(Dir["#{util_dir}/debian-backports-keyring/*.{dsc,gz,deb}"], "#{installer_dir}/util/debian-backports-keyring")
    FU.cp(Dir["#{util_dir}/open-dolphin-backup-usb-storage/*.{dsc,gz,deb,changes}"], "#{installer_dir}/util/open-dolphin-backup-usb-storage")
    FU.cp("#{util_dir}/open-dolphin-backup-replace.rb", "#{installer_dir}/util/open-dolphin-backup-replace.rb")
    open("#{installer_dir}/server-install.sh", "w") do |f|
      f.puts <<-'SH'
#!/bin/sh
set -e
if [ `id -u` -ne 0 ]; then
  if [ -x /usr/bin/gksu ]; then
    exec /usr/bin/gksu -S "$0"
  fi
  echo "This program should run as root." 2>&1
  exit 1
fi
if [ ! -t 0 ]; then
  if [ -x /usr/bin/gnome-terminal ]; then
    /usr/bin/gnome-terminal --command="$0" </dev/null >/dev/null 2>&1 &
    exit
  fi
  echo "This program should run in a terminal." 2>&1
  exit 2
fi
cd "$(dirname "$0")"
ruby1.8 util/open-dolphin-server-install.rb
      SH
      f.chmod 0755
    end
    open("#{installer_dir}/client-install.sh", "w") do |f|
      f.puts <<-'SH'
#!/bin/bash
if [ `id -u` -eq 0 ]; then
  echo "This program should not run as root." 2>&1
  exit 1
fi
DESTDIR=${1:-"$HOME/OpenDolphinClient"}
set -ex
cd "$(dirname "$0")"
mkdir -p "$DESTDIR"
tar -zxvf OpenDolphin-client.tar.gz -C "$DESTDIR"
tar -zxvf OpenDolphin-lib.tar.gz -C "$DESTDIR"
tar -zxvf OpenDolphin-init-db.tar.gz -C "$DESTDIR" base
tar -jxvf util/archive/apache-ant-1.7.1-bin.tar.bz2 -C "$DESTDIR"
RUN_SH="$DESTDIR/run.sh"
if [ ! -f "$RUN_SH" ]; then
  cat >"$RUN_SH" <<'EOF'
#!/bin/sh
cd "$(dirname "$0")/client"
RUN_SH_DIR="$(dirname "$0")"
PATH="$RUN_SH_DIR/apache-ant-1.7.1/bin:$PATH"
exec ant run
EOF
  chmod +x "$RUN_SH"
fi
DESKTOP=$(xdg-user-dir DESKTOP 2>/dev/null || true)
if [ -z "$DESKTOP" ]; then
  DESKTOP="$HOME/Desktop"
fi
DESKTOP_FILE="$DESKTOP/OpenDolphinClient.desktop"
if [ ! -f "$DESKTOP_FILE" ]; then
  cat >"$DESKTOP_FILE" <<EOF
#!/usr/bin/env xdg-open
[Desktop Entry]
Version=1.0
Encoding=UTF-8
Name=OpenDolphin Client
Exec=$DESTDIR/run.sh
Icon=$DESTDIR/client/src/open/dolphin/resources/images/OpenDolphin-ICO.ico
StartupNotify=true
Type=Application
Terminal=false
EOF
  chmod +x "$DESKTOP_FILE"
fi

DESKTOP_FILE="$DESKTOP/OpenDolphinServer.desktop"
SERVER_RUNNER=/opt/OpenDolphin/util/open-dolphin-server.rb
if [ ! -f "$DESKTOP_FILE" -a -x "$SERVER_RUNNER" ]; then
  cat >"$DESKTOP_FILE" <<EOF
#!/usr/bin/env xdg-open
[Desktop Entry]
Version=1.0
Encoding=UTF-8
Name=OpenDolphin Server
Exec=$SERVER_RUNNER
Icon=$DESTDIR/client/src/open/dolphin/resources/images/OpenDolphin-ICO.ico
StartupNotify=true
Type=Application
Terminal=false
EOF
  chmod +x "$DESKTOP_FILE"
fi

DESKTOP_FILE="$DESKTOP/open-dolphin-backup-usb-storage.desktop"
PROGRAM=/usr/bin/open-dolphin-backup-usb-storage
if [ ! -f "$DESKTOP_FILE" -a -x "$PROGRAM" ]; then
  cat >"$DESKTOP_FILE" <<EOF
#!/usr/bin/env xdg-open
[Desktop Entry]
Version=1.0
Encoding=UTF-8
Name=OpenDolphin USB Backup
Name[ja_JP]=OpenDolphin USB記憶装置へのバックアップ
Exec=$PROGRAM
Icon=/usr/share/open-dolphin-backup-usb-storage/gnome-dev-removable-usb.png
StartupNotify=true
Type=Application
Terminal=false
EOF
  chmod +x "$DESKTOP_FILE"
fi
      SH
      f.chmod 0755
    end
  end

  def install_OpenDolphin
    xsystem("mkdir", "-p", INSTALL_DIR)
    xsystem("tar", "--checkpoint=1000", "-zxf", File.expand_path("../OpenDolphin-Server.tar.gz"), "-C", INSTALL_DIR)
    [
      "#{JBOSS_DEFAULT}/data",
      # "#{JBOSS_DEFAULT}/log", # log writes to ~/.OpenDolphin if available
      "#{JBOSS_DEFAULT}/tmp",
      "#{JBOSS_DEFAULT}/work",
    ].each do |path|
      xsystem("chown", "-R", "dolphin:dolphin", path)
      xsystem("chmod", "-R", "g+ws", path)
    end

    util_dir = "#{INSTALL_DIR}/util"
    xsystem("mkdir", "-p", util_dir)
    xsystem("install", "-t", util_dir, *Dir["*.rb"])

    xsystem("mkdir", "-p", INIT_DB_DIR)
    xsystem("tar", "--checkpoint=500", "-zxf", File.expand_path("../OpenDolphin-init-db.tar.gz"), "-C", INIT_DB_DIR)
    xsystem("tar", "--checkpoint=500", "-zxf", File.expand_path("../OpenDolphin-lib.tar.gz"), "-C", INIT_DB_DIR)
    xsystem("tar", "--checkpoint=500", "-jxf", File.expand_path("../util/archive/apache-ant-1.7.1-bin.tar.bz2"), "-C", INIT_DB_DIR)
    # extract icons
    xsystem("tar", "--checkpoint=500", "-zxf", File.expand_path("../OpenDolphin-client.tar.gz"), "-C", INSTALL_DIR, "client/src/open/dolphin/resources/images")
  end

  def adduser_opendolphin
    if File.exist?("/etc/debian_version")
      # depends debian's adduser
      argv = [
        "/usr/sbin/adduser",
        "--system",
        "--quiet",
        "--home", INSTALL_DIR,
        "--shell", "/bin/bash",
        "--group",
        "--gecos", "OpenDolphin Server",
        "dolphin"
      ]
      xsystem(*argv)
      if ENV["SUDO_USER"]
        xsystem("/usr/sbin/adduser", "--quiet", ENV["SUDO_USER"], "dolphin")
      end
    end
  end

  def pwgen
    require "base64"
    require "openssl"
    Base64.encode64(OpenSSL::Random.random_bytes(6)).chomp
  end

  def jboss_set_postgres_password(password)
    path = "#{JBOSS_DEFAULT}/deploy/postgres-ds.xml"
    xsystem("chown", "root:dolphin", path)
    xsystem("chmod", "0640", path)
    replace_file(path, true) do |content|
      content.sub(/(<password>)(.+)(<\/password>)/) do
        # password generated by base64 includes [A-Za-z0-9+/] only
        $1 + password + $3
      end
    end
  end

  def pg_createuser
    xsystem("sudo", "-u", "postgres", "createuser", "-S", "-D", "-R", "dolphin")
  rescue InstallAbort
    puts(_("WARNING: createuser dolphin failed. OK if dolphin user already created."))
  end

  def pg_set_password(password)
    # password generated by base64 does not include bad characters
    xsystem("sudo", "-u", "postgres", "psql", "-c", "alter user dolphin password '#{password}'")
  end

  def pg_createdb
    xsystem("sudo", "-u", "postgres", "createdb", "dolphin")
  rescue InstallAbort
    puts(_("WARNING: createdb dolphin failed. OK if dolphin database already created."))
  end

  def open_dolphin_server_title
    _("OpenDolphin Server for Initialize Dolphin Database")
  end

  def start_open_dolphin_server_localhost_only
    require "etc"
    dolphin_gid = Etc.getgrnam("dolphin").gid
    @server_pid = fork do
      Process.egid = dolphin_gid
      Process.gid = dolphin_gid
      argv = [
        # "sudo", "-u", "dolphin", # Cannot open display
        "#{INSTALL_DIR}/util/open-dolphin-server.rb",
        "--title", open_dolphin_server_title,
        "--",
        "./bin/run.sh",
        "-b", "127.0.0.1",
      ]
      exec(*argv)
    end
  end

  def wait_open_dolphin_server_started
    require "open-dolphin-server"
    until OpenDolphinServer.open_dolphin_server_started?("127.0.0.1", 10)
      puts(_("Wait for starting %s") % open_dolphin_server_title)
      sleep 5
    end
  end

  def open_dolphin_client_ant_init_db
    Dir.chdir(INIT_DB_DIR) do
      orig_path = ENV["PATH"]
      ENV["PATH"] = "#{Dir.pwd}/apache-ant-1.7.1/bin:#{orig_path}"
      xsystem("ant", "init-db")
    end
  end

  def stop_open_dolphin_server
    Process.kill("INT", @server_pid)
      puts(_("Wait for stopping %s") % open_dolphin_server_title)
    Process.waitpid(@server_pid)
  end

  def self.run
    need_root_privilege
    if DISTRIBUTION == "etch"
      install_bpo_keyring
      set_bpo_apt_line
    end
    apt_update
    apt_upgrade
    apt_install_packages
    if false
      postgresql_conf__listen_address_all
      ipaddr, prefixlen = get_inet_addr_and_prefixlen
      pg_hba_conf__allow_dolphin(ipaddr, prefixlen)
      restart_postgresql
    end
    adduser_opendolphin
    install_OpenDolphin
    dolphin_dbuser_password = pwgen
    jboss_set_postgres_password(dolphin_dbuser_password)
    pg_createuser
    pg_set_password(dolphin_dbuser_password)
    pg_createdb
    start_open_dolphin_server_localhost_only
    wait_open_dolphin_server_started
    open_dolphin_client_ant_init_db
    stop_open_dolphin_server
  end
end


if __FILE__ == $0
  case ARGV[0]
  when "pack"
    OpenDolphinServerInstaller.pack_OpenDolphin(ARGV[1] || "OpenDolphin-installer")
  else
    Dir.chdir(File.dirname(__FILE__))
    OpenDolphinServerInstaller.run
  end
end
