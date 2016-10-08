#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#公開されたスタンプCommonStampをインポートするテスト

def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('TextField', 'DolphinMara')
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('impt_24')
  }

  with_window('スタンプインポート-OpenDolphin') {
    assert_p('ObjectListTable', 'Text', 'StampCommon', '{4, 名  称}')
    assert_p('ObjectListTable', 'Text', '院内シェア', '{4, カテゴリ}')
    assert_p('ObjectListTable', 'Text', 'グッデイ第三ビル内クリニック', '{4, 公開者}')
    assert_p('ObjectListTable', 'Text', '院内にて共用してください', '{4, 説  明}')
    click('ObjectListTable', '{7, 公開者}')
    click('ObjectListTable', '{4, 公開者}')
    click('インポート')
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('impt_24')
  }

  with_window('スタンプインポート-OpenDolphin') {
    click('閉じる')
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
    select('UserStampBox', '検体検査')
    select('UserStampBox', '生体検査')
    select('UserStampBox', '注 射')
    select('UserStampBox', '処 方')
    select('UserStampBox', '初診・再診')
    select('UserStampBox', '在宅')
    select('UserStampBox', '指導')

    with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
      window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
    }
  }

  with_window('確認') {
    click('はい')
  }

end
