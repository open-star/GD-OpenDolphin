#スタンプ箱に新しいフォルダを作成し
#カルテに貼付けたスタンプをスタンプ箱のフォルダににドラッグし、保存する
#スタンプ箱のフォルダをドラッグし、カルテに貼付けられることを確認する

with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    assert_p('UserStampBox', 'Text', 'パ ス')
    select('StampTree2', '[]')
    rightclick('StampTree2', '{/パ ス/新規フォルダ, 新規フォルダ}')
    select('StampTree2', '[]')
    rightclick('StampTree2', '{/パ ス/新規フォルダ, 新規フォルダ}')
    select('StampTree2', '[]')
    rightclick('StampTree2', '{/パ ス/新規フォルダ, 新規フォルダ}')
    select_menu('新規フォルダ')
    select('StampTree2', '[]')
    rightclick('StampTree2', '{/パ ス/新規フォルダ, 新規フォルダ}')
    click('StampTree2', '{/パ ス/新規フォルダ, 新規フォルダ}')
    select('StampTree2', '[{/パ ス/新規フォルダ, 新規フォルダ}]')
    rightclick('StampTree2', 2, '{/パ ス/新規フォルダ, 新規フォルダ}')
    select_menu('新規フォルダ2')
    select('StampTree2', '[{/パ ス/新規フォルダ, 新規フォルダ}]')

    with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
      click('RowTipsTable', '{1, 生年月日}')
      rightclick('RowTipsTable', 2, '{1, 生年月日}')
      select_menu('カルテを開く')

      with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
        click('table', '{9, 内容}')
        click('table', '{8, 内容}')
        click('StampHolder2')
        click('StampHolder1')
      }
    }

    click('StampTree2', '{/パ ス/新規フォルダ/かぜ薬セット, かぜ薬セット}')
    click('StampTree2', '{/パ ス/新規フォルダ/新規フォルダ/かぜ薬セット, かぜ薬セット}')
    doubleclick('StampTree2', '{/パ ス/新規フォルダ/新規フォルダ, 新規フォルダ}')
    click('StampTree2', '{/パ ス/新規フォルダ/新規フォルダ, 新規フォルダ}')
    doubleclick('StampTree2', '{/パ ス/新規フォルダ/新規フォルダ, 新規フォルダ}')
    click('StampTree2', '{/パ ス/新規フォルダ/新規フォルダ/かぜ薬セット, かぜ薬セット}')
    click('StampTree2', '{/パ ス/新規フォルダ/かぜ薬セット, かぜ薬セット}')
    doubleclick('StampTree2', '{/パ ス/新規フォルダ/新規フォルダ, 新規フォルダ}')
    select('StampTree2', '[{/パ ス/新規フォルダ/かぜ薬セット, かぜ薬セット}]')
    drag_and_drop('StampTree2', '{/パ ス/新規フォルダ/新規フォルダ/かぜ薬セット, かぜ薬セット}', 'StampTree2', '{/パ ス/新規フォルダ/新規フォルダ, 新規フォルダ}', 'move')
    select('StampTree2', '[{/パ ス/新規フォルダ/新規フォルダ/かぜ薬セット, かぜ薬セット}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    click('KartePanel2', 81, 29)
    click('table', '{0, 内容}')
    click('new_24')

    with_window('新規カルテ-OpenDolphin') {
      select('List', '[01270016  協会けんぽ]')
      select('List', '[01270016  協会けんぽ]')
      click('OK')
    }
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('StampTree2', '{/パ ス/新規フォルダ/新規フォルダ, 新規フォルダ}')
    select('StampTree2', '[{/パ ス/新規フォルダ/新規フォルダ, 新規フォルダ}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')

    with_window('未保存処理-OpenDolphin') {
      click('キャンセル')
    }

    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')

    with_window('未保存処理-OpenDolphin') {
      click('キャンセル')
    }

    click('StampHolder')
    assert_p('StampHolder', 'Text', "<HTML><BODY><TT><FONT SIZE=\"12\" COLOR=\"\#ffffff\">\n<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"3\">\n  <TR BGCOLOR=\"\#FFCED9\">\n     <TD COLSPAN=\"3\">RP (かぜ薬セット)</TD>\n  </TR>\n  <TR>\n     <TD>・ポンタールシロップ3．25％</TD>\n     <TD> x 3.0</TD>\n     <TD>mＬ</TD>\n  </TR>\n  <TR>\n     <TD COLSPAN=\"3\">痛む時に x 3</TD>\n  </TR>\n  <TR>\n     <TD COLSPAN=\"3\">院外処方</TD>\n  </TR>\n</TABLE>\n</BODY></HTML>")
    click('StampHolder1')
    click('StampHolder1')
    assert_p('StampHolder1', 'Text', "<HTML><BODY><TT><FONT SIZE=\"12\" COLOR=\"\#ffffff\">\n<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"3\">\n  <TR BGCOLOR=\"\#FFCED9\">\n     <TD COLSPAN=\"3\">RP (かぜ薬セット)</TD>\n  </TR>\n  <TR>\n     <TD>・パセトシン細粒10％ 100mg</TD>\n     <TD> x 450.0</TD>\n     <TD>g</TD>\n  </TR>\n  <TR>\n     <TD>・フスコデシロップ</TD>\n     <TD> x 6.0</TD>\n     <TD>mＬ</TD>\n  </TR>\n  <TR>\n     <TD COLSPAN=\"3\">1日3回毎食後に x 3</TD>\n  </TR>\n  <TR>\n     <TD COLSPAN=\"3\">院外処方</TD>\n  </TR>\n</TABLE>\n</BODY></HTML>")
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')

    with_window('未保存処理-OpenDolphin') {
      click('保存')
    }

    with_window('ドキュメント保存-OpenDolphin') {
      click('保存して送信')
    }
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree2', '[{/パ ス/新規フォルダ/新規フォルダ, 新規フォルダ}]')
  }

  with_window('確認') {
    click('はい')
  }
