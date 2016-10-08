#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#スタンプのエクスポート処理の確認
#デスクトップに指定したファイル名でスタンプファイルが出力されているか確認
#出力したスタンプを再度インポートして処理が正常に終了することを確認
def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
    select('TextField', 'DolphinMara')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', '処 方')
    select('UserStampBox', '注 射')
    select('UserStampBox', '生体検査')
    select('UserStampBox', '検体検査')
    select('UserStampBox', '画像診断')
    select('UserStampBox', '手 術')
    select('UserStampBox', '処 置')
    select('UserStampBox', 'その他')
    select('UserStampBox', '汎 用')
    select('UserStampBox', 'ORCA')
    click('save_24')

    with_window('') {
      select('FileOpenDialog', '#H/Desktop/DolphinMaraのStamp')
    }
  }

  with_window('確認') {
    click('OK')
  }

  with_window('スタンプ箱-OpenDolphin') {
    window_closed('スタンプ箱-OpenDolphin')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }

end
