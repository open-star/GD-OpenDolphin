#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_20'
  
  original_confirm_at_new    = get_setting('ConfirmAtNew')
  original_create_karte_mode = get_setting('CreateKarteMode')
  original_place_karte_mode  = get_setting('PlaceKarteMode')
  original_confirm_at_save   = get_setting('ConfirmAtSave')
  original_print_karte_count = get_setting('PrintKarteCount')
  original_save_karte_mode   = get_setting('SaveKarteMode')
  original_is_hospital       = get_setting('IsHospital')

  on_setting_window_for('カルテ') do
    select('tabbedPane', '確認ダイアログ')
    
    assert_p('newKarte', 'Border.Title', '新規カルテ作成時')
    assert_p('saveKarte', 'Border.Title', 'カルテ保存時')
    
    select('確認ダイアログを表示しない', 'true')
    select('確認ダイアログを表示しない', 'false')
    select('前回処方を適用', 'true')
    select('前回処方を適用', 'true')
    select('全てコピー', 'true')
    select('空白の新規カルテ', 'true')
    select('タブパネルへ追加', 'true')
    select('別ウィンドウで編集', 'true')
    select('入院', 'true')
    select('入院', 'false')
    select('確認ダイアログを表示しない1', 'true')
    select('確認ダイアログを表示しない1', 'false')
    select('確認ダイアログを表示しない1', 'true')
    select('printCount', '2')
    select('仮保存', 'true')
    select('保 存1', 'true')
    
    # Enable or Disable
    select('確認ダイアログを表示しない1', 'false')
    assert_p('printCount', 'Enabled', 'false')
    assert_p('保 存1', 'Enabled', 'false')
    assert_p('仮保存', 'Enabled', 'false')
    select('確認ダイアログを表示しない1', 'true')
    assert_p('printCount', 'Enabled', 'true')
    assert_p('保 存1', 'Enabled', 'true')
    assert_p('仮保存', 'Enabled', 'true')
    
    click('閉じる')
  end
  
  assert_settings('ConfirmAtNew', original_confirm_at_new)
  assert_settings('CreateKarteMode', original_create_karte_mode)
  assert_settings('PlaceKarteMode', original_place_karte_mode)
  assert_settings('ConfirmAtSave', original_confirm_at_save)
  assert_settings('PrintKarteCount', original_print_karte_count)
  assert_settings('SaveKarteMode', original_save_karte_mode)
  assert_settings('IsHospital', original_is_hospital)
  
  
  expected_confirm_at_new    = false
  expected_create_karte_mode = 1
  expected_place_karte_mode  = true
  expected_confirm_at_save   = false
  expected_print_karte_count = 5
  expected_save_karte_mode   = 1
  expected_is_hospital       = true
  
  on_setting_window_for('カルテ') do
    select('tabbedPane', '確認ダイアログ')
    
    select('確認ダイアログを表示しない', 'true')
    
    select('前回処方を適用', 'true')

    select('別ウィンドウで編集', 'true')
    
    select('入院', 'true')
    
    select('確認ダイアログを表示しない1', 'true')
    select('確認ダイアログを表示しない1', 'true')
    select('printCount', expected_print_karte_count.to_s)
    select('仮保存', 'true')
    
    click('保 存')
  end
  
  p get_setting('ConfirmAtSave')
 
  assert_settings('ConfirmAtNew', expected_confirm_at_new)
  assert_settings('CreateKarteMode', expected_create_karte_mode)
  assert_settings('PlaceKarteMode', expected_place_karte_mode)
  assert_settings('ConfirmAtSave', expected_confirm_at_save)
  assert_settings('PrintKarteCount', expected_print_karte_count)
  assert_settings('SaveKarteMode', expected_save_karte_mode)
  assert_settings('IsHospital', expected_is_hospital)
  
  on_setting_window_for('カルテ') do
    select('tabbedPane', '確認ダイアログ')
    
    select('確認ダイアログを表示しない', 'false')
    
    select('空白の新規カルテ', 'true')

    select('別ウィンドウで編集', 'true')
    
    select('入院', 'false')

    select('printCount', '2')
    select('保 存1', 'true')
    select('確認ダイアログを表示しない1', 'false')
    
    click('保 存')
  end

end
