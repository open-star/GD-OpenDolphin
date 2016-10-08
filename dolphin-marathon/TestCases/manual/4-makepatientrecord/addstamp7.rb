#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#処方スタンプ箱で禁忌組み合わせを選んだ際に、警告メッセージが表示されることを確認する
def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    assert_p('UserStampBox', 'Text', '処 方')
    select('tools_24', 'true')
    select('TextField1', 'バキソ')
    select('部分一致1', 'true')
    doubleclick('table3', '{0, 名  称}')
    assert_p('medTable', 'Text', 'バキソカプセル１０　１０ｍｇ', '{0, 診療内容}')
    select('TextField1', 'ニービア')
    select('TextField1', 'ニービア')
    drag_and_drop('TextField1', nil, 'TextField1', nil, 'move')
    select('TextField1', 'ノービア')
    click('table3', '{1, 名  称}')
    doubleclick('table3', '{1, 名  称}')
  }

  with_window('禁忌があります') {
    click('symptomTable', '{0, 薬剤名称}')
    click('symptomTable', '{1, 薬剤名称}')
    click('symptomTable', '{2, 薬剤名称}')
    click('symptomTable', '{0, 薬剤名称}')
    assert_p('symptomTable', 'Text', 'ノービア・ソフトカプセル１００ｍｇ', '{0, 薬剤名称}')
    assert_p('symptomTable', 'Text', 'ノービア・ソフトカプセル１００ｍｇ', '{1, 薬剤名称}')
    assert_p('symptomTable', 'Text', 'ノービア・ソフトカプセル１００ｍｇ', '{2, 薬剤名称}')
    assert_p('symptomsTextArea', 'Text', '血液障害、不整脈、血管攣縮、重篤な又は生命に危険を及ぼすような事象')
    assert_p('interactTextArea', 'Text', '本剤のチトクロームＰ４５０に対する競合的阻害作用により、併用した場合これらの薬剤の血中濃度が大幅に上昇')
    click('追加しない')
  }

  with_window('スタンプ箱-OpenDolphin') {
    window_closed('スタンプ箱-OpenDolphin')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }

end
