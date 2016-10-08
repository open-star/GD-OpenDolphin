# -*- coding: utf-8 -*-

require 'fileutils'

module OpenDolphin; end

module OpenDolphin::SetupUtils

  module I18n
    Messages = Hash.new {|h, k| lambda {|message| message } }
    Messages['ja'] = Hash.new {|h, k| k }
    MESSAGES = Messages[(/ja/ =~ ENV['LANG']) ? 'ja' : 'en']
    def _(message)
      MESSAGES[message]
    end
  end

  class InstallAbort < Exception; end

  case File.read("/etc/debian_version").chomp
  when "4.0"
    DISTRIBUTION = "etch".freeze
  when /\A5\./
    DISTRIBUTION = "lenny".freeze
  else
    DISTRIBUTION = `lsb_release -cs`.to_s.chomp.freeze
  end

  case DISTRIBUTION
  when "etch"
    PG_VERSION = "8.1".freeze
  when "lenny"
    PG_VERSION = "8.4".freeze
  when "hardy"
    PG_VERSION = "8.3".freeze
  else
    PG_VERSION = `aptitude search '~n^postgresql-8..$'`[/8\.\d/].freeze
  end

  Messages = Hash.new {|h, k| lambda {|message| message } }
  Messages['ja'] = Hash.new {|h, k| k }

  # For utils
  I18n::Messages["ja"]["FAILED: %p"] = "実行失敗: %p"
  I18n::Messages["ja"]["OK: %s unchanged"] = "OK: %s の変更なし"
  I18n::Messages["ja"]["WARNING: %s unchanged"] = "WARNING: %s を変更しませんでした。"
  I18n::Messages["ja"]["Apply above change (Y/N) [default=Y] ? "] = "上の変更を適用しますか? (Y/N) [default=Y] "
  I18n::Messages["ja"]["This program needs root privilege."] = "このプログラムには管理者権限が必要です。"

  def xsystem(*args)
    STDERR.puts args.join(" ")
    system(*args) or raise InstallAbort, (_("FAILED: %p") % [args])
  end

  def xsystem_without_raise(*args)
    STDERR.puts args.join(" ")
    system(*args)
  end

  def confirm(message)
    STDERR.write message
    /^n/i !~ gets
  end

  APT = "aptitude".freeze
  FU = FileUtils::Verbose

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

  def need_root_privilege
    if Process.uid != 0
      STDERR.puts(_("This program needs root privilege."))
      exit(false)
    end
  end

  def restart_postgresql
    postgres_script = "/etc/init.d/postgresql-#{PG_VERSION}"
    xsystem(postgres_script, "restart")
  rescue InstallAbort
    xsystem(postgres_script, "start")
  end
end

