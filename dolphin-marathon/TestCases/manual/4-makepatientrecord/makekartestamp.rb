#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#スタンプ箱からスタンプをドラッグ＆ドロップしてカルテに入力する

def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
    click('ログイン')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{1, 生年月日}')
    rightclick('RowTipsTable', '{1, 生年月日}')
    select_menu('カルテを開く')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      click('new_24')

      with_window('新規カルテ-OpenDolphin') {
        select('List', '[01270016  協会けんぽ]')
        select('List', '[01270016  協会けんぽ]')
        click('OK')
      }

      with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
        select('TextPane1', "昨晩から発熱\n５月２６日午前９時時点で３８．５℃\n")
      }
    }
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('StampTree5', '吐き気止め１')
    select('StampTree5', '[吐き気止め１]')
    click('StampTree5', '解熱剤２')
    select('StampTree5', '[解熱剤２]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    select('TextPane1', "昨晩から発熱\n５月２６日午前９時時点で３８．５℃\n\n \n\n")
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', '手 術')
    select('StampTree5', '[解熱剤２]')
    select('UserStampBox', '画像診断')
    select('UserStampBox', '検体検査')
    select('UserStampBox', '生体検査')
    select('UserStampBox', '検体検査')
    click('StampTree8', '{/検体検査/感染症免疫学的検査, 感染症免疫学的検査}')
    doubleclick('StampTree8', '{/検体検査/感染症免疫学的検査, 感染症免疫学的検査}')
    click('StampTree8', '{/検体検査/感染症免疫学的検査/インフルエンザウイルスＡ型抗体価, インフルエンザウイルスＡ型抗体価}')
    click('StampTree8', '{/検体検査/感染症免疫学的検査/インフルエンザウイルスＡ型抗体価, インフルエンザウイルスＡ型抗体価}')
    click('StampTree8', '{/検体検査/感染症免疫学的検査/インフルエンザウイルスＢ型抗体価, インフルエンザウイルスＢ型抗体価}')
    click('StampTree8', '{/検体検査/感染症免疫学的検査/インフルエンザウイルスＢ型抗体価, インフルエンザウイルスＢ型抗体価}')
    select('StampTree8', '[{/検体検査/感染症免疫学的検査/インフルエンザウイルスＢ型抗体価, インフルエンザウイルスＢ型抗体価}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    select('TextPane1', "２日前から発熱\n５月２６日午前９時時点で３８．５℃\n\n \n\n\n \n\n \n")
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')

    with_window('未保存処理-OpenDolphin') {
      click('保存')
    }

    with_window('ドキュメント保存-OpenDolphin') {
      click('保存して送信')
    }
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree8', '[{/検体検査/感染症免疫学的検査/インフルエンザウイルスＢ型抗体価, インフルエンザウイルスＢ型抗体価}]')
  }

  with_window('確認') {
    click('はい')
  }

end
