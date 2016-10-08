#! /bin/bash

set -ex

sudo aptitude update
sudo aptitude install libglade2-ruby libgettext-ruby1.8
PACKAGE="open-dolphin-backup-usb-storage_0.3.12_all.deb"
sudo dpkg -i "$PACKAGE"

DESKTOP=$(xdg-user-dir DESKTOP 2>/dev/null || true)
if [ -z "$DESKTOP" ]; then
  DESKTOP="$HOME/Desktop"
fi
DESKTOP_FILE="$DESKTOP/open-dolphin-backup-usb-storage.desktop"
PROGRAM="/usr/bin/open-dolphin-backup-usb-storage"
if [ ! -f "$DESKTOP_FILE" -a -x "$PROGRAM" ]; then
  cat >"$DESKTOP_FILE" <<EOF
#!/usr/bin/env xdg-open
[Desktop Entry]
Version=1.0
Encoding=UTF-8
Name=OpenDolphin USB Backup
Name[ja_JP]=OpenDolphin USB記憶装置へのバックアップ
Exec=$PROGRAM
Icon=/usr/share/open-dolphin-backup-usb-storage/gnome-dev-removable-usb.png
StartupNotify=true
Type=Application
Terminal=false
EOF
  chmod +x "$DESKTOP_FILE"
fi
