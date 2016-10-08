#! /usr/bin/ruby1.8
# -*- coding: utf-8 -*-

#
# Install some packages for OpenDolphin
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

class PackageInstaller

  extend OpenDolphin::SetupUtils
  include OpenDolphin::SetupUtils::I18n

  Messages["ja"]["OK: debian-backports-keyring already installed."] = "OK: debian-backports-keyring がインストール済みです。"
  Messages["ja"]["OK: sun-java6-jdk is installable"] = "OK: sun-java6-jdk がインストール可能です。"
  Messages["ja"]['Failed to update list of apt packages'] = 'APT のパッケージリストの更新に失敗しました'
  Messages["ja"]['Continue to install (Y/N) [default=y] ? '] = 'インストールを継続しますか (Y/N) [default=y] ? '

  def self.run(debug = false)
    return self.name if debug

    need_root_privilege

    if OpenDolphin::SetupUtils::DISTRIBUTION == "etch"
      install_bpo_keyring
      set_bpo_apt_line
    end
    if OpenDolphin::SetupUtils::DISTRIBUTION == "lucid"
      add_partner_archive
    end
    apt_update
    apt_upgrade
    apt_install_packages
    switch_jdk_is_used_by_system
  end

  class << self
    private
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

  deb http://www.jp.backports.org/ etch-backports main contrib non-free
        LIST
      end
    end

    def add_partner_archive
      xsystem "add-apt-repository", "deb http://archive.canonical.com/ lucid partner"
    end

    def apt_update
      xsystem(APT, "update")
    rescue InstallAbort
      warn _('Failed to update list of apt packages')
      exit -1 unless confirm(_("Continue to install (Y/N) [default=y] ? "))
    end

    def apt_upgrade
      xsystem(APT, "upgrade")
    end

    def switch_jdk_is_used_by_system
      xsystem 'update-alternatives', '--config', 'java'
    end

    def apt_install_packages
      packages = [
        "postgresql-#{PG_VERSION}",
        "libopenssl-ruby1.8",
        "libgettext-ruby1.8",
        "xdg-utils",
      ]
      if DISTRIBUTION == "etch" # etch only
        %w"bin jre jdk fonts plugin demo doc source".each do |s|
          packages.push "sun-java5-#{s}_" # '_' means 'purge'
        end
      end
      %w"bin jre jdk fonts plugin demo".each do |s|
        packages.push "sun-java6-#{s}"
      end
      xsystem(APT, "install", *packages)
    end
  end
end

if $0 == __FILE__
  PackageInstaller.run
end

