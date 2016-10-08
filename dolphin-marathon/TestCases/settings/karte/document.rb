#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'assertions/assert_settings'
require 'settings/on_setting_window'

def test
  java_recorded_version = '1.6.0_20'
  
  original_show_modified_karte              = get_setting('ShowModifiedKarte')
  original_show_unsend_karte                = get_setting('ShowUnsendKarte')
  original_show_send_karte                  = get_setting('ShowSendKarte')
  original_show_fetch_karte_count           = get_setting('FetchKarteCount')
  original_show_karte_v                     = get_setting('ScrollKarteV')
  original_show_karte_extraction_period     = get_setting('KarteExtractionPeriod')
  original_show_ascending_diagnosis         = get_setting('AscendingDiagnosis')
  original_show_diagnosis_extraction_period = get_setting('DiagnosisExtractionPeriod')
  original_show_auto_outcome_input          = get_setting('AutoOutcomeInput')
  original_show_offset_outcome_date         = get_setting('OffsetOutcomeDate')

  on_setting_window_for('カルテ') do

    select('tabbedPane', '文 書')
    
    assert_p('kartePanel', 'Border.Title', 'カルテ')
    assert_p('label8', 'Text', '文書履歴:')
    assert_p('label9', 'Text', '自動文書取得数:')
    assert_p('scrP', 'Enabled', 'true')
    assert_p('label11', 'Text', '文書抽出期間:')
    assert_p('diagnosisPanel', 'Border.Title', '傷病名')
    assert_p('label12', 'Text', '表示順:')
    assert_p('label14', 'Text', '転帰入力時:')
    assert_p('label13', 'Text', '抽出期間:')
    assert_p('デフォルト設定に戻す', 'Text', 'デフォルト設定に戻す')
    
    
    select('修正履歴表示', 'true')
    select('未送信履歴表示', 'true')
    select('送信履歴表示', 'true')
    select('spinner', '7')
    select('水平', 'true')
    select('垂直', 'true')
    select('水平', 'true')
    select('spinner', '10')
    select('periodCombo', '1ケ月')
    select('periodCombo', '3ケ月')
    select('periodCombo', '半年')
    select('periodCombo', '1年')
    select('periodCombo', '2年')
    select('periodCombo', '3年')
    select('periodCombo', '5年')
    select('昇順', 'true')
    select('降順', 'true')
    select('diagnosisPeriodCombo', '1年')
    select('diagnosisPeriodCombo', '全て')
    select('diagnosisPeriodCombo', '2年')
    select('diagnosisPeriodCombo', '3年')
    select('diagnosisPeriodCombo', '5年')
    select('diagnosisPeriodCombo', '継続のみ')
    select('終了日を自動入力する', 'false')
    select('終了日を自動入力する', 'true')
    select('outcomeSpinner', '0')
    select('outcomeSpinner', '-1')
    select('outcomeSpinner', '-31')
    click('デフォルト設定に戻す')
    
    # Enable or Disable
    select('終了日を自動入力する', 'false')
    assert_p('outcomeSpinner', 'Enabled', 'false')
    select('終了日を自動入力する', 'true')
    assert_p('outcomeSpinner', 'Enabled', 'true')
    
    click('閉じる')
  end
  
  assert_settings('ShowModifiedKarte', original_show_modified_karte)
  assert_settings('ShowUnsendKarte', original_show_unsend_karte)
  assert_settings('ShowSendKarte', original_show_send_karte)
  assert_settings('FetchKarteCount', original_show_fetch_karte_count)
  assert_settings('ScrollKarteV', original_show_karte_v)
  assert_settings('KarteExtractionPeriod', original_show_karte_extraction_period)
  assert_settings('AscendingDiagnosis', original_show_ascending_diagnosis)
  assert_settings('DiagnosisExtractionPeriod', original_show_diagnosis_extraction_period)
  assert_settings('AutoOutcomeInput', original_show_auto_outcome_input)
  assert_settings('OffsetOutcomeDate', original_show_offset_outcome_date)
  
  on_setting_window_for('カルテ') do
    select('tabbedPane', '文 書')
    
    select('修正履歴表示', 'true')
    select('未送信履歴表示', 'true')
    select('送信履歴表示', 'true')
    select('spinner', '10')
    select('水平', 'true')
    select('periodCombo', '2年')

    select('昇順', 'true')
    select('diagnosisPeriodCombo', '全て')
    select('終了日を自動入力する', 'true')
    select('outcomeSpinner', '-31')

    click('保 存')
  end
  
  assert_settings('ShowModifiedKarte', true)
  assert_settings('ShowUnsendKarte', true)
  assert_settings('ShowSendKarte', true)
  assert_settings('FetchKarteCount', 10)
  assert_settings('ScrollKarteV', false)
  assert_settings('KarteExtractionPeriod', -24)
  assert_settings('AscendingDiagnosis', true)
  assert_settings('DiagnosisExtractionPeriod', 0)
  assert_settings('AutoOutcomeInput', true)
  assert_settings('OffsetOutcomeDate', -31)
  
  
  [
    ['1ケ月', -1],
    ['3ケ月', -3],
    ['半年', -6],
    ['1年', -12],
    ['2年', -24],
    ['3年', -36],
    ['5年', -60],
  ].each do |key, val|
    on_setting_window_for('カルテ') do  
      select('tabbedPane', '文 書')
      select('periodCombo', key)
      click('保 存')
    end
    assert_settings('KarteExtractionPeriod', val)
  end

  [
    ['1年', -12],
    ['2年', -24],
    ['3年', -36],
    ['5年', -60],
    ['継続のみ', 1],
  ].each do |key, val|
    on_setting_window_for('カルテ') do  
      select('tabbedPane', '文 書')
      select('diagnosisPeriodCombo', key)
      click('保 存')
    end
    assert_settings('DiagnosisExtractionPeriod', val)
  end

end
