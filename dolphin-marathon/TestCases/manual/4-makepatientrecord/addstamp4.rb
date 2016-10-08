#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#処方スタンプ箱で作成したスタンプをカルテに貼付け保存できることを確認する

def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', '画像診断')
    select('UserStampBox', '手 術')
    select('UserStampBox', '処 置')
    select('UserStampBox', 'その他')
    select('UserStampBox', '汎 用')
    select('UserStampBox', 'ORCA')
    select('UserStampBox', 'パ ス')
    select('UserStampBox', 'テキスト')
    select('UserStampBox', '傷病名')
    select('UserStampBox', 'テキスト')
    select('UserStampBox', 'パ ス')
    select('UserStampBox', 'ORCA')
    select('UserStampBox', '汎 用')
    select('UserStampBox', 'その他')
    select('UserStampBox', '処 置')
    select('UserStampBox', '手 術')
    select('UserStampBox', '画像診断')
    select('UserStampBox', '検体検査')
    select('UserStampBox', '生体検査')
    select('UserStampBox', '注 射')
    select('UserStampBox', '処 方')
    assert_p('UserStampBox', 'Text', '処 方')
    select('tools_24', 'true')
    select('TextField1', 'ロキソニン')
    select('部分一致1', 'true')
    doubleclick('table3', '{1, 名  称}')
    select('tabbedPane', '用法')
    select('ComboBox', '内服２回等(200)')
    doubleclick('table4', '{5, 名 称}')
    assert_p('medTable', 'Text', 'ロキソニン錠　６０ｍｇ', '{0, 診療内容}')
    assert_p('medTable', 'Text', '[用法] １日２回朝夕食後に', '{1, 診療内容}')
    select('stampNameField5', 'ロキソニン錠1*2')
    click('forwd_16')
    assert_p('StampTree11', 'Text', 'ロキソニン錠1*2', '{/処 方/ロキソニン錠1*2, ロキソニン錠1*2}')
    select('StampTree11', '[{/処 方/ロキソニン錠1*2, ロキソニン錠1*2}]')
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
    select('StampTree11', '[{/処 方/ロキソニン錠1*2, ロキソニン錠1*2}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    select('TextPane1', "\n \nスタンプメーカーで作成したスタンプをカルテに貼付ける")
    click('save_24')

    with_window('ドキュメント保存-OpenDolphin') {
      click('保存して送信')
    }
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    click('BlockGlass', 83, 51)
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')

    with_window('スタンプ箱-OpenDolphin') {
      select('StampTree11', '[{/処 方/ロキソニン錠1*2, ロキソニン錠1*2}]')
    }
  }

  with_window('確認') {
    click('はい')
  }

end
