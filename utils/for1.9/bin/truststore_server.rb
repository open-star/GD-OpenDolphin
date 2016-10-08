#! /usr/bin/ruby1.8
# -*- coding: utf-8 -*-

#
# TrustStore Server: destributes OpenDolphin truststore for client
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
#
$:.unshift File.join(File.dirname(__FILE__), 'lib')
require 'dolphin_installer_utils'

require 'webrick'

include OpenDolphin::SetupUtils
include OpenDolphin::SetupUtils::I18n

module TrustStore

  Messages['ja']['Use SSL connection (Y/N) [default=Y] ? ']     = 'SSL 通信を利用しますか (Y/N) [default=Y] ? '
  Messages['ja']['Delete old trust store (Y/N) [default=Y] ? '] = '古い 証明書リスト を削除しますか (Y/N) [default=Y] ? '
  Messages['ja']['Checking needed files.']             = '必要なファイルを確認しています...'
  Messages['ja']['Certification file does not exist.'] = 'サーバ証明書が存在していません.'
  Messages['ja']['Deleting old trust store...']        = '古い 証明書リスト を削除しています...'
  Messages['ja']['Deleted.']                           = '削除しました.'
  Messages['ja']['Check completed.']                   = 'チェックが完了しました.'
  Messages['ja']['Creating TrustStore...']             = '証明書リスト を作成しています.'
  Messages['ja']['Completed.']                         = '作成が完了しました.'
  Messages['ja']['Interrupted.']                       = '中断しました'
  Messages['ja']['Configuring postgresql.conf to use ssl...'] = 'SSL を使用するように設定ファイルを修正します.'
  Messages['ja']['Configuring pg_hba.conf to use ssl...']     = 'SSL を使用するように権限ファイルを修正します.'

  def self.run(debug = false, cert_file = nil)

    return self.name if debug

    return false unless confirm _('Use SSL connection (Y/N) [default=Y] ? ')

    trap :INT do
      STDERR.puts(_("Interrupted."))
      exit false
    end

    truststore_name = TrustStore::Manager.run(cert_file)
    settings = {
      :server_name   => 'Dolphin TrustStore Server',
      :port          => 1026,
      :document_root => './',
      :truststore    => truststore_name,
    }
    TrustStore::Server.new(settings).start
  end

  class Manager

    DEFAULT_CERT_FILE   = '/etc/ssl/certs/ssl-cert-snakeoil.pem'.freeze

    ALIAS_NAME  = 'dolphinDBServer'.freeze
    TRUST_STORE = 'dolphin.trustStore'.freeze

    def self.run(cert_file)
      need_root_privilege

      cnf_setted = pg_enable_postgres_ssl
      hba_setted = pg_required_ssl_setting
      restart_postgresql if cnf_setted || hba_setted
      create cert_file
    end

    class << self
      def create(cert_file = DEFAULT_CERT_FILE)

        # FIXME:
        cert_file ||= DEFAULT_CERT_FILE

        check_needed_files cert_file
        create_trust_store cert_file

        TRUST_STORE
      end

      def check_needed_files(cert_file)
        puts _('Checking needed files.')
        unless File.exist?(cert_file)
          abort _('Certification file does not exist.')
        end
        puts _('Check completed.')
      end

      def clear_old_trust_store
        puts _('Deleting old trust store...')
        File.unlink(TRUST_STORE)
        puts _('Deleted.')
      end

      def create_trust_store(cert_file)
        if File.exist?(TRUST_STORE)
          return unless confirm _("Delete old trust store (Y/N) [default=Y] ? ")
          clear_old_trust_store
        end
        puts _('Creating TrustStore...')
        system "keytool -import -alias #{ALIAS_NAME} -file #{cert_file} -keystore #{TRUST_STORE}"
        puts _('Completed.')
      end

      def pg_required_ssl_setting
        puts _('Configuring pg_hba.conf to use ssl...')
        replace_file("/etc/postgresql/#{PG_VERSION}/main/pg_hba.conf") do |content|
          if /^hostssl dolphin     dolphin.+$/ !~ content
            content.sub(/^host   ( dolphin     dolphin.+)$/) { 'hostssl' + $1 }
          else
            content
          end
        end
      end

      def pg_enable_postgres_ssl
        puts _('Configuring postgresql.conf to use ssl...')
        need_restart = replace_file("/etc/postgresql/#{PG_VERSION}/main/postgresql.conf") do |content|
          content.sub /ssl\s?\=\s?false/,'ssl = true'
        end
      end
    end
  end

  class Server

    def initialize(settings)
      @server = new_server(settings)
      @server.mount_proc('/cert') do |req, res|
        res.set_redirect(WEBrick::HTTPStatus::Found, "/#{settings[:truststore]}")
      end
      trap(:INT) { @server.shutdown }
    end

    def start
      @server.start
    end

    private
    def new_server(settings)
      WEBrick::HTTPServer.new config(settings)
    end

    def config(settings)
      {
        :ServerSoftware => settings[:server_name],
        :Port           => settings[:port],
        :DocumentRoot   => settings[:document_root],
        :DocumentRootOptions => {
          :FancyIndexing => false,
          :FileCallback => lambda {|req, res|
            unless /#{settings[:truststore]}/ =~ req.path
              res.status = WEBrick::HTTPStatus::Forbidden
            end
          },
        },
      }
    end
  end
end

if $0 == __FILE__
  TrustStore.run(false, ARGV.empty? ? nil : ARGV.shift)
end

