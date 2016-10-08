#右クリックでカルテにスタンプを入力する
with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{0, 氏   名}')
    rightclick('RowTipsTable', '{0, 氏   名}')
    select_menu('カルテを開く')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      click('new_24')

      with_window('新規カルテ-OpenDolphin') {
        select('List', '[01270016  協会けんぽ]')
        select('List', '[01270016  協会けんぽ]')
        click('OK')
      }

      with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
        rightclick('TextPane1')
        rightclick('TextPane1')
        rightclick('TextPane1')
        select_menu('汎 用2>>おたふくかぜ')
        window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')

        with_window('未保存処理-OpenDolphin') {
          click('保存')
        }

        with_window('ドキュメント保存-OpenDolphin') {
          click('保存して送信')
        }
      }

      window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
    }

    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }
