#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'manual/on_inspector_for'

def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    on_inspector_for('00038', name) do |patient_id, patient_name, user_name|

      select('tabbedPane', '患者情報')
      assert_p('Table1', 'Text', '00038', '{0, B}')
      assert_p('Table1', 'Text', 'ドルフィン マラソン', '{1, B}')
      assert_p('Table1', 'Text', 'ドルフィン マラソン', '{2, B}')
      assert_p('Table1', 'Text', '', '{3, B}')
      assert_p('Table1', 'Text', 'female', '{4, B}')
      assert_p('Table1', 'Text', '36 歳 (1974-02-28)', '{5, B}')
      assert_p('Table1', 'Text', '', '{6, B}')
      assert_p('Table1', 'Text', '', '{7, B}')
      assert_p('Table1', 'Text', '573-0112', '{8, B}')
      assert_p('Table1', 'Text', '大阪府枚方市尊延寺 ３ー４ー４', '{9, B}')
      assert_p('Table1', 'Text', '06-9182-9181', '{10, B}')
      assert_p('Table1', 'Text', '', '{11, B}')
      assert_p('Table1', 'Text', '', '{12, B}')
      assert_p('Table2', 'Text', '協会けんぽ', '{0, B}')
      assert_p('Table2', 'Text', '09', '{1, B}')
      assert_p('Table2', 'Text', '01270016', '{2, B}')
      assert_p('Table2', 'Text', '５６２９８７６', '{3, B}')
      assert_p('Table2', 'Text', '川いろは', '{4, B}')
      assert_p('Table2', 'Text', '本人', '{5, B}')
      assert_p('Table2', 'Text', '2010-05-21', '{6, B}')
      assert_p('Table2', 'Text', '9999-12-31', '{7, B}')
      assert_p('Table2', 'Text', '', '{8, B}')
      assert_p('Table2', 'Text', '0.30', '{9, B}')
    end
  end
end

