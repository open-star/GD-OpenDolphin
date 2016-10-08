#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_18'

  on_main_window_as('mmmmmm', 'mmmmmm') do

    assert_p('ヘルプ', 'Content[0]', '[Enabled,ドルフィン, Enabled,オルカ, Enabled,CLAIM 規格, Enabled,情報...]')

    # 情報
    select_menu('ヘルプ>>情報...')
    with_window('情報-OpenDolphin') do
      assert_p('OptionPane.label1', 'Text', "OpenDolphin  Ver.#{DOLPHIN_VERSION}")
      assert_p('OptionPane.label2', 'Text', "Copyright (C) #{COPYRIGHT_DOLPHIN}")
      assert_p('OptionPane.label3', 'Text', "Copyright (C) #{COPYRIGHT_GOODDAY}")
      click('閉じる')
    end

    # 実際に開くのかは目で確かめる
    select_menu('ヘルプ>>ドルフィン')
    select_menu('ヘルプ>>オルカ')
    select_menu('ヘルプ>>CLAIM 規格')
  end
end
