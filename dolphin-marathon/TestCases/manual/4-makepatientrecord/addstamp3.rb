#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#画像診断タブで作成したスタンプをカルテに貼れることを確認する
def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{1, 性別}')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', 'テキスト')
    select('UserStampBox', 'パ ス')
    select('UserStampBox', 'ORCA')
    select('UserStampBox', '汎 用')
    select('UserStampBox', 'その他')
    select('UserStampBox', '処 置')
    select('UserStampBox', '手 術')
    select('UserStampBox', '画像診断')
    assert_p('UserStampBox', 'Text', '画像診断')
    select('tools_24', 'true')
    select('methodList', '[{15, 単純-胸腹部}]')
    select('commentList', '[{11, 2方向(P→A\,側)}]')
    select('stampNameField8', '胸部レントゲン２PA')
    select('部分一致', 'true')
    select('TextField', '透析診断')
    select('TextField', '骨折')
    doubleclick('table2', '{51, 名  称}')
    click('setTable7', 'Meta', '{0, 診療内容}')
    rightclick('setTable7', '{0, 診療内容}')
    assert_p('setTable7', 'Text', '関節内骨折観血的手術（肩鎖）', '{0, 診療内容}')
    click('forwd_16')
    assert_p('StampTree7', 'Text', '胸部レントゲン２PA', '胸部レントゲン２PA')
    select('StampTree7', '[胸部レントゲン２PA]')
    window_closed('スタンプ箱-OpenDolphin')
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
    select('StampTree7', '[胸部レントゲン２PA]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    select('TextPane1', "\n \nスタンプメーカーで作成した画像診断のスタンプがカルテに貼れることを確認")
    click('StampHolder')
    assert_p('StampHolder', 'Text', "<HTML><BODY><TT><FONT SIZE=\"12\" COLOR=\"\#ffffff\">\n<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"3\">\n<TR BGCOLOR=\"\#FFCED9\">\n<TD COLSPAN=\"3\">画像診断(胸部レントゲン2PA) X 1</TD>\n</TR>\n<TR>\n<TD>・関節内骨折観血的手術（肩鎖）</TD>\n<TD> </TD>\n<TD> </TD>\n</TR>\n<TR>\n<TD COLSPAN=\"3\">2方向(P→A,側)</TD>\n</TR>\n</TABLE>\n</BODY></HTML>")
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

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree7', '[胸部レントゲン２PA]')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    rightclick('RowTipsTable', '{1, 氏   名}')
    select_menu('カルテを開く')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    click('StampHolder2')
    click('StampHolder2')
    assert_p('StampHolder2', 'Text', "<HTML><BODY><TT><FONT SIZE=\"12\" COLOR=\"\#ffffff\">\n<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"3\">\n<TR BGCOLOR=\"\#FFCED9\">\n<TD COLSPAN=\"3\">画像診断(胸部レントゲン2PA) X 1</TD>\n</TR>\n<TR>\n<TD>・関節内骨折観血的手術（肩鎖）</TD>\n<TD> </TD>\n<TD> </TD>\n</TR>\n<TR>\n<TD COLSPAN=\"3\">2方向(P→A,側)</TD>\n</TR>\n</TABLE>\n</BODY></HTML>")
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }

end
