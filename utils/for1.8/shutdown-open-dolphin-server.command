"$(dirname "$0")"/../jboss-4.2.2.GA/bin/shutdown.sh --shutdown

# http://apple.sysbio.info/~mjhsieh/archives/000328.html
echo "Please close this terminal window!"
osascript -l AppleScript -e 'tell Application "Terminal" to close every window whose contents contains "Please close this terminal window!"' &
