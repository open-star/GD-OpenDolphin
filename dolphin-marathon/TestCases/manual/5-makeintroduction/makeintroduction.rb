#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#紹介患者経過報告書を作成し、PDFに出力する
def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{0, 氏   名}')
    rightclick('RowTipsTable', '{0, 氏   名}')
    select_menu('カルテを開く')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
    }

    rightclick('RowTipsTable', '{0, 氏   名}')
    select_menu('カルテを開く')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      click('docs_24')

      with_window('新規文書作成-OpenDolphin') {
        select('List', '[紹介患者経過報告書]')
        assert_p('List', 'Text', '紹介患者経過報告書', '紹介患者経過報告書')
        select('List', '[紹介患者経過報告書]')
        click('了解')
      }

      select('clientHospital', 'グッデイクリニック第三ビル内')
      select('clientDept', '外科')
      select('clientDoctor', 'ドルフィンマラソン')
      select('informedContent', "胃内視検査の結果、特に異常が認められませんでした。\n")
      click('save_24')
      click('print_24')

      with_window('紹介状印刷-OpenDolphin') {
        click('PDF作成')
      }

      with_window('紹介状作成-OpenDolphin') {
        click('OK')
      }

      window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
    }

    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }

end
