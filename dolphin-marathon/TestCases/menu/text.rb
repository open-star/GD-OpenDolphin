#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_18'

  on_main_window_as('mmmmmm', 'mmmmmm') do
    assert_content('テキスト', [['Disabled,サイズ', 'Disabled,大きく', 'Disabled,小さく', 'Disabled,標準サイズ', 'Disabled,スタイル', 'Disabled,ボールド', 'Disabled,イタリック', 'Disabled,アンダーライン', 'Disabled,行揃え', 'Disabled,左揃え', 'Disabled,中央揃え', 'Disabled,右揃え', 'Disabled,カラー', 'Disabled,レッド', 'Disabled,オレンジ', 'Disabled,イェロー', 'Disabled,グリーン', 'Disabled,ブルー', 'Disabled,パープル', 'Disabled,グレイ', 'Disabled,黒']])
  end
end

