#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('admin', 'admin') do

    select_menu('ツール>>院内ユーザ登録...')

    with_window('ユーザ管理-OpenDolphin') do
      select('TextField', 'グッデイ整形外科')
      select('TextField1', '111')
      select('TextField2', '1111')
      select('TextField3', '大阪府北区梅田1-2-2-1300-1')
      select('TextField6', '6666')
      select('TextField7', 'http://www.good-day.co.jp')
      
      click('更新')

      # Gnome: '了解', Mac: 'Ok'
      with_window('null-OpenDolphin') { click($fixture.variables.ok_label) }

      click('戻す')
      click('戻す')
      click('戻す')
      click('戻す')
      click('戻す', 10)
      click('戻す')
      select('TextField', 'グッデイ')

      click('更新')
      
      with_window('null-OpenDolphin') { click($fixture.variables.ok_label) }
    end
  end
end
