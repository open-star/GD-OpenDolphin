#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  
  java_recorded_version = '1.6.0_20'

  on_main_window_as('admin', 'admin') do
    
    select_menu('ツール>>院内ユーザ登録...')

    with_window('ユーザ管理-OpenDolphin') do
      select('TabbedPane', 'ユーザ登録')
      select('TabbedPane', 'ユーザリスト')
      select('TabbedPane', 'ユーザ登録')
      select('TabbedPane', 'ユーザリスト')
      click('ユーザリスト')
      click('閉じる2')
    end
  end

end
