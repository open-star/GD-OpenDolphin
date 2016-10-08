#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'common/wait_table'
require 'manual/on_inspector_for'

#インスペクタ画面で　身長、体重を追加する
#インスペクタ画面で　身長、体重を1削除する

def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    on_inspector_for('00038', name) do |_, _, name|

      wait_table('table2', '{0, 診療日}')
      click('table2', '{1, 診療日}')
   
      select('searchField', '胸部')
      keystroke('searchField', 'Enter')

      rightclick('table1', 2, '{0, 体重}')
      select_menu('追加')

      with_window('身長体重登録-OpenDolphin') {
        select('weightFld', '40')
        select('heightFld', '170')
        click('変更...')
        click('Table', '{3, 火}')
        click('追加')
      }

      rightclick('table1', '{1, 測定日}')
      select_menu('追加')

      with_window('身長体重登録-OpenDolphin') {
        select('weightFld', '43')
        select('heightFld', '170')
        click('変更...')
        click('Table', '{4, 火}')
        click('追加')
      }

      click('table1', '{1, 測定日}')
      rightclick('table1', 2, '{1, 測定日}')
      select_menu('削除')
      rightclick('table1', '{2, 測定日}')
      select_menu('追加1')

      with_window('身長体重登録-OpenDolphin') {
        select('weightFld', '42')
        select('heightFld', '170')
        click('変更...')
        click('Table', '{3, 金}')
        click('追加')
      }

      click('table1', '{0, 測定日}')
      rightclick('table1', 2, '{0, 測定日}')
      select_menu('削除')
      rightclick('table1', '{2, BMI}')
      select_menu('追加1')

      with_window('身長体重登録-OpenDolphin') {
        select('weightFld', '43')
        select('heightFld', '170')
        click('Form', 374, 70)
        doubleclick('identifiedDateFld')
        click('identifiedDateFld')
        click('変更...')
        click('Table', '{4, 木}')
        click('追加')
      }

      # MEMO: データがずれるからこのテストは難しい
#       click('table1', '{0, BMI}')
#       assert_p('table1', 'Text', '40', '{0, 体重}')
#       assert_p('table1', 'Text', '43', '{1, 体重}')
#       assert_p('table1', 'Text', '43', '{2, 体重}')
#       click('table1', '{2, 体重}')
#       rightclick('table1', 2, '{2, 体重}')
#       click('table1', '{4, 身長}')
#       click('table1', '{2, 体重}')
#       rightclick('table1', 2, '{2, 体重}')
#       select_menu('削除1')
#       click('table1', '{3, 体重}')
#       click('table1', '{0, BMI}')
#       rightclick('table1', 2, '{0, BMI}')
#       assert_p('table1', 'Text', '40', '{0, 体重}')
#       assert_p('table1', 'Text', '43', '{1, 体重}')
#       assert_p('table1', 'Text', '43', '{1, 体重}')
    end
  end
end
