#カルテに画像を貼付ける
with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
    click('ログイン')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{1, 性別}')
    rightclick('RowTipsTable', 2, '{1, 性別}')
    select_menu('カルテを開く')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師(1)') {
    click('new_24')

    with_window('新規カルテ-OpenDolphin') {
      select('List', '[01270016  協会けんぽ]')
      select('List', '[01270016  協会けんぽ]')
      click('OK')
    }

    select('tabbedPane', 'イメージ')
  }

  with_window('シェーマエディタ-OpenDolphin') {
    click('カルテに展開')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    click('SchemaHolder')
    select('TextPane', " \n経過観察のためレントゲン\n所見　異常なし\n")
    click('save_24')

    with_window('ドキュメント保存-OpenDolphin') {
      click('保存して送信')
    }
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師(1)') {
    select('tabbedPane', '参 照')
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師(1)')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }
