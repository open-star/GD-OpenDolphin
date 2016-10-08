#スタンプ箱のORCAタブにデータ表示されていることを確認する

with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', '手 術')
    select('UserStampBox', '処 置')
    select('UserStampBox', 'その他')
    select('UserStampBox', '汎 用')
    select('UserStampBox', 'ORCA')
    assert_p('UserStampBox', 'Text', 'ORCA')
    click('OrcaTree', '{/ORCA/紹介状, 紹介状}')
    select('OrcaTree', '[{/ORCA/紹介状, 紹介状}]')
    assert_p('OrcaTree', 'Text', '紹介状', '{/ORCA/紹介状, 紹介状}')

    with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
      window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
    }

    select('OrcaTree', '[{/ORCA/紹介状, 紹介状}]')
  }

  with_window('確認') {
    click('はい')
  }
