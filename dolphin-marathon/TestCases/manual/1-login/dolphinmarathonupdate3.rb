#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('TextField', 'admin')
  }

  with_window('メインウインドウ-OpenDolphin | admin | 医師') {
    select_menu('ツール>>院内ユーザ登録...')

    with_window('ユーザ管理-OpenDolphin') {
      select('TextField', 'グッデイ整形外科')
      select('TextField1', '111')
      select('TextField2', '1111')
      select('TextField3', '大阪府北区梅田1-2-2-1300-1')
      select('TextField6', '6666')
      select('TextField7', 'http://www.good-day.co.jp')
      click('更新')

      with_window('null-OpenDolphin') {
        click('OK')
      }

      click('戻す')
      click('戻す')
      rightclick('TextField', 'Alt')
      click('戻す')
      click('戻す')
      click('戻す', 10)
      click('戻す')
      select('TextField', 'グッデイ')
      click('閉じる')
    }

    window_closed('メインウインドウ-OpenDolphin | admin | 医師')
  }

  with_window('確認') {
    click('はい')
  }
  with_window('メインウインドウ-OpenDolphin | admin | 医師') {
    select_menu('ツール>>院内ユーザ登録...')

    with_window('ユーザ管理-OpenDolphin') {
      select('TextField', 'グッデイ脳神経外科')
      select('TextField1', '530')
      select('TextField2', '0001')
      select('TextField3', '大阪府北区梅田1-2-2-1300')
      select('TextField6', '6670')
      click('更新')

      with_window('null-OpenDolphin') {
        click('OK')
      }

      click('戻す')
      click('閉じる')
    }

    window_closed('メインウインドウ-OpenDolphin | admin | 医師')
  }

  with_window('確認') {
    click('はい')
  }

end
