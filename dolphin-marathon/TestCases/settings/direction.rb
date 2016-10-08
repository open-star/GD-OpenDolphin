#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_20'
  
  on_setting_window_for('指示箋') do

    assert_p('jPanel1', 'Border.Title', '指示箋の印刷先')
    assert_p('jLabel8', 'Text', 'プリント先１')
    assert_p('jLabel9', 'Text', 'プリント先2')
    assert_p('jLabel11', 'Text', '処方')
    assert_p('jLabel2', 'Text', '注射')
    assert_p('jLabel31', 'Text', '処置')
    assert_p('jLabel4', 'Text', '手術')
    assert_p('jLabel5', 'Text', '検査')
    assert_p('jLabel6', 'Text', '画像診断')
    assert_p('jLabel7', 'Text', 'その他')
    
    select('medOrderComboBox', 'なし')
    select('cCmedOrderComboBox', 'なし')
    select('injectionOrderComboBox', 'なし')
    select('cCinjectionOrderComboBox', 'なし')
    select('treatmentOrderComboBox', 'なし')
    select('cCtreatmentOrderComboBox', 'なし')
    select('surgeryOrderComboBox', 'なし')
    select('cCsurgeryOrderComboBox', 'なし')
    select('testOrderComboBox', 'なし')
    select('cCtestOrderComboBox', 'なし')
    select('radiologyOrderComboBox', 'なし')
    select('cCradiologyOrderComboBox', 'なし')
    select('otherOrderComboBox', 'なし')
    select('cCotherOrderComboBox', 'なし')
    
    click('保 存')
  end
  
  14.times {|i| assert_settings('Direction', 'なし', i) }
end
