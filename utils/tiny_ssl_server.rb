#! /usr/bin/ruby1.8
require 'socket'
require 'openssl'

puts 'Booting tiny server ...'

port = 2010
unless ARGV.empty?
  port = ARGV[0].to_i
end

cert_file = '/etc/ssl/certs/ssl-cert-snakeoil.pem'
pkey_file = '/etc/ssl/private/ssl-cert-snakeoil.key'

ctx = OpenSSL::SSL::SSLContext.new()
ctx.cert = OpenSSL::X509::Certificate.new(File.read(cert_file))
ctx.key  = OpenSSL::PKey::RSA.new(File.read(pkey_file))

serv = OpenSSL::SSL::SSLServer.new(TCPServer.new(port), ctx)

puts 'Ready.'
puts "Port: #{port}"
loop do
  while soc = serv.accept
    soc.close
  end
end

puts 'Server shuted down.'

