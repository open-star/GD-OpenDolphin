#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_20'
  
  original_replace_stamp        = get_setting('ReplaceStamp')
  original_stamp_space          = get_setting('StampSpace')
  original_default_labo_fold    = get_setting('LaboFold')
  original_default_zyozai_num   = get_setting('DefaultZyozaiNum')
  original_default_mizuyaku_num = get_setting('DefaultMizuyakuNum')
  original_default_sanyaku_num  = get_setting('DefaultSanyakuNum')
  original_default_rp_num       = get_setting('DefaultRpNum')

  on_setting_window_for('カルテ') do
    
    select('tabbedPane', 'スタンプ')
    
    assert_p('stampAction', 'Border.Title', 'スタンプ動作の設定')
    assert_p('stampDefault', 'Border.Title', 'スタンプエディタのデフォルト数量')
    
    select('置き換える', 'true')
    select('DnD時にスタンプの間隔を空ける', 'false')
    select('検体検査の項目を折りたたみ表示する', 'false')

    select('defaultZyozaiNum', '3')
    select('defaultMizuyakuNum', '1')
    select('defaultSanyakuNum', '1.0')
    select('defaultRpNum', '3')
  
    click('閉じる')
  end
  
  # Settigns was not changed When click close
  assert_settings('ReplaceStamp', original_replace_stamp)
  assert_settings('StampSpace', original_stamp_space)
  assert_settings('LaboFold', original_default_labo_fold)
  assert_settings('DefaultZyozaiNum', original_default_zyozai_num)
  assert_settings('DefaultMizuyakuNum', original_default_mizuyaku_num)
  assert_settings('DefaultSanyakuNum', original_default_sanyaku_num)
  assert_settings('DefaultRpNum', original_default_rp_num)
  
  
  expected_replace_stamp        = true
  expected_stamp_space          = true
  expected_default_labo_fold    = true
  expected_default_zyozai_num   = '5'
  expected_default_mizuyaku_num = '2'
  expected_default_sanyaku_num  = '4.0'
  expected_default_rp_num       = '2'
  
  on_setting_window_for('カルテ') do
    
    select('tabbedPane', 'スタンプ')
    
    select('置き換える', expected_replace_stamp.to_s)
    select('DnD時にスタンプの間隔を空ける', expected_stamp_space.to_s)
    select('検体検査の項目を折りたたみ表示する', expected_default_labo_fold.to_s)

    select('defaultZyozaiNum', expected_default_zyozai_num)
    select('defaultMizuyakuNum', expected_default_mizuyaku_num)
    select('defaultSanyakuNum', expected_default_sanyaku_num)
    select('defaultRpNum', expected_default_rp_num)
    
    click('保 存')
  end
  
  assert_settings('ReplaceStamp', expected_replace_stamp)
  assert_settings('StampSpace', expected_stamp_space)
  assert_settings('LaboFold', expected_default_labo_fold)
  assert_settings('DefaultZyozaiNum', expected_default_zyozai_num)
  assert_settings('DefaultMizuyakuNum', expected_default_mizuyaku_num)
  assert_settings('DefaultSanyakuNum', expected_default_sanyaku_num)
  assert_settings('DefaultRpNum', expected_default_rp_num)
  
  # 後始末
  on_setting_window_for('カルテ') do
    
    select('tabbedPane', 'スタンプ')
    
    select('置き換える', 'false')
    select('DnD時にスタンプの間隔を空ける', 'false')
    select('検体検査の項目を折りたたみ表示する', 'false')

    select('defaultZyozaiNum', '3')
    select('defaultMizuyakuNum', '1')
    select('defaultSanyakuNum', '1.0')
    select('defaultRpNum', '3')
  
    click('保 存')
  end
end
