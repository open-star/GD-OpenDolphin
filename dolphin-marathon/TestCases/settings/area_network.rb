#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_20'
  
  original_join_area_network        = expected_join_area_network        = get_setting('JoinAreaNetwork')
  original_area_network_name        = expected_area_network_name        = get_setting('AreaNetworkName')
  original_area_network_facility_id = expected_area_network_facility_id = get_setting('AreaNetworkFacilityId')
  original_area_network_creator_id  = expected_area_network_creator_id  = get_setting('AreaNetworkCreatorId')

  on_setting_window_for('地域連携') do
    
    # Enable or Disable
    select('参加する', 'true')
    assert_p('areaNetworkCombo', 'Enabled', 'true')
    assert_p('facilityIdField', 'Enabled', 'true')
    assert_p('creatorIdField', 'Enabled', 'true')
    select('参加しない', 'true')
    assert_p('areaNetworkCombo', 'Enabled', 'false')
    assert_p('facilityIdField', 'Enabled', 'false')
    assert_p('creatorIdField', 'Enabled', 'false')
    
    # 地域連携
    assert_p('content', 'Border.Title', '地域連携')
    assert_p('参加する', 'Component.Text', '参加する')
    select('参加する', 'true')
    
    assert_p('label1', 'Text', 'プロジェクト:')
    assert_p('label2', 'Text', '連携用医療機関ID:')
    assert_p('label3', 'Text', '連携用ユーザID:')

    select('areaNetworkCombo', '選択してください')
    select('areaNetworkCombo', 'HANIWA(宮崎県)')
    select('areaNetworkCombo', 'HIGOMED(肥後医育)')
    select('areaNetworkCombo', 'HOT(東京都医師会)')
    select('areaNetworkCombo', 'MAIKO(京都地域連携医療)')
    
    select('facilityIdField', '11111111')
    select('creatorIdField', '22222222')

    click('閉じる')
  end
  
  assert_settings('JoinAreaNetwork', expected_join_area_network)
  assert_settings('AreaNetworkName', expected_area_network_name)
  assert_settings('AreaNetworkFacilityId', expected_area_network_facility_id)
  assert_settings('AreaNetworkCreatorId', expected_area_network_creator_id)
  
  expected_join_area_network = true
  expected_area_network_name = 'maiko'
  expected_area_network_facility_id = '11111111'
  expected_area_network_creator_id = '22222222'
  
  on_setting_window_for('地域連携') do

    select('参加する', 'true')
    select('areaNetworkCombo', '選択してください')
    assert_p('保 存', 'Enabled', 'false')
    select('areaNetworkCombo', 'MAIKO(京都地域連携医療)')
    select('facilityIdField', expected_area_network_facility_id)
    select('creatorIdField', expected_area_network_creator_id)
    assert_p('保 存', 'Enabled', 'true')

    click('保 存')
  end
  
  assert_settings('JoinAreaNetwork', expected_join_area_network)
  assert_settings('AreaNetworkName', expected_area_network_name)
  assert_settings('AreaNetworkFacilityId', expected_area_network_facility_id)
  assert_settings('AreaNetworkCreatorId', expected_area_network_creator_id)
  
  # Reset
  on_setting_window_for('地域連携') do
    
    select('参加する', 'true')
    select('areaNetworkCombo', '選択してください')
    select('facilityIdField', original_area_network_facility_id)
    select('creatorIdField', original_area_network_creator_id)
    
    select('参加しない', 'true')
    
    click('保 存')
  end

end
