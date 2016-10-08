#カルテの新規作成　検証 
 with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{0, 生年月日}')
    rightclick('RowTipsTable', '{0, 生年月日}')
    select_menu('カルテを開く')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      click('new_24')

      with_window('新規カルテ-OpenDolphin') {
        select('List', '[01270016  協会けんぽ]')
        select('List', '[01270016  協会けんぽ]')
        assert_p('前回処方を適用', 'Text', 'false')
        select('List', '[01270016  協会けんぽ]')
        click('OK')
      }

      with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
        assert_p('TextPane1', 'Text', '')
        window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')
      }

      window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
    }

    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }
