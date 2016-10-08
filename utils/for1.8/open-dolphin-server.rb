#!/usr/bin/ruby1.8 -Ku
# -*- coding: utf-8 -*-
=begin
= run OpenDolphin Server in systray
== usage
 $ ruby1.8 open-dolphin-server.rb &

* left click tray icon: toggle terminal
* right click tray icon: show menu

If you need log files, see ~/.OpenDolphin/*.log instead of
/opt/OpenDolphin/jboss-4.2.2.GA/server/default/log/*.log.

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
require "daemon-in-terminal"
require "net/http"
require "timeout"

class OpenDolphinServer < DaemonInTerminal
  DaemonInTerminal::Messages["ja"]["%d clients connected. Really terminate?"] =
    "%d 件のクライアントが接続しています。本当に終了しますか?"
  DaemonInTerminal::Messages["ja"]["Really terminate?"] =
    "本当に終了しますか?"
  DaemonInTerminal::Messages["ja"]["You needs dolphin group privilege. Please run `sudo adduser $USER dolphin`."] =
    "dolphin グループの権限が必要です。「sudo adduser $USER dolphin」などで dolphin グループに所属してログインし直してください。"
  DaemonInTerminal::Messages["ja"]["Run Client"] = "クライアント起動"

  # override
  def terminate
    client_count = `netstat -nt`.scan(/:1098\s+\S+\s+ESTABLISHED/).size
    if 0 < client_count
      message = _("%d clients connected. Really terminate?") % client_count
    else
      message = _("Really terminate?")
    end
    question(message) do
      Process.kill("INT", -@pid)
    end
  end

  # override
  def daemon_started?
    unless defined?(@jboss_host)
      # @argv = ["./bin/run.sh", "-b", "192.168.x.y"]
      if i = @argv.index("-b")
        @jboss_host = @argv[i+1]
      else
        @jboss_host = "localhost"
      end
    end
    timeout = [1, @options[:check_interval]/1000.0/2].min
    self.class.open_dolphin_server_started?(@jboss_host, timeout)
  end

  # check http://localhost:8080/jmx-console/
  def self.open_dolphin_server_started?(jboss_host, timeout=1)
    require "net/http"
    require "timeout"
    req = Net::HTTP::Get.new("/jmx-console/")
    res = Net::HTTP.new(jboss_host, 8080).start do |http|
      # timeout should be less than check interval to avoid to stop GUI
      timeout(timeout) do
        http.request(req)
      end
    end
    p res if DEBUG
    if /OpenDolphinServer/ =~ res.body
      return true
    end
    return false
  rescue StandardError, Timeout::Error
    # ignore
    return false
  end

  # override
  def already_running?
    # call here to show dialog
    unless postgres_ds_xml_readable?(@options[:dir])
      message = _("You needs dolphin group privilege. Please run `sudo adduser $USER dolphin`.")
      error(message)
      raise message
    end
    daemon_started?
  end

  # override
  def notify_started
    spawn_client
    super
  end

  # override
  def init_menu
    super

    item = Gtk::MenuItem.new
    @menu.append(item)

    item = Gtk::MenuItem.new(_("Run Client"))
    item.signal_connect("activate") do |widget|
      spawn_client
    end
    @menu.append(item)
    @menu.show_all
  end

  def spawn_client
    client_dir = File.expand_path('../../client', __FILE__)
    if File.exist?("#{client_dir}/build.xml")
      system("cd #{client_dir} && env -u JAVA_OPTS ant run &")
    else
      system("$HOME/OpenDolphinClient/run.sh &")
    end
  end
end

def set_jboss_server_log_dir
  home = File.expand_path("~")
  if File.writable?(home)
    log_dir = "#{home}/.OpenDolphin"
    unless File.directory?(log_dir)
      Dir.mkdir(log_dir)
    end
    ENV["JAVA_OPTS"] = "-Xms128m -Xmx512m -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000" # from jboss-4.2.2.GA/bin/run.conf
    ENV["JAVA_OPTS"] += " -Djboss.server.log.dir=#{log_dir}"
  end
end

def postgres_ds_xml_readable?(dir)
  path = "#{dir}/server/default/deploy/postgres-ds.xml"
  File.readable?(path)
end

def set_java_home
  java_home = "/usr/lib/jvm/java-6-sun"
  if File.directory?(java_home)
    ENV["JAVA_HOME"] = java_home
  end
end

if __FILE__ == $0
  h = {}
  h[:title] = "OpenDolphin Server"
  images_dir = File.expand_path('../../client/src/open/dolphin/resources/images', __FILE__)
  unless File.directory?(images_dir)
    images_dir = File.expand_path("~/OpenDolphinClient/client/src/open/dolphin/resources/images")
  end
  h[:starting_icon_path] = "#{images_dir}/web_32.gif"
  h[:icon_path] = "#{images_dir}/OpenDolphin-ICO.ico"
  h[:check_interval] = 3000 # 3s
  h[:dir] = File.join(File.dirname(__FILE__), "../jboss-4.2.2.GA")
  set_java_home
  set_jboss_server_log_dir if Process.uid != 0
  default_command_argv = ["./bin/run.sh"]
  File.umask(002) # create files as group writable
  OpenDolphinServer.run(ARGV, h, default_command_argv)
  Gtk.main
end
