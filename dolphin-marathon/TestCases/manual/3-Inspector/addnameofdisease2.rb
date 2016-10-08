#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'common/wait_table'
require 'manual/on_inspector_for'

#傷病名の転帰、疾患終了日を追記
def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    on_inspector_for('00038', name) do |_, _, name|
      select('tabbedPane', '傷病名')
      click('Table1', '{0, 転 帰}')
      click('Table1', '{0, 転 帰}')
      click('Table1', '{0, 疾患終了日}')
      click('JTableHeader4', '疾患終了日')
      click('Table1', '{0, 疾患終了日}')
      rightclick('Table1', 2, '{0, 疾患終了日}')
    end

    with_window('傷病名編集ダイアログ') {
      window_closed('傷病名編集ダイアログ')
    }

    on_inspector_for('00038', name) do |_, _, name|
      doubleclick('Table', '{0, 疾患終了日}')
      doubleclick('Table', '{0, 疾患終了日}')
      select('Table', '2010-05-30', '{0, 疾患終了日}')
      click('save_16')
    end
  end
end

