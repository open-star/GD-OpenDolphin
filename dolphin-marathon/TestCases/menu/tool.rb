#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_18'

  on_main_window_as('mmmmmm', 'mmmmmm') do
    assert_content('ツール', [['Enabled,スタンプボックス', 'Enabled,シェ−マボックス', 'Enabled,プロフィール変更...', 'Disabled,院内ユーザ登録...']])
  end
end

