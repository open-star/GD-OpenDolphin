#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_18'

  on_main_window_as('mmmmmm', 'mmmmmm') do
    assert_content('編 集', [['Disabled,修正', 'Disabled,元に戻す', 'Disabled,再実行', 'Disabled,カット', 'Disabled,コピー', 'Disabled,ペースト']])
  end
end

