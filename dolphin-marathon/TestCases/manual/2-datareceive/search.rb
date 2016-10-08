#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

# MEMO: Ubuntuだと動いていない
#test description 受付画面のアレルギー項目の値を検索する
def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do

    select('TabbedPane', '患者検索')

    keystroke('TextField', 'Enter')

    now = Time.now
    until get_p('AddressTipsTable', 'RowCount').to_i > 0 || (Time.now - now) > 30
      sleep 0.01
    end

    select('TextField', '金属')
    click('アレルギー')

    now = Time.now
    until get_p('AddressTipsTable', 'RowCount').to_i == 1 || (Time.now - now) > 30
      sleep 0.01
    end

    assert_p('AddressTipsTable', 'Text', '00038', '{0, ID}')
  end
end

