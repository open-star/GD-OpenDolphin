#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#スタンプを自費入力スタンプを作成する
#カルテを作成し、スタンプを貼付け保存できることを確認する
def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{1, 氏   名}')
    rightclick('RowTipsTable', '{1, 氏   名}')
    select_menu('カルテを開く')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      click('new_24')

      with_window('新規カルテ-OpenDolphin') {
        select('List', '[01270016  協会けんぽ]')
        select('List', '[01270016  協会けんぽ]')
        click('OK')
      }
    }
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('StampTree3', '{/汎 用/エディタから発行..., エディタから発行...}')
    click('StampTree3', '{/汎 用/エディタから発行..., エディタから発行...}')
    select('StampTree3', '[{/汎 用/エディタから発行..., エディタから発行...}]')
  }

  with_window('汎 用エディタ-OpenDolphin') {
    rightclick('TextField', 'Meta')
    rightclick('TextField', 'Meta')
    assert_p('TextField', 'Text', '')
    select('TextField', '095')
    select('部分一致', 'true')
    doubleclick('table', '{12, 名  称}')
    click('setTable', '{0, 診療内容}')
    assert_p('setTable', 'Text', '予防接種', '{0, 診療内容}')
    click('lgicn_16')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree3', '[{/汎 用/エディタから発行..., エディタから発行...}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    assert_p('StampHolder', 'Text', "<HTML><BODY><TT><FONT SIZE=\"12\" COLOR=\"\#ffffff\">\n<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"3\">\n<TR BGCOLOR=\"\#FFCED9\">\n<TD COLSPAN=\"3\">汎 用(予防接種) X 1</TD>\n</TR>\n<TR>\n<TD>・予防接種</TD>\n<TD> </TD>\n<TD> </TD>\n</TR>\n<TR>\n<TD COLSPAN=\"3\"></TD>\n</TR>\n</TABLE>\n</BODY></HTML>")
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
    select('StampTree3', '[{/汎 用/エディタから発行..., エディタから発行...}]')
  }

  with_window('確認') {
    click('はい')
  }

end
