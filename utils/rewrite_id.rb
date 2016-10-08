#! /usr/bin/env ruby
# -*- coding: utf-8 -*-

FILES        = Dir['*.xml']
RESTOER_FILE = 'labotest.restore'
DEFAULT_ID   = '00001'

class IDRewriter

  def initialize(files, restore_file)
    @files = files
    @restore_file = restore_file
  end

  def rewrite(id)

    restore_list = []

    @files.each do |file|
      info = [file]
      data = File.read(file).map {|line|
        if id_line_pattern =~ line
          info << $1
          line.gsub!(/#{$1}/, id)
        end
        line
      }
      File.open(file, 'w') {|io| io.write data }
      restore_list << info.join(',')
    end

    File.open(@restore_file, 'w') {|io| io.write restore_list.join("\n") }
  end

  def restore
    restore_list = File.read(@restore_file).split("\n").map {|i| i.split(',') }
    restore_list.each do |restore|
      file_name = restore.first
      target_id = restore.last

      data = File.read(file_name).map {|line|
        line.gsub!(/#{$1}/, target_id) if id_line_pattern =~ line
        line
      }
      File.open(file_name, 'w+') {|io| io.write data }
    end
  end

  def clean
    require 'fileutils'
    FileUtils.rm(@restore_file)
  end

  private
  def id_line_pattern
    %r!<mmlCm:Id mmlCm:type="local" [^>]*>(\d+)</mmlCm:Id>!
  end
end

if $0 == __FILE__
  command = ARGV.shift
  files   = ARGV.dup

  rewriter = IDRewriter.new(files.empty? ? FILES : files, RESTOER_FILE)

  case command
  when /^r/
    rewriter.restore
  when /^c/
    rewriter.clean
  else
    id = /\A\d*\z/ =~ command ? command : DEFAULT_ID
    rewriter.rewrite(id)
  end
end

