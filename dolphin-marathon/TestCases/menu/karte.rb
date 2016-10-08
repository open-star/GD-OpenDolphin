#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_18'

  on_main_window_as('mmmmmm', 'mmmmmm') do
    assert_content('カルテ', [['Disabled,昇順', 'Disabled,降順', 'Disabled,修正履歴表示', 'Disabled,未送信履歴表示', 'Disabled,送信履歴表示', 'Enabled,環境設定...']])
  end
end

