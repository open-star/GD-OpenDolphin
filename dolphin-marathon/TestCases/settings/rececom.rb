#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_20'
  
  expected_send_claim        = get_setting('SendClaim')
  expected_orca_version      = get_setting('OrcaVersion')
  original_jmari_code        = expected_jmari_code    = get_setting('JMARICode')
  original_claim_address     = expected_claim_address = get_setting('ClaimAddress')
  original_claim_port        = expected_claim_port    = get_setting('ClaimPort')
  expected_claim_host_name   = get_setting('ClaimHostName')
  expected_use_as_pvt_server = get_setting('UseAsPVTServer')
  expected_claim01           = get_setting('Claim01')

  on_setting_window_for('レセコン') do
    
    # CLAIM送信
    assert_p('sendClaim', 'Border.Title', 'CLAIM（請求データ）送信')
    assert_p('label1', 'Text', '診療行為送信:')
    select('送信する', 'true')

    # レセコン情報
    assert_p('port', 'Border.Title', 'レセコン情報')
    assert_p('label2', 'Text', '機種:')
    assert_p('label3', 'Text', 'バージョン:')
    assert_p('label4', 'Text', 'CLAIM診療科コード:')
    assert_p('label5', 'Text', '医療機関ID:  JPN"')
    assert_p('label6', 'Text', 'IPアドレス:')
    assert_p('label7', 'Text', 'ポート番号:')
    assert_p('claimPortField', 'Enabled', 'true')
    select('4.0以降', 'true')
    select('デフォルト01を使用', 'true')
    select('jmariField', '123456789098')
    select('claimAddressField', 'xxx.xxx.xxx.xxx')
    select('claimPortField', '0001')

    # 受付情報の受信
    assert_p('pvt', 'Border.Title', '受付情報の受信')
    assert_p('このマシンでORCAからの受付情報を受信する', 'Text', expected_claim01.to_s)
    select('このマシンでORCAからの受付情報を受信する', 'false')

    click('閉じる')
  end
  
  assert_settings('SendClaim', expected_send_claim)
  assert_settings('OrcaVersion', expected_orca_version)
  assert_settings('JMARICode', expected_jmari_code)
  assert_settings('ClaimAddress', expected_claim_address)
  assert_settings('ClaimPort', expected_claim_port)
  assert_settings('ClaimHostName', expected_claim_host_name)
  assert_settings('UseAsPVTServer', expected_use_as_pvt_server)
  assert_settings('Claim01', expected_claim01)
  
  # Change Test
  expected_send_claim        = true
  expected_orca_version      = "40"
  expected_jmari_code        = "JPN123456789098"
  expected_claim_address     = "xxx.xxx.xxx.xxx"
  expected_claim_port        = 1234
  expected_claim_host_name   = get_setting('ClaimHostName')
  expected_use_as_pvt_server = false
  expected_claim01           = false
  
  on_setting_window_for('レセコン') do
    
    # Port Enable or Disable
    select('送信する', 'true')
    assert_p('claimPortField', 'Enabled', 'true')
    select('送信しない', 'true')
    assert_p('claimPortField', 'Enabled', 'false')
    select('送信する', 'true')
    
    # JMRI Enable or Disable
    select('3.4', 'true')
    assert_p('jmariField', 'Enabled', 'false')
    select('4.0以降', 'true')
    assert_p('jmariField', 'Enabled', 'true')
    
    # レセコン情報
    select('4.0以降', 'true')
    select('デフォルト01を使用', 'false')
    select('jmariField', expected_jmari_code.sub(/JPN/, ''))
    select('claimAddressField', expected_claim_address)
    select('claimPortField', expected_claim_port.to_s)

    # 受付情報の受信
    select('このマシンでORCAからの受付情報を受信する', 'false')

    click('保 存')
  end
  
  assert_settings('SendClaim', expected_send_claim)
  assert_settings('OrcaVersion', expected_orca_version)
  assert_settings('JMARICode', expected_jmari_code)
  assert_settings('ClaimAddress', expected_claim_address)
  assert_settings('ClaimPort', expected_claim_port)
  assert_settings('ClaimHostName', expected_claim_host_name)
  assert_settings('UseAsPVTServer', expected_use_as_pvt_server)
  assert_settings('Claim01', expected_claim01)
  
  # Reset settings
  on_setting_window_for('レセコン') do
  
    select('4.0以降', 'true')
    select('デフォルト01を使用', 'true')
    select('jmariField', original_jmari_code.sub(/JPN/, ''))
    select('claimAddressField', original_claim_address)
    select('claimPortField', original_claim_port.to_s)
    select('このマシンでORCAからの受付情報を受信する', 'true')
    select('送信しない', 'true')

    click('保 存')
  end
  
end
