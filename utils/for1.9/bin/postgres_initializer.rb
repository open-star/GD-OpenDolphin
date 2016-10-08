#! /usr/bin/ruby1.8
# -*- coding: utf-8 -*-

#
# Setting PostgreSQL
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

$:.unshift File.join(File.dirname(__FILE__), 'lib')
require 'dolphin_installer_utils'

require "ipaddr"

class PostgresInitializer

  extend OpenDolphin::SetupUtils
  include OpenDolphin::SetupUtils::I18n

  Messages["ja"]["FAILED: pg_hba.conf: failed to allow dolphin"] = "FAILED: pg_hba.conf: dolphin の接続許可に失敗しました。"
  Messages["ja"]["WARNING: createuser dolphin failed. OK if dolphin user already created."] = "WARNING: 「createuser dolphin」に失敗しました。既に作成済みの場合は問題ありません。"
  Messages["ja"]["WARNING: createdb dolphin failed. OK if dolphin database already created."] = "WARNING: 「createdb dolphin」に失敗しました。既に作成済みの場合は問題ありません。"
  Messages["ja"]["Password: %s"] = "パスワード: %s"
  Messages["ja"]["This password is needed when access OpenDolphin database."] = "このパスワードは OpenDolphin のデータベースにアクセスする為に必要です."
  Messages["ja"]["You should minute a password carefully and don't lose your memo."] = "メモを取るかコピー&ペーストでファイルに保存するなどして、絶対になくさないようにしてください."
  Messages["ja"]["Please keep your password secret."] = "また、不用意に人に教えないようにしてください."

  SECRET_KEY = 'OpenDolphin'
  SALT       = 'CONYANCO'
  PASSWORD_FILE = '.pgpasswd'

  def self.run(debug = false)
    return self.name if debug

    need_root_privilege

    adduser_opendolphin
    pg_createuser
    pg_createdb

    postgresql_conf__listen_address '*'
    pg_hba_conf__allow_dolphin *get_inet_addr_and_prefixlen
    pg_set_password(dolphin_dbuser_temporary_password)
    restart_postgresql

    initialize_database

    password = dolphin_dbuser_password
    pg_set_password(password)
    save_password_to_file(password)
    restart_postgresql

    puts
    puts '#' * 28
    puts _("Password: %s") % [password]
    puts
    puts _("This password is needed when access OpenDolphin database.")
    puts _("You should minute a password carefully and don't lose your memo.")
    puts _("Please keep your password secret.")
    puts '#' * 28
    puts
  end

  class << self

    private

    def dolphin_dbuser_temporary_password
      'nyanco'
    end

    def adduser_opendolphin
      if File.exist?("/etc/debian_version")
        # depends debian's adduser
        argv = [
          "/usr/sbin/adduser",
          "--system",
          "--quiet",
          "--home", '/home/dolphin',
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

    def dolphin_dbuser_password
      pwgen
    end

    def pwgen
      require "base64"
      require "openssl"
      Base64.encode64(OpenSSL::Random.random_bytes(6)).chomp
    end

    def postgresql_conf__listen_address(ipaddr)
      replace_file("/etc/postgresql/#{PG_VERSION}/main/postgresql.conf") do |content|
        content.sub(/^\#? *(listen_addresses\s+=\s+)'(.+?)'/) { "#$1'#{ipaddr}'" }
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
      ipaddr = ipaddr.split('.')
      ipaddr[-1] = '0'
      ipaddr = ipaddr.join('.')
      replace_file("/etc/postgresql/#{PG_VERSION}/main/pg_hba.conf") do |content|
        [
          ["dolphin", "dolphin", "#{ipaddr}/#{prefixlen}"],
        ].each do |data|
          line = "host    %-11s %-11s %-21s md5\n" % data
          unless content.include?(line)
            content = content.sub(/^.+127\.0\.0\.1\/32.+/) { line + $& }
          end
          unless content.include?(line)
            line = ("host    %-11s %-11s %-21s md5\n" % %w[ all all 127.0.0.1/32]) + line
            content = line << "\n" << content
          end
          unless content.include?(line)
            raise InstallAbort, _("FAILED: pg_hba.conf: failed to allow dolphin")
          end
        end
        content
      end
    end

    def pg_createuser
      xsystem("sudo", "-u", "postgres", "createuser", "-S", "-d", "-R", "dolphin")
    rescue InstallAbort
      puts(_("WARNING: createuser dolphin failed. OK if dolphin user already created."))
    end

    def pg_set_password(password)
      # password generated by base64 does not include bad characters
      sql = "alter user dolphin with password '#{password}'"
      xsystem("sudo", "-u", "postgres", "psql", "-c", sql)
    end

    def pg_createdb
      xsystem("sudo", "-u", "dolphin", "createdb", "dolphin")
    rescue InstallAbort
      puts(_("WARNING: createdb dolphin failed. OK if dolphin database already created."))
    end

    def save_password_to_file(password)
      File.open(PASSWORD_FILE, 'wb') do |io|
        crypted_password = crypt(password, hex(SECRET_KEY + hostname), SALT)
        io.write crypt(hostname << ':' << crypted_password, hex(SECRET_KEY), SALT)
      end
    end

    def crypt(word, key, salt)
      require 'openssl'
      require 'base64'
      des = OpenSSL::Cipher::DES.new
      des.pkcs5_keyivgen(key, salt)
      Base64.encode64(des.encrypt.update(word) + des.final)
    end

    def hostname
      `hostname`.chop
    end

    def hex(word)
      require 'digest/sha2'
      Digest::SHA256.hexdigest(word)
    end

    def initialize_database
      unless is_initialized_database_schema?
        xsystem "java -jar init_db/DatabaseInitializer.jar"
      end
      wait_for_initialize_database_schema
    end

    def is_initialized_database_schema?
      /d_users/ =~ `sudo -u dolphin psql dolphin -c '\\dt'`
    end

    def wait_for_initialize_database_schema
      sleep 5 until is_initialized_database_schema?
    end
  end
end

if $0 == __FILE__
  PostgresInitializer.run
end

