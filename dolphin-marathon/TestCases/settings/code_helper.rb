#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_preference'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_20'
  
  SettingManager.preferences_target = Java::OpenDolphinCLient.Dolphin.java_class
  
  original_keywords = []
  keys = %w[
    text
    path
    generalOrder
    otherOrder
    treatmentOrder
    surgeryOrder
    radiologyOrder
    testOrder
    physiologyOrder
    injectionOrder
    medOrder
    baseChargeOrder
    instractionChargeOrder
    stayOnHomeChargeOrder
    orcaSet
  ]
  keys.each_with_index do |key, idx|
    original_keywords[idx] = get_preference(key)
  end

  on_setting_window_for('コード') do
  
    original_keywords.each_with_index do |val, idx|
      idx = '' if idx == 0
      select("TextField#{idx}", val.to_s)
    end
  
    # Enable or Disable
    original_keywords.each_with_index do |val, idx|
      idx = '' if idx == 0
      select("TextField#{idx}", '')
      assert_p('保 存', 'Enabled', 'false')
      select("TextField#{idx}", val)
      assert_p('保 存', 'Enabled', 'true')
    end

    # 修飾キー
    assert_p('Panel4', 'Border.Title', '修飾キー + スペース = 補完ポップアップ')
    assert_p('Label', 'Text', '修飾キー:')
    
    # スタンプのキーワード
    assert_p('Panel7', 'Border.Title', 'スタンプ箱のキーワード')
    assert_p('Label1', 'Text', 'テキスト:')
    assert_p('Label3', 'Text', '汎 用:')
    assert_p('Label5', 'Text', '処 置:')
    assert_p('Label7', 'Text', '画像診断:')
    assert_p('Label9', 'Text', '生体検査:')
    assert_p('Label10', 'Text', '注 射:')
    assert_p('Label12', 'Text', '診断料:')
    assert_p('Label14', 'Text', '在宅:')
    assert_p('Label2', 'Text', 'パス:')
    assert_p('Label4', 'Text', 'その他:')
    assert_p('Label6', 'Text', '手 術:')
    assert_p('Label8', 'Text', '検体検査:')
    assert_p('Label11', 'Text', '処 方:')
    assert_p('Label13', 'Text', '指導:')
    assert_p('Label15', 'Text', 'ORCA:')
    
    #select('メタ', 'true')
    #select('コントロール', 'true')
    
    click('閉じる')
  end
  
  # 保存のテスト
  # キーワードがすべて同じでも保存できるのはよくない
  on_setting_window_for('コード') do
    select('コントロール', 'true')
    original_keywords.length.times do |idx|
      idx = '' if idx == 0
      select("TextField#{idx}", 'hoge')
    end
    click('保 存')
  end
  
  keys.each do |key|
    assert_preference(key, 'hoge')
  end

  on_setting_window_for('コード') do
    original_keywords.each_with_index do |val, idx|
      idx = '' if idx == 0
      select("TextField#{idx}", val.to_s)
    end
    click('保 存')
  end
  
  keys.each_with_index do |key, idx|
    assert_preference(key, original_keywords[idx])
  end
end
