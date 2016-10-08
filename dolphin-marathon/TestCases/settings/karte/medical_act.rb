#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_20'
  
  original_use_top15_as_title  = get_setting('UseTop15AsTitle')
  original_default_karte_title = get_setting('DefaultKarteTitle')
  original_send_diagnosis      = get_setting('SendDiagnosis')

  on_setting_window_for('カルテ') do
    select('tabbedPane', '診療行為')
    
    assert_p('karteTitle', 'Border.Title', 'カルテの保存時に設定するタイトル')
    assert_p('sendDefault', 'Border.Title', '診療行為送信のデフォルト設定')
    
    select('カルテの先頭15文字を使用する', 'true')
    select('カルテの先頭15文字を使用する', 'false')
    
    select('送信しない', 'true')
    select('送信する', 'true')
    
    select('defaultKarteTitle', 'ほげほげ')
    
    click('閉じる')
  end
  
  assert_settings('UseTop15AsTitle', original_use_top15_as_title)
  assert_settings('DefaultKarteTitle', original_default_karte_title)
  assert_settings('SendDiagnosis', original_send_diagnosis)
  
  expected_use_top15_as_title  = false
  expected_default_karte_title = 'ほげほげ'
  expected_send_diagnosis      = false
  
  on_setting_window_for('カルテ') do
    select('tabbedPane', '診療行為')
    
    select('カルテの先頭15文字を使用する', 'false')
    select('defaultKarteTitle', 'ほげほげ')
    
    select('送信しない', 'true')
    
    click('保 存')
  end
 
  assert_settings('UseTop15AsTitle', expected_use_top15_as_title)
  assert_settings('DefaultKarteTitle', expected_default_karte_title)
  assert_settings('SendDiagnosis', expected_send_diagnosis)
  
  # リセット
  on_setting_window_for('カルテ') do
    select('tabbedPane', '診療行為')
    
    select('カルテの先頭15文字を使用する', 'false')
    select('defaultKarteTitle', '経過記録')
    select('カルテの先頭15文字を使用する', 'true')
    
    select('送信する', 'true')
    
    click('保 存')
  end

end
