#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_18'

  on_main_window_as('mmmmmm', 'mmmmmm') do
    assert_content('挿 入', [['Disabled,傷病名', 'Disabled,テキスト', 'Disabled,シェ−マ', 'Disabled,スタンプ']])
  end
end

