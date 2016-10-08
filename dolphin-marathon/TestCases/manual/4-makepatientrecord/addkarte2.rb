#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

def test
  java_recorded_version = '1.6.0_20'

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{0, 氏   名}')
    rightclick('RowTipsTable', 2, '{0, 氏   名}')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      click('new_24')

      with_window('新規カルテ-OpenDolphin') {
        select('List', '[01270016  協会けんぽ]')
        select('List', '[01270016  協会けんぽ]')
        select('全てコピー', 'true')
        click('Box$Filler5', 188, 199)
        select('前回処方を適用', 'true')
        select('全てコピー', 'true')
        assert_p('前回処方を適用', 'Text', 'false')
        assert_p('前回処方を適用', 'Text', 'false')
        assert_p('全てコピー', 'Text', 'true')
        assert_p('空白の新規カルテ', 'Text', 'false')
        assert_p('別ウィンドウで編集', 'Text', 'true')
        assert_p('タブパネルへ追加', 'Text', 'false')
        assert_p('入院', 'Text', 'false')
        assert_p('Label4', 'Text', '2010-05-25 16:58:49')
        click('OK')
      }

      with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
        select('TextPane1', "\n \n\n \n\n発熱　37.5℃\n悪寒あり\n\n本日は入浴禁止。\nシャワーのみ。\n\n\n\n")
        select_menu('ツール>>シェ−マボックス')
      }
    }
  }

  with_window('シェーマボックス') {
    click('ref_24')
    window_closed('シェーマボックス')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    click('KartePanel2', 614, 31)
    rightclick('TextPane1')
    click('ColorChooserComponent', 118, 8)
    rightclick('TextPane1')
    click('ColorChooserComponent1', 113, 8)
    select_menu('ツール')
    click('Separator', 53, 43)
    select_menu('ツール>>シェ−マボックス')
  }

  with_window('シェーマボックス') {
    window_closed('シェーマボックス')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    select_menu('挿 入>>処 方>>喘息13kg')
    click('StampHolder2')
    select('TextPane1', "\n \n\n \n\n発熱　37.5℃\n悪寒あり\n\n本日は入浴禁止。\nシャワーのみ。\n\n\n\n")
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree3', '[]')
    select('UserStampBox', '処 置')
    click('StampTree5', '痙攣止め１')
    click('StampTree5', '痙攣止め１')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree5', '[痙攣止め１]')
  }

  with_window('未保存処理-OpenDolphin') {
    click('保存')
  }

  with_window('ドキュメント保存-OpenDolphin') {
    click('保存して送信')
  }

  with_window('OpenDolphin: CLAIM 送信') {
    click('OK')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree5', '[痙攣止め１]')
  }

  with_window('確認') {
    click('はい')
  }

end
