#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#検体検査
#総蛋白（正式名称）、TP（名称）のどちらでも検索できます。
def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', 'その他')
    select('UserStampBox', '処 置')
    select('UserStampBox', '手 術')
    select('UserStampBox', '画像診断')
    select('UserStampBox', '検体検査')
    assert_p('UserStampBox', 'Text', '検体検査')
    select('tools_24', 'true')
    select('TextField', '淡白')
    select('部分一致', 'true')
    select('TextField', '総蛋白')
    doubleclick('table2', '{0, 名  称}')
    assert_p('table2', 'Text', 'ＴＰ', '{0, 名  称}')
    select('TextField', 'TP')
    click('table2', '{0, 名  称}')
    assert_p('table2', 'Text', 'ＴＰ', '{0, 名  称}')
    window_closed('スタンプ箱-OpenDolphin')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }

end
