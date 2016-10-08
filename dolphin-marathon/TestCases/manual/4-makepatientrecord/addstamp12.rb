#スタンボックスに「ロキソニン削除用」スタンプを作成し
#スタンプ箱で右クリック矢印削除するテスト
 with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', '処 方')
    select('tools_24', 'true')
    select('TextField1', 'ロキソ')
    select('部分一致1', 'true')
    doubleclick('table3', '{22, 名  称}')
    select('stampNameField5', 'ロキソニンテープ削除用')
    click('forwd_16')
    drag_and_drop('StampTree11', '{/処 方/ロキソニンテープ削除用, ロキソニンテープ削除用}', 'StampTree11', '{/処 方/ロキソニンテープ削除用, ロキソニンテープ削除用}', 'move')
    assert_p('StampTree11', 'Text', 'ロキソニンテープ削除用', '{/処 方/ロキソニンテープ削除用, ロキソニンテープ削除用}')
    select('StampTree11', '[{/処 方/ロキソニンテープ削除用, ロキソニンテープ削除用}]')
    select('StampTree11', '[{/処 方/ロキソニンテープ削除用, ロキソニンテープ削除用}]')
    rightclick('StampTree11', '{/処 方/ロキソニンテープ削除用, ロキソニンテープ削除用}')
    select_menu('削 除')
    window_closed('スタンプ箱-OpenDolphin')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }
