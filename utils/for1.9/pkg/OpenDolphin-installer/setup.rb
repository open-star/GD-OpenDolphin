#!/usr/bin/ruby1.8 -Ku

$:.unshift File.join(File.dirname(__FILE__), 'bin')
$:.unshift File.join(File.dirname(__FILE__), 'bin/lib')
require 'dolphin_installer_utils'

include OpenDolphin::SetupUtils::I18n

Messages['ja']['Start setup...'] = 'セットアップを開始します...'
Messages['ja']['...done'] = 'セットアップを完了しました'
Messages['ja']['OpenDolphin setup tools'] = 'OpenDolphin セットアップツール'
Messages['ja']['You can skip some tool uses following options:'] = '以下のオプションを使用することで、手順を省略することが出来ます'
Messages['ja']['    -I: skip package install'] = '    -I: パッケージのインストールをスキップする'
Messages['ja']['    -P: skip PostgreSQL setup'] = '    -P: PostgreSQL のセットアップをスキップする'
Messages['ja']['    -T: skip SSL setup'] = '    -T: SSL 通信のセットアップをスキップする'

TOOLS = [
  'package_installer',
  'postgres_initializer',
  'truststore_server',
]

def show_help_then_exit
  puts _('OpenDolphin setup tools')
  puts
  puts _('You can skip some tool uses following options:')
  puts
  puts _('    -I: skip package install')
  puts _('    -P: skip PostgreSQL setup')
  puts _('    -T: skip SSL setup')
  exit
end

execute_queue = TOOLS.dup
unless ARGV.empty?
  ARGV.each do |arg|
    execute_queue.delete case arg
      when '-I' then 'package_installer'
      when '-P' then 'postgres_initializer'
      when '-T' then 'truststore_server'
      when /-h(?:elp)?/ then show_help_then_exit
    end
  end
  ARGV.clear
end

ExecuteMap = {
  'package_installer'    => 'PackageInstaller',
  'postgres_initializer' => 'PostgresInitializer',
  'truststore_server'    => 'TrustStore',
}

result = {}

puts _("Start setup...")
execute_queue.each do |tool|
  require tool
  result[tool] = Object.const_get(ExecuteMap[tool]).run
end
puts _("...done")

