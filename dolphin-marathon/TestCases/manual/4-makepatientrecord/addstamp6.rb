#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    assert_p('UserStampBox', 'Text', '処 方')
    select('tools_24', 'true')
    select('TextField1', 'ロキソ')
    select('部分一致1', 'true')
    click('table3', '{1, 名  称}')
    doubleclick('table3', '{2, 名  称}')
    click('medTable', '{0, 診療内容}')
    assert_p('medTable', 'Text', 'ロキソニン錠　６０ｍｇ', '{0, 診療内容}')
    select('院内', 'true')
    select('緊急時', 'true')
    assert_p('院内', 'Text', 'true')
    assert_p('院外', 'Text', 'false')
    assert_p('院外', 'Text', 'false')
    assert_p('緊急時', 'Text', 'true')
    select('tabbedPane', '用法')
    select('ComboBox', '内服１回等(100)')
    click('table4', '{0, 名 称}')
    doubleclick('table4', '{0, 名 称}')
    click('medTable', '{1, 診療内容}')
    drag_and_drop('medTable', '{1, 診療内容}', 'medTable', '{1, 診療内容}', 'move')
    assert_p('medTable', 'Text', '[用法] 医師の指示通りに', '{1, 診療内容}')
    doubleclick('medTable', '{1, 回数}')
    doubleclick('medTable', '{1, 回数}')
    click('medTable', '{1, 回数}')
    rightclick('medTable', '{1, 回数}')
    select('medTable', '１ ', '{1, 回数}')
    rightclick('medTable', '{1, 回数}')
    select('medTable', '１     ', '{1, 回数}')
    click('medTable', '{1,  }')
    rightclick('medTable', '{1, 回数}')
    assert_p('medTable', 'Text', '１', '{1, 回数}')
    select('stampNameField5', 'ロキソニン錠医師の指示で')
    click('medTable', '{0, 診療内容}')
    drag_and_drop('medTable', '{0, 診療内容}', 'medTable', '{0, 診療内容}', 'move')
    select('tabbedPane', '注射薬')
    select('tabbedPane', '特定器材')
    select('tabbedPane', '内用・外用薬')
    select('tabbedPane', '用法')
    select('院外', 'true')
    select('緊急時', 'false')
    select('緊急時', 'true')
    select('院内', 'true')
    click('medTable', '{1,  }')
    click('medTable', '{0,  }')
    click('medTable', '{0, 診療内容}')
    window_closed('スタンプ箱-OpenDolphin')
  }

#スタンプ箱に矢印ボタンで取り込み、カルテに貼付け、保存できることを確認する

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }

end
