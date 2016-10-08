#!/usr/bin/ruby1.8 -Ku
# -*- coding: utf-8 -*-
# copyright (c) 2009 Good-Day Inc.

require 'optparse'

class OpenDolphinBackupReplace
  TITLE = "OpenDolphin データベース書き戻しプログラム"

  def zenity(*args)
    r,w = IO.pipe
    pid = fork do
      r.close
      STDOUT.reopen(w)
      exec("zenity", "--title", TITLE, *args)
      exit!(1)
    end
    w.close
    pid, status = Process.waitpid2(pid)
    [status, r.read]
  end

  def error(text)
    zenity("--error", "--text", text)
    exit(2)
  end

  def info(text)
    zenity("--info", "--text", text)
  end

  def question(text)
    zenity("--question", "--text", text)
  end

  def warning(text)
    zenity("--warning", "--text", text)
  end

  def canceled
    info("キャンセルにより終了します。")
    exit(1)
  end

  def sudo(*root_command_line)
    puts "execute: sudo #{root_command_line.inspect}"
    system("sudo", *root_command_line)
  end

  def get_hdd_names
    return @hdd_names if defined?(@hdd_names)
    hdd = Hash.new
    if %r!^/dev/(sd.).\s+\d+\s+\d+\s+\d+\s+\d+% /\Z! =~ `df /`
      hdd[:boot] = $1
      case hdd[:boot]
      when 'sda'
        hdd[:other] = 'sdb'
        hdd[:src], hdd[:dst] = '1台目のHDD', '2台目のHDD'
        @hdd_names = hdd
        return hdd
      when 'sdb'
        hdd[:other] = 'sda'
        hdd[:src], hdd[:dst] = '2台目のHDD', '1台目のHDD'
        @hdd_names = hdd
        return hdd
      end
    end
    error("マウントしている HDD が不明です。")
  end

  DUMP_FILE_PATTERN = /\/dolphin\.dump\.(?:([sh]d[a-d])\.)?\d{8}\.gz\z/

  def get_pg_dump_files
    return @pg_dump_files if defined?(@pg_dump_files)
    backup_dir = `. /etc/default/orca-box-backup-local; echo $BACKUP_DIR`.to_s.chomp
    list = Dir.glob("#{backup_dir}/*.gz")
    h = Hash.new
    list.each do |s|
      if DUMP_FILE_PATTERN =~ s
        disk = $1
        drive = ""
        case disk
        when "sda"
          drive = "[1st HDD]"
        when "sdb"
          drive = "[2nd HDD]"
        end
        stat = File.stat(s)
        time = stat.mtime.strftime("%Y年%m月%d日%H時%M分")
        k = "#{time}#{drive}(#{stat.size}バイト)"
        v = Hash.new
        v[:file] = s
        v[:disk] = disk
        v[:stat] = stat
        h[k] = v
      end
    end
    @pg_dump_files = h
  end

  def newest_dump_file
    hdd = get_hdd_names
    h = get_pg_dump_files
    newest = nil
    h.each_value do |v|
      if v[:disk] == hdd[:other]
        unless newest && v[:stat].mtime < newest[:stat].mtime
          newest = v
        end
      end
    end
    newest
  rescue
    puts $!.inspect
    puts $!.backtrace
    nil
  end

  def select_pg_dump_file
    selected = nil
    h = get_pg_dump_files
    while selected.nil? or selected.empty? or not(h.key?(selected))
      status, selected = zenity("--list",
        "--text", "書き戻すバックアップを選んでください。",
        "--column", "バックアップファイル",
        *h.keys.sort.reverse)
      selected.chomp! if selected
      unless status.success?
        canceled
      end
    end
    h[selected][:file]
  end

  def run_restore(hdd, opt)
    newest = newest_dump_file
    if newest
      status, = question("#{hdd[:src]}から起動しました。
データベースを最新のバックアップから書き戻しますか？")
      if status.success?
        filepath = newest[:file]
      end
    end
    unless filepath
      status, = question("#{hdd[:src]}から起動しました。
バックアップを選択してデータベースに書き戻しますか？")
      unless status.success?
        canceled
      end
      filepath = select_pg_dump_file
    end

    status, = question("本当に書き戻しますか？
よければ OK を押してください。
#{hdd[:src]}での変更は消えてしまいます。")
    status, = question(question_text)
    unless status.success?
      canceled
    end
    action_text = "データベースの書き戻し"
    exit_text = "終了します。"
    begin
      do_restore(opt[:target_db], opt[:db_user], opt[:stop_daemons])
      info("#{action_text}に成功しました。#{exit_text}")
    rescue => e
      puts e.inspect
      puts e.backtrace
      error("#{action_text}に失敗しました。#{exit_text}\n\n理由:#{e.message}")
    end
  end

  def do_initd(daemons, action)
    daemons.each do |daemon|
      initd_script = "/etc/init.d/#{daemon}"
      if File.exist?(initd_script)
        sudo(initd_script, action) or yield(daemon)
      else
        puts "#{initd_script} がないので無視します。"
      end
    end
  end

  def do_restore(target_db, db_user, *daemons)
    do_initd(daemons, "stop") do |daemon|
      raise "#{daemon} の停止に失敗しました。"
    end
    sudo("-u", db_user, "dropdb", target_db) or
      puts "#{target_db} の削除に失敗しましたが、無視します。"
    sudo("-u", db_user, "createdb", target_db) or
      raise "#{target_db} の作成に失敗しました。"
    # decompress gzip file
    replace_log = "#{filename}.#{Time.now.strftime('%Y%m%d%H%M%S')}.replace.dump.log"
    if /(.+)\.gz\z/ =~ filename
      sudo("zcat #{filename} | sudo -u #{db_user} psql -e #{target_db} 2>&1 | tee #{replace_log}") or
        puts "データベースの書き戻しに失敗しました。"
    else
      sudo("sudo -u #{db_user} psql -e #{target_db} < #{filename} 2>&1 | tee #{replace_log}") or
        puts "データベースの書き戻しに失敗しました。"
    end
  ensure
    do_initd(daemons.reverse, "start") do
      # ignore
    end
  end

  def check_on_tty
    unless STDOUT.tty?
      error("端末上で起動してください。")
    end
  end

  def check_non_root_exec
    if Process.uid == 0
      error("root権限で実行しないでください。")
    end
  end

  def check_root_exec
    if Process.uid != 0
      error("このプログラムはROOT権限で実行してください。")
    end
  end

  # 2台目から起動してpg_dumpから復元する必要があれば
  # opt[:restore]をtrueに設定する。
  def check_need_restore(opt)
    newest = newest_dump_file
    found_restore_log_of_newest_dump = newest &&
      Dir.glob("#{newest[:file]}.*.replace.dump.log").find do |x|
      newest[:stat].mtime < File.mtime(x)
    end
    if newest.nil? || !found_restore_log_of_newest_dump
      opt[:restore] = true
    end
  end

  def check_proc_cmdline(opt)
    proc_cmdline = File.read("/proc/cmdline")
    return unless / orcabox=(\S+)/ =~ proc_cmdline
    cmd = $1
    case cmd
    when /format-(sd[ab])/
      disk = $1
      opt[:format] = disk
    when 'cdboot'
      check_need_restore(opt)
    end
  end

  def parse(argv, opt)
    parser = OptionParser.new
    parser.banner << "\n Example: #{File.basename($0, '.*')} --target-db=dolphin --db-user=dolphin --stop-daemons=jma-receipt"

    parser.on("--target-db=DBNAME", /\A[a-z]+\z/, "書き戻し先のデータベース") do |s|
      opt[:target_db] = s
    end
    parser.on("--db-user=DBUSER", /\A[a-z\-]+\z/, "データベースに接続するユーザアカウント") do |s|
      opt[:db_user] = s
    end
    parser.on("--stop-daemons=DAEMON1,DAEMON2", Array, "データベース書き戻し中に止めるデーモン") do |a|
      opt[:stop_daemons] = a
    end
    parser.on_tail("--help", "show this message") {puts parser; exit}

    parser.parse!(argv)
  end

  def run(*argv)
    opt = {
      :target_db => nil,
      :db_user => nil,
      :stop_daemons => [],
    }
    check_on_tty
    parse(argv, opt)
    check_root_exec
    check_proc_cmdline(opt)
    hdd = get_hdd_names
    if opt[:restore]
      run_restore(hdd, opt)
    end
  rescue
    puts $!.inspect
    puts $!.backtrace
    error("エラーにより終了します。")
  end
end

if __FILE__ == $0
  OpenDolphinBackupReplace.new.run(*ARGV)
end
