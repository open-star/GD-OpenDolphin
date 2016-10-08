#!/usr/bin/ruby1.8 -Ku
# -*- coding: utf-8 -*-
=begin
= run daemon in terminal
== usage
 $ daemon-in-terminal.rb -- some-command [command arguments...]

* left click tray icon: toggle terminal
* right click tray icon: show menu

== License (MIT License)

Copyright (c) 2009 Kazuhiro NISHIYAMA

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

require "gtk2"
require "optparse"
require "vte"

class DaemonInTerminal
  Messages = Hash.new{|h,k| proc{|message|message} }
  Messages["ja"] = Hash.new{|h,k| k }
  Messages["ja"]["command failed or exited: %s"] = "コマンドの実行に失敗したか、既に終了しています: %s"
  Messages["ja"]["command failed: %s"] = "コマンドの実行に失敗しました: %s"
  Messages["ja"]["Show Terminal"] = "端末表示"
  Messages["ja"]["Terminate"] = "終了"
  Messages["ja"]["Kill"] = "強制終了"
  Messages["ja"]["Really kill?"] = "本当に強制終了しますか?"
  Messages["ja"]["%s starting. Wait a moment."] = "%s を起動しています。しばらくお待ちください。"
  Messages["ja"]["%s started"] = "%s が起動しました。"
  Messages["ja"]["%s exited"] = "%s が終了しました。"
  Messages["ja"]["%s already running"] = "%s は既に起動中です。"

  DEBUG = $DEBUG

  def initialize(options, argv, locale=nil)
    @options = options
    @title = options[:title]
    if options[:icon_path]
      @icon = Gdk::Pixbuf.new(options[:icon_path])
    else
      @icon = nil
    end
    if options[:starting_icon_path]
      @starting_icon = Gdk::Pixbuf.new(options[:starting_icon_path])
      @started_icon = @icon
    else
      @starting_icon = @icon
      @started_icon = nil
    end
    @starting_tooltip = options[:starting_tooltip] || options[:tooltip] || @title
    @started_tooltip = options[:tooltip] || @title
    @argv = argv
    if locale
      @messages = Messages[locale]
    else
      if /^ja/ =~ ENV["LANG"]
        @messages = Messages["ja"]
      else
        @messages = Messages["en"]
      end
    end
  end

  def run
    init_window
    init_signal
    init_menu
    if defined?(Gtk::StatusIcon)
      init_status_icon
    else
      require "gtktrayicon"
      init_gtk_tray_icon
    end
  end

  def _(message)
    @messages[message]
  end

  def init_window
    @window = Gtk::Window.new(@title)
    @window.signal_connect("destroy"){Gtk.main_quit}
    @window.signal_connect("delete-event") do
      toggle_window
      true # do not destroy
    end
    init_terminal
    @window.icon = @starting_icon if @starting_icon
    scrollbar = Gtk::VScrollbar.new
    scrollbar.adjustment = @terminal.adjustment
    box = Gtk::HBox.new
    box.pack_start(@terminal)
    box.pack_start(scrollbar, false)
    @window.add(box)
    @window.realize # avoid "Gtk-CRITICAL: **:gtk_window_realize_icon: assertion `info->icon_pixmap == NULL' failed" on etch
  end

  def init_terminal
    @started = false
    @terminal = Vte::Terminal.new
    @terminal.scroll_on_output = true
    @terminal.scrollback_lines = 1000
    @terminal.set_font("Monospace 10", Vte::TerminalAntiAlias::FORCE_ENABLE)
    @terminal.signal_connect("child-exited") do |widget|
      if @started
        notify(_("%s exited") % @title)
      else
        @window.show_all
        error(_("command failed or exited: %s") % @argv[0])
      end
      Gtk.main_quit
    end
    @terminal.signal_connect("window-title-changed") do |widget|
      @window.title = @terminal.window_title
    end
    if already_running?
      error(_("%s already running") % @title)
      raise "already running: #{@argv.inspect}"
    end
    @pid = @terminal.fork_command(@argv[0], @argv)
    if @pid == -1
      error(_("command failed: %s") % @argv[0])
      raise "fork_command failed: #{@argv.inspect}"
    end
    notify(_("%s starting. Wait a moment.") % @title, false)
    init_check_started
  end

  def init_signal
    forward_signal = proc do |sig|
      p [:forward_signal, sig, @pid] if DEBUG
      Process.kill sig, -@pid if @pid
    end
    trap("HUP", &forward_signal)
    trap("INT", &forward_signal)
    trap("TERM", &forward_signal)
    trap("QUIT", &forward_signal)
    trap("PIPE", &forward_signal)
  end

  def notify_started
    @started = true
    if @started_icon
      @window.icon = @started_icon
      @s_icon.pixbuf = @started_icon
    end
    @set_tip.call(@started_tooltip)
    message = _("%s started") % @title
    notify(message)
  end

  def init_check_started
    GLib::Timeout.add(@options[:check_interval]) do
      begin
        Process.kill(0, @pid)
        if daemon_started?
          notify_started
        end
        !@started
      rescue Errno::ESRCH
        false
      end
    end
  end

  def init_menu
    @menu = Gtk::Menu.new
    @show_terminal_menu_item = Gtk::CheckMenuItem.new(_("Show Terminal"))
    @show_terminal_menu_item.signal_connect("toggled") do |widget|
      toggle_window_internal
    end
    @menu.append(@show_terminal_menu_item)
    item = Gtk::MenuItem.new(_("Terminate"))
    item.signal_connect("activate") do |widget|
      terminate
    end
    @menu.append(item)
    item = Gtk::MenuItem.new(_("Kill"))
    item.signal_connect("activate") do |widget|
      question(_("Really kill?")) do
        Process.kill("KILL", -@pid)
      end
    end
    @menu.append(item)
    @menu.show_all
  end

  def toggle_window
    @show_terminal_menu_item.active = !@show_terminal_menu_item.active?
  end

  def toggle_window_internal
    if @window.visible?
      @window.hide
    else
      @window.show_all
    end
  end

  def init_status_icon
    @s_icon = Gtk::StatusIcon.new
    if @icon
      @s_icon.pixbuf = @starting_icon
    else
      @s_icon.stock = Gtk::Stock::ABOUT # dummy
    end
    @set_tip = proc do |tip|
      p [:@set_tip, tip] if DEBUG
      @s_icon.tooltip = tip
    end
    @set_tip.call(@starting_tooltip)
    @s_icon.signal_connect("activate") do |widget|
      toggle_window
    end
    @s_icon.signal_connect("popup_menu") do |widget, button, activate_time|
      @menu.popup(nil, nil, button, activate_time)
    end
  end

  def init_gtk_tray_icon
    tray = Gtk::TrayIcon.new(@title)
    tooltips = Gtk::Tooltips.new
    @set_tip = proc do |tip|
      tooltips.set_tip(tray, tip, nil)
    end
    @set_tip.call(@starting_tooltip)
    if @starting_icon
      @s_icon = Gtk::Image.new(@starting_icon)
    else
      @s_icon = Gtk::Label.new(@title) # dummy
    end
    e_box = Gtk::EventBox.new.add(@s_icon)
    e_box.signal_connect("button_press_event") do |widget, event|
      case event.button
      when 1
        toggle_window
      when 3
        @menu.popup(nil, nil, event.button, event.time)
      end
    end
    tray.add(e_box)
    tray.show_all
  end

  def dialog(parent, type, buttons, message)
    flags = Gtk::Dialog::DESTROY_WITH_PARENT
    dialog = Gtk::MessageDialog.new(parent, flags, type, buttons, message)
    dialog.title = @title
    dialog.modal = false
    dialog.run do |response|
      case response
      when Gtk::Dialog::RESPONSE_OK
        yield if block_given?
      end
    end
    dialog.destroy
  end

  def error(message, parent=@window)
    type = Gtk::MessageDialog::ERROR
    buttons = Gtk::MessageDialog::BUTTONS_OK
    dialog(parent, type, buttons, message) do
      yield if block_given?
    end
  end

  def info(message, parent=@window)
    type = Gtk::MessageDialog::INFO
    buttons = Gtk::MessageDialog::BUTTONS_OK
    dialog(parent, type, buttons, message) do
      yield if block_given?
    end
  end

  def question(message, parent=@window)
    type = Gtk::MessageDialog::QUESTION
    buttons = Gtk::MessageDialog::BUTTONS_OK_CANCEL
    dialog(parent, type, buttons, message) do
      yield if block_given?
    end
  end

  def notify(message, fallback=true)
    unless system("notify-send", @title, message)
      info(message) if fallback
    end
  end

  # customize
  def terminate
    Process.kill("TERM", -@pid)
  end

  # customize
  def daemon_started?
    true
  end

  # customize
  def already_running?
    false
    #daemon_started?
  end

  def self.run(argv, h={}, default_command_argv=nil)
    h[:title] ||= File.basename($0)
    h[:starting_icon_path] ||= nil
    h[:icon_path] ||= nil
    h[:tooltip] ||= nil
    h[:starting_tooltip] ||= nil
    h[:check_interval] ||= 1000 # 1s
    h[:dir] ||= nil
    argv.options do |opts|
      opts.banner << " -- command [arguments...]"

      opts.on("--title=TITLE", "window title") do |s|
        h[:title] = s
      end
      opts.on("--starting-window-icon=ICONPATH", "path to icon file while starting") do |s|
        h[:starting_icon_path] = s
      end
      opts.on("--window-icon=ICONPATH", "path to icon file") do |s|
        h[:icon_path] = s
      end
      opts.on("--starting-window-icon=ICONPATH", "path to icon file while starting") do |s|
        h[:starting_icon_path] = s
      end
      opts.on("--tooltip=TOOLTIP", "tooltip of tray icon") do |s|
        h[:tooltip] = s
      end
      opts.on("--starting-tooltip=TOOLTIP", "tooltip of tray icon while starting") do |s|
        h[:starting_tooltip] = s
      end
      opts.on("--check-interval=MSEC", Integer, "interval of checking daemon started (msec)") do |n|
        h[:check_interval] = n
      end
      opts.on("--dir=DIRECTORY", "cd to directory, before run daemon") do |dir|
        h[:dir] = dir
      end

      opts.parse!
      if argv.empty?
        if default_command_argv
          argv = default_command_argv
        else
          puts opts
          exit
        end
      end

      if h[:dir]
        Dir.chdir(h[:dir])
      end

      daemon = self.new(h, argv)
      daemon.run
    end
  end
end

if __FILE__ == $0
  DaemonInTerminal.run(ARGV)
  Gtk.main
end
