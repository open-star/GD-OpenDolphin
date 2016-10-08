#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'manual/on_inspector_for'

#カルテの新規作成
def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    on_inspector_for('00038', name) do |patient_id, patient_name, user_name|

      click('new_24')

      with_window('新規カルテ-OpenDolphin') {
        select('List', '[01270016  協会けんぽ]')
        select('List', '[01270016  協会けんぽ]')
        assert_p('前回処方を適用', 'Text', 'false')
        select('List', '[01270016  協会けんぽ]')
        click($fixture.variables.ok_label)
      }

      # FIXME: 出来れば患者名に依存したくない
      with_window("ドルフィン マラソン(ドルフィン マラソン) : #{patient_id}- カルテ | #{user_name} | 医師") {
        assert_p('TextPane1', 'Text', '')
      }
    end
  end
end
