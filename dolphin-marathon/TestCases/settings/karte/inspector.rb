#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'


# TODO: PDFの保存先の選択ダイアログを開くと保存ボタンが押せなくなる
#       外した状態なので原因が分かれば修正する

def test
  java_recorded_version = '1.6.0_20'

  original_top_inspector       = get_setting('TopInspector')
  original_second_inspector    = get_setting('SecondInspector')
  original_third_inspector     = get_setting('ThirdInspector')
  original_forth_inspector     = get_setting('ForthInspector')
  original_localte_by_platform = get_setting('LocateByPlatform')
  original_pdf_store           = get_setting('PDFStore')
    
  on_setting_window_for('カルテ') do
    
    select('tabbedPane', 'インスペクタ')
    
    # インスペクター
    assert_p('insP', 'Border.Title', 'インスペクタ画面')
    assert_p('label1', 'Text', '左側トップ:')
    assert_p('label2', 'Text', '2番目:')
    assert_p('label3', 'Text', '3番目:')
    assert_p('label4', 'Text', 'ボトム:')
    assert_p('label6', 'Text', '画面ロケータ:')
    
    # PDF出力先
    assert_p('pdfP', 'Border.Title', '紹介状等PDFの出力先')
    assert_p('label7', 'Text', '出力先:')
    click('設定')
    with_window('開く') do
      # select('FileChooser, '#H/work/good-day/opendolphin/opendolphin/trunk/client/test')
      click('取消し')
    end    
    
    # Enable or Disable
    [
      %w[メモ メモ メモ メモ],
      %w[アレルギ アレルギ アレルギ アレルギ],
      %w[身長体重 身長体重 身長体重 身長体重],
      %w[文書履歴 文書履歴 文書履歴 文書履歴],

      %w[メモ メモ 身長体重 文書履歴],
      %w[メモ アレルギ メモ 文書履歴],
      %w[メモ アレルギ 身長体重 メモ],

      %w[アレルギ アレルギ 身長体重 文書履歴],
      %w[メモ アレルギ アレルギ 文書履歴],
      %w[メモ アレルギ 身長体重 アレルギ],

      %w[身長体重 アレルギ 身長体重 文書履歴],
      %w[メモ 身長体重 身長体重 文書履歴],
      %w[メモ アレルギ 身長体重 身長体重],

      %w[文書履歴 アレルギ 身長体重 文書履歴],
      %w[メモ 文書履歴 身長体重 文書履歴],
      %w[メモ アレルギ 文書履歴 文書履歴],
    ].each do |a, b, c, d|
      select('topCompo', a)
      select('secondCompo', b)
      select('thirdCompo',  c)
      select('forthCompo', d)
      assert_p('infoLabel', 'Text', '重複があります。')
    end
    select('topCompo', 'メモ')
    select('secondCompo',  'アレルギ')
    select('thirdCompo',  '身長体重')
    select('forthCompo', '文書履歴')
    assert_p('infoLabel', 'Text', '有効な組み合わせになっています。')
    
    click('閉じる')
  end
  
  expected_pdf_store = '#H/work/good-day/opendolphin/opendolphin/trunk/client'
  
  on_setting_window_for('カルテ') do
    
    select('tabbedPane', 'インスペクタ')
    
    select('topCompo', '文書履歴')
    select('secondCompo',  'メモ')
    select('thirdCompo',  'アレルギ')
    select('forthCompo', '身長体重')
    select('プラットフォーム', 'true')
    
    # click('設定')
    # with_window('開く') do
    #   select('FileChooser', expected_pdf_store)
    # end
    
    click('保 存')
    # click('閉じる')
  end
  
  assert_settings('TopInspector', '文書履歴')
  assert_settings('SecondInspector', 'メモ')
  assert_settings('ThirdInspector', 'アレルギ')
  assert_settings('ForthInspector', '身長体重')
  assert_settings('LocateByPlatform', true)
  # assert_settings('PDFStore', expected_pdf_store)
  
  # reset
  on_setting_window_for('カルテ') do
    
    select('tabbedPane', 'インスペクタ')
    
    select('topCompo', 'メモ')
    select('secondCompo',  'アレルギ')
    select('thirdCompo',  '身長体重')
    select('forthCompo', '文書履歴')
    select('位置と大きさを記憶する', 'true')
    
    # click('設定')
    # with_window('開く') do
    #   select('FileChooser', original_pdf_store)
    # end
    
    click('保 存')
  end
  
  assert_settings('TopInspector', 'メモ')
  assert_settings('SecondInspector', 'アレルギ')
  assert_settings('ThirdInspector', '身長体重')
  assert_settings('ForthInspector', '文書履歴')
  assert_settings('LocateByPlatform', false)
  assert_settings('PDFStore', original_pdf_store)
end
