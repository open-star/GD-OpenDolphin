#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{0, 氏   名}')
    rightclick('RowTipsTable', 2, '{0, 氏   名}')
    select_menu('カルテを開く')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師(1)') {
    click('docs_24')

    with_window('新規文書作成-OpenDolphin') {
      select('List', '[紹介状]')
      assert_p('List', 'Text', '紹介状', '紹介状')
      select('List', '[紹介状]')
      click('了解')
    }

    click('docs_24')

    with_window('新規文書作成-OpenDolphin') {
      click('取消し')
    }

    select('cHospital', 'グッデイ診療所')
    click('参照2')
    click('Form', 504, 140)
    click('参照1')
    click('Form', 197, 118)
    click('参照1')
    select_menu('内科1')
    click('参照')
    select_menu('テスト太郎')
    click('patientName')
    assert_p('cHospital', 'Text', 'グッデイ診療所')
    assert_p('cDept', 'Text', '内科')
    assert_p('cDoctor', 'Text', 'テスト太郎')
    select('disease', '胃癌')
    select('purpose', '検査のため')
    select('pastFamily', '父親　腎臓結石の病歴あり')
    select('clinicalCourse', '胃に痛みあり')
    select('clinicalCourse', "胃に痛みあり\n")
    select('medication', '整腸剤にて経過観察中')
    select('remarks', '特になし')
    click('save_24')
    click('print_24')

    with_window('紹介状印刷-OpenDolphin') {
      click('PDF作成')
    }

    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師(1)')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{0, 氏   名}')
    rightclick('RowTipsTable', '{0, 氏   名}')
    select_menu('カルテを開く')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      click('docs_24')

      with_window('新規文書作成-OpenDolphin') {
        click('取消し')
      }

      window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
    }

    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }

end
