#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_18'
  
  original_host_address = expected_host_address = get_setting('HostAddress')
  original_db_address   = expected_db_address   = get_setting('DbAddress')
  original_user_id      = expected_user_id      = get_setting('UserId')
  
  on_setting_window_for('サーバ') do
    
    # Enable or Disable
    select('hostAddressField', 'localhost')
    select('userIdField', 'nyanko')
    assert_p('保 存', 'Enabled', 'true')
    select('hostAddressField', 'localhost')
    select('userIdField', '')
    assert_p('保 存', 'Enabled', 'false')
    select('hostAddressField', '')
    select('userIdField', 'nyanko')
    assert_p('保 存', 'Enabled', 'false')
    select('hostAddressField', '')
    select('userIdField', '')
    assert_p('保 存', 'Enabled', 'false')
    
    # サーバ情報
    assert_p('sip', 'Border.Title', 'サーバ情報')
    assert_p('label', 'Text', 'IPアドレス:')
    assert_p('jLabel1', 'Text', 'DBアドレス:')
    
    select('hostAddressField', 'hhh.hhh.hhh.hhh')
    select('dbAddressField', 'xxx.xxx.xxx.xxx')
    
    # ユーザ情報
    assert_p('uip', 'Border.Title', 'ユーザ情報')
    assert_p('jLabel3', 'Text', 'ユーザID:')

    select('userIdField', 'nyanko')
    
    click('閉じる')
  end
  
  assert_settings('HostAddress', expected_host_address)
  assert_settings('DbAddress', expected_db_address)
  assert_settings('UserId', expected_user_id)

  expected_host_address = 'remotehost'
  expected_db_address   = 'aaa.bbb.ccc.ddd'
  expected_user_id      = 'tatibana'
  
  on_setting_window_for('サーバ') do
    select('hostAddressField', expected_host_address)
    select('dbAddressField', expected_db_address)
    select('userIdField', expected_user_id)

    click('保 存')
  end
  
  assert_settings('HostAddress', expected_host_address)
  assert_settings('DbAddress', expected_db_address)
  assert_settings('UserId', expected_user_id)
  
  on_login_window do
    assert_p('TextField', 'Text', 'tatibana')
  end
  
  # Reset settings
  on_setting_window_for('サーバ') do
    select('hostAddressField', original_host_address)
    select('dbAddressField', original_db_address)
    select('userIdField', original_user_id)

    click('保 存')
  end
end
