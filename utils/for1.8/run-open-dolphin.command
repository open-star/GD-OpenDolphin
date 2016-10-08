osascript -l AppleScript -e 'tell application "Terminal"
  set miniaturized of front window to true
end tell'

"$(dirname "$0")"/run-open-dolphin.sh open -a OpenDolphinClient-MAC

# http://apple.sysbio.info/~mjhsieh/archives/000328.html
echo "Please close this terminal window!"
osascript -l AppleScript -e 'tell Application "Terminal" to close every window whose contents contains "Please close this terminal window!"' &
