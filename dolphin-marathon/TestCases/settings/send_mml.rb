#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_20'

  original_send_mml                 = expected_send_mml                 = get_setting('SendMML')
  original_mml_version              = expected_mml_version              = get_setting('MMLVersion')
  original_uploader_ip_address      = expected_uploader_ip_address      = get_setting('UploaderIPAddress')
  original_uploader_share_directory = expected_uploader_share_directory = get_setting('UploadShareDirectory')
  
  on_setting_window_for('MML出力') do
  
    # Enable or Disable
    select('しない', 'true')
    assert_p('2.3', 'Enabled', 'false')
    assert_p('3.0', 'Enabled', 'false')
    assert_p('protocolCombo', 'Enabled', 'false')
    assert_p('uploaderServer', 'Enabled', 'false')
    assert_p('shareDirectory', 'Enabled', 'false')
    select('する', 'true')
    assert_p('2.3', 'Enabled', 'true')
    assert_p('3.0', 'Enabled', 'true')
    assert_p('protocolCombo', 'Enabled', 'true')
    assert_p('uploaderServer', 'Enabled', 'true')
    assert_p('shareDirectory', 'Enabled', 'true')
    
    # MML(XML)出力
    assert_p('content', 'Border.Title', 'MML(XML)出力')
    assert_p('する', 'Component.Text', 'する')
    assert_p('しない', 'Component.Text', 'しない')
    assert_p('label1', 'Text', 'MML バージョン:')
    assert_p('label2', 'Text', '送信プロトコル:')
    assert_p('label3', 'Text', '送信サーバアドレス:')
    assert_p('label4', 'Text', '送信先ディレクトリ:')
    
    select('する', 'true')
    
    select('2.3', 'true')
    select('uploaderServer', '192.168.0.181')
    select('shareDirectory', 'directory')
 
    assert_p('保 存', 'Enabled', 'true')
    
    click('閉じる')
  end
  
  assert_settings('SendMML', expected_send_mml)
  assert_settings('MMLVersion', expected_mml_version)
  assert_settings('UploaderIPAddress', expected_uploader_ip_address)
  assert_settings('UploadShareDirectory', expected_uploader_share_directory)
  
  expected_send_mml                 = true
  expected_mml_version              = '300'
  expected_uploader_ip_address      = '192.168.0.181'
  expected_uploader_share_directory = 'directory'

  on_setting_window_for('MML出力') do
    select('する', 'true')
    select((expected_mml_version == '300') ? '3.0' : '2.3', 'true')
    select('uploaderServer', expected_uploader_ip_address)
    select('shareDirectory', expected_uploader_share_directory)
    click('保 存')
  end
  
  assert_settings('SendMML', expected_send_mml)
  assert_settings('MMLVersion', expected_mml_version)
  assert_settings('UploaderIPAddress', expected_uploader_ip_address)
  assert_settings('UploadShareDirectory', expected_uploader_share_directory)

  on_setting_window_for('MML出力') do
    select('する', 'true')
    select((original_mml_version == '300') ? '3.0' : '2.3', 'true')
    select('uploaderServer', original_uploader_ip_address)
    select('shareDirectory', original_uploader_share_directory)
    if original_send_mml
      select('する', 'true')
    else
      select('しない', 'true')
    end
    click('保 存')
  end
  
  assert_settings('SendMML', original_send_mml)
  assert_settings('MMLVersion', original_mml_version)
  assert_settings('UploaderIPAddress', original_uploader_ip_address)
  assert_settings('UploadShareDirectory', original_uploader_share_directory)

end
