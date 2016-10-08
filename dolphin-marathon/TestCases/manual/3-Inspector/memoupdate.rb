#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'manual/on_inspector_for'

#文書履歴更新
def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    on_inspector_for('00038', name) do |_, _, name|

      select('CompositeArea', "幼少期に小児喘息\n平均体温35.6℃\n糖尿病の傾向あり")
      click('ref_16')
    end
  end
end

