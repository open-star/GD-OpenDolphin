#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#自院コードで用法を入力しスタンプ箱でスタンプを作成する
#カルテに貼付け、保存できることを確認する


def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', '注 射')
    select('UserStampBox', '生体検査')
    select('UserStampBox', '検体検査')
    select('UserStampBox', '画像診断')
    select('UserStampBox', '手 術')
    select('UserStampBox', '処 置')
    select('UserStampBox', 'その他')
    select('UserStampBox', '汎 用')
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
    select('UserStampBox', '初診・再診')
    select('UserStampBox', '在宅')
    select('UserStampBox', '処 方')
    select('tools_24', 'true')
    select('TextField1', 'ルキソニン')
    select('部分一致1', 'true')

    with_window('進行状況...') {
      click('キャンセル')
    }

    select('TextField1', 'ロキソニン')
    click('table3', '{3, 名  称}')
    doubleclick('table3', '{3, 名  称}')
    select('stampNameField5', 'ロキソニンテープ')
    click('table3', '{4, カ ナ}')
    doubleclick('table3', '{3, カ ナ}')
    select('tabbedPane', '用法')
    select('TextField3', '0003')
    click('table4', '{6, 名 称}')
    doubleclick('table4', '{18, 名 称}')
    click('medTable', '{0, 診療内容}')
    drag_and_drop('medTable', '{0, 診療内容}', 'medTable', '{0, 診療内容}', 'move')
    assert_p('medTable', 'Text', 'ロキソニンテープ５０ｍｇ　７ｃｍ×１０ｃｍ', '{0, 診療内容}')
    assert_p('medTable', 'Text', '[用法] １日３回毎８時間毎に', '{1, 診療内容}')
    assert_p('TextField3', 'Text', '0003')
    click('forwd_16')
    assert_p('StampTree11', 'Text', 'ロキソニンテープ', '{/処 方/ロキソニンテープ, ロキソニンテープ}')
    select('StampTree11', '[{/処 方/ロキソニンテープ, ロキソニンテープ}]')
    window_closed('スタンプ箱-OpenDolphin')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{1, 氏   名}')
    rightclick('RowTipsTable', '{1, 氏   名}')
    select_menu('カルテを開く')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    rightclick('table', '{0, 内容}')
    rightclick('table', '{0, 内容}')
    click('new_24')

    with_window('新規カルテ-OpenDolphin') {
      select('List', '[01270016  協会けんぽ]')
      select('List', '[01270016  協会けんぽ]')
      click('OK')
    }
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree11', '[{/処 方/ロキソニンテープ, ロキソニンテープ}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    select('TextPane1', "\n \nスタンプ箱で作成したスタンプをカルテに張り付け、保存できることを確認する\n")
    click('save_24')

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
      select('StampTree11', '[{/処 方/ロキソニンテープ, ロキソニンテープ}]')
    }
  }

  with_window('確認') {
    click('はい')
  }

end
