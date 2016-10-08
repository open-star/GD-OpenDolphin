#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture


# MEMO: デスクトップのものは動かない
#morikawa KEK00002.xmlをメインウィンドゥにDrag&Dropして取り込む
def test
  java_recorded_version = '1.6.0_20'

    select('TabbedPane', '受付リスト')
    select('TabbedPane', '患者検索')
    select('TextField', '山本 一真')
    assert_p('TextField', 'Text', '山本 一真')
    assert_p('TextField', 'Text', '山本 一真')
    rightclick('AddressTipsTable', '{0, 氏名}')
    click('AddressTipsTable', '{0, 氏名}')
    rightclick('AddressTipsTable', 2, '{0, 氏名}')
    select_menu('カルテを開く')
  }

  with_window('山本 一真(ヤマモト カズマ) : 00001- インスペクタ | DolphinMarathon | 医師(1)') {
    select('tabbedPane', 'ラボテスト')
    select('tabbedPane', '治療履歴')
    select('tabbedPane', '傷病名')
    select('tabbedPane', '参 照')
    select('tabbedPane', 'ラボテスト')
    click('table', '{2, 内容}')
    click('table', '{1, 内容}')
    select('tabbedPane', '参 照')
    select('tabbedPane', 'ラボテスト')
    click('Table1', '{0, 採取\: 20090824}')
    click('Table1', '{0, 採取\: 20090824}')
    click('Table1', '{0, 採取\: 20090824}')
    click('Table1', '{0, 採取\: 20090824}')
    click('Table1', '{0, 採取\: 20090824}')
    click('JTableHeader4', '{24, 採取\: 20091017}')
    click('Table1', '{0, 採取\: 20091017}')
    click('Table1', '{1, 採取\: 20091017}')
    click('Table1', '{3, 採取\: 20091017}')
    click('LaboTestGraphPanel', 170, 55)
    click('Table1', '{19, 採取\: 20091017}')
    click('Table1', '{12, 採取\: 20091017}')
    click('Table1', '{9, 採取\: 20091017}')
    click('Table1', '{6, 採取\: 20091017}')
    click('Table6', '{27, 項  目}')
    click('Table1', '{27, 採取\: 20091017}')
    click('Table1', '{28, 採取\: 20091017}')
    click('Table1', '{29, 採取\: 20091017}')
    click('Table1', '{30, 採取\: 20091017}')
    click('Table1', '{0, 採取\: 20091017}')
    click('Table1', '{27, 採取\: 20091017}')
  }

  with_window('山本 一真(ヤマモト カズマ) : 00001- インスペクタ | DolphinMarathon | 医師') {
    window_closed('山本 一真(ヤマモト カズマ) : 00001- インスペクタ | DolphinMarathon | 医師')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMarathon | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMarathon | 医師')
  }

  with_window('確認') {
    click('OptionPane', 359, 130)
    click('はい')
    click('はい')
    click('はい')
    window_closed('確認')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMarathon | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMarathon | 医師')
  }

  with_window('確認') {
    click('はい')
    click('はい')
    click('はい')
    click('いいえ')
    doubleclick('はい')
    rightclick('はい')
    click('はい')
    click('はい')
    click('いいえ')
    click('はい')
    doubleclick('はい')
  }

end
