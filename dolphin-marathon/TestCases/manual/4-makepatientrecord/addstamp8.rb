#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#処方のフリーコメントで作成したスタンプがカルテに正しく貼付けでき、保存できることを確認する
def test
  java_recorded_version = '1.6.0_20'

  with_window('スタンプ箱-OpenDolphin') {
    click('StampTree3', '{/汎 用/日本脳炎, 日本脳炎}')
    select('StampTree3', '[{/汎 用/日本脳炎, 日本脳炎}]')
    select('StampTree3', '[{/汎 用/日本脳炎, 日本脳炎}]')
    assert_p('UserStampBox', 'Text', '汎 用')
    select('StampTree3', '[{/汎 用/日本脳炎, 日本脳炎}]')
    select('tools_24', 'true')
    click('table2', '{0, コード}')
    doubleclick('table2', '{0, コード}')
    select('TextField', '810000001')
    click('table2', '{0, コード}')
    doubleclick('table2', '{0, 名  称}')
  }

  with_window('入力') {
    select('OptionPane.textField', '経過観察のため実施しました')
    click('OK')
    click('OK')
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('setTable1', '{0, 診療内容}')
    assert_p('setTable1', 'Text', '経過観察のため実施しました', '{0, 診療内容}')
    click('forwd_16')
    assert_p('StampTree3', 'Text', '経過観察のため実施しました', '{/汎 用/経過観察のため実施しました, 経過観察のため実施しました}')
    select('StampTree3', '[{/汎 用/経過観察のため実施しました, 経過観察のため実施しました}]')
    window_closed('スタンプ箱-OpenDolphin')
    select('tools_24', 'false')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{1, 氏   名}')
    rightclick('RowTipsTable', '{1, 氏   名}')
    select_menu('カルテを開く')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    click('new_24')

    with_window('新規カルテ-OpenDolphin') {
      select('List', '[01270016  協会けんぽ]')
      select('List', '[01270016  協会けんぽ]')
      click('OK')
    }
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree3', '[{/汎 用/経過観察のため実施しました, 経過観察のため実施しました}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')
    select('TextPane1', "\n \nスタンプ箱で作成したフリーコメントスタンプをカルテに貼付けて、保存できることを確認する")

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

    with_window('スタンプ箱-OpenDolphin') {
      select('StampTree3', '[{/汎 用/経過観察のため実施しました, 経過観察のため実施しました}]')
    }
  }

  with_window('確認') {
    click('はい')
  }
#コメント定型文
  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    assert_p('UserStampBox', 'Text', '汎 用')
    select('tools_24', 'true')
    select('TextField', '830000001')
    click('table2', '{0, 名  称}')
    select('TextField', '820000001')
    select('部分一致', 'true')
    click('table2', '{0, 名  称}')
    select('TextField', '82000')
    click('table2', '{2, 名  称}')
    doubleclick('table2', '{2, 名  称}')
  }

  with_window('入力') {
    select('OptionPane.textField', '自費から3割支払い')
    assert_p('OptionPane.textField', 'Text', '自費から3割支払い')
    click('キャンセル')
  }

  with_window('スタンプ箱-OpenDolphin') {
    doubleclick('table2', '{2, 名  称}')
  }

  with_window('入力') {
    assert_p('OptionPane.textField', 'Text', '自費から')
    select('OptionPane.textField', '自費から３割支払い')
    assert_p('OptionPane.textField', 'Text', '自費から３割支払い')
    click('OK')
  }

  with_window('スタンプ箱-OpenDolphin') {
    assert_p('setTable1', 'Text', '自費から３割支払い', '{0, 診療内容}')
    select('TextField', '830000')
    click('table2', '{3, 名  称}')
    doubleclick('table2', '{4, 名  称}')
  }
#コメント定型文字以降フリー
 
  with_window('入力') {
    click('OptionPane.label1', 'Meta')
    assert_p('OptionPane.label1', 'Text', '骨髄提供者名：')
    assert_p('OptionPane.textField', 'Text', '')
    select('OptionPane.textField', 'オープンドルフィン検査機関')
    assert_p('OptionPane.textField', 'Text', 'オープンドルフィン検査機関')
    click('OK')
  }

  with_window('スタンプ箱-OpenDolphin') {
    assert_p('setTable1', 'Text', '骨髄提供者名：オープンドルフィン検査機関', '{1, 診療内容}')
    select('TextField', '840000')
    doubleclick('table2', '{13, 名  称}')
  }

  with_window('入力') {
    select('OptionPane.textField', '12-')
    click('キャンセル')
  }

  with_window('スタンプ箱-OpenDolphin') {
    doubleclick('table2', '{13, 正式名称}')
  }

#数値入力コメント

  with_window('スタンプ箱-OpenDolphin') {
    assert_p('setTable1', 'Text', '外泊　　　日～　　日', '{2, 診療内容}')
    assert_p('setTable1', 'Text', '12-13', '{2, 数 量}')
    select('stampNameField1', 'コメント')
    click('forwd_16')
    assert_p('StampTree3', 'Text', 'コメント', '{/汎 用/コメント, コメント}')
    select('StampTree3', '[{/汎 用/コメント, コメント}]')
    window_closed('スタンプ箱-OpenDolphin')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{1, 氏   名}')
    rightclick('RowTipsTable', 2, '{1, 氏   名}')
    select_menu('カルテを開く')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    click('new_24')

    with_window('新規カルテ-OpenDolphin') {
      select('List', '[01270016  協会けんぽ]')
      select('List', '[01270016  協会けんぽ]')
      click('OK')
    }
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree3', '[{/汎 用/コメント, コメント}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    rightclick('StampHolder', 'Meta')
    rightclick('StampHolder', 'Meta')
    click('StampHolder')
    rightclick('StampHolder', 2)
  }

  with_window('汎 用エディタ-OpenDolphin') {
    window_closed('汎 用エディタ-OpenDolphin')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    click('StampHolder')
    assert_p('StampHolder', 'Text', "<HTML><BODY><TT><FONT SIZE=\"12\" COLOR=\"\#ffffff\">\n<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"3\">\n<TR BGCOLOR=\"\#FFCED9\">\n<TD COLSPAN=\"3\">汎 用(コメント) X 1</TD>\n</TR>\n<TR>\n<TD>・自費から3割支払い</TD>\n<TD> </TD>\n<TD> </TD>\n</TR>\n<TR>\n<TD>・骨髄提供者名：オープンドルフィン検査機関</TD>\n<TD> </TD>\n<TD> </TD>\n</TR>\n<TR>\n<TD>・外泊   日～  日</TD>\n<TD> X 12-13</TD>\n<TD></TD>\n</TR>\n<TR>\n<TD COLSPAN=\"3\"></TD>\n</TR>\n</TABLE>\n</BODY></HTML>")
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

    with_window('スタンプ箱-OpenDolphin') {
      select('StampTree3', '[{/汎 用/コメント, コメント}]')
    }
  }

  with_window('確認') {
    click('はい')
  }




end
