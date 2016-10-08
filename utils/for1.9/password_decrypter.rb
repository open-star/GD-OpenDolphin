#! /usr/bin/env ruby
# -*- coding: utf-8 -*-

require 'openssl'
require 'base64'
require 'digest/sha2'

class PasswordDecrypter
  def initialize(key_seed, salt)
    @key  = Digest::SHA256.hexdigest(key_seed)
    @salt = salt
    @cipher = nil
  end
  def run(encrypted)
    cipher.update(decode64(encrypted)) + cipher.final
  end
  def run_with(filename)
    run File.read(File.expand_path(filename))
  end
  private
  def cipher
    return @cipher if @cipher
    @cipher = OpenSSL::Cipher::DES.new
    @cipher.pkcs5_keyivgen(@key, @salt)
    @cipher.decrypt
  end

  def decode64(word)
    Base64.decode64(word)
  end
end

if $0 == __FILE__
  exit -1 if ARGV.empty?
  key_seed = 'OpenDolphin'
  salt     = 'CONYANCO'
  descrypter = PasswordDecrypter.new(key_seed, salt)
  ARGV.each do |filename|
    encrypted_chunk = descrypter.run_with(filename)
    if encrypted_chunk.nil?
      warn "Failed to decrypt #{filename}."
      next
    end
    hostname, encrypted_password = encrypted_chunk.split(':')
    password_decrypter = PasswordDecrypter.new(key_seed + hostname, salt)
    password = password_decrypter.run(encrypted_password)
    puts "[#{filename}] #{hostname}:#{password}"
  end
end
