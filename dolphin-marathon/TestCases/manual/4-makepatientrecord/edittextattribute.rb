#カルテ入力の際、テキストの属性（装飾、文字色）などを変更し保存できることを確認する

with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', '処 方')

    with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
      click('RowTipsTable', '{0, 性別}')
      rightclick('RowTipsTable', '{1, 氏   名}')
      click('RowTipsTable', '{0, 氏   名}')
      rightclick('RowTipsTable', 2, '{0, 氏   名}')
      select_menu('カルテを開く')

      with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
        click('TextPane1')
        click('new_24')

        with_window('新規カルテ-OpenDolphin') {
          select('List', '[01270016  協会けんぽ]')
          select('List', '[01270016  協会けんぽ]')
          click('OK')
        }
      }
    }

    select('StampTree11', '[]')
    click('StampTree11', '{/処 方/咳止め大人, 咳止め大人}')
    select('StampTree11', '[{/処 方/咳止め大人, 咳止め大人}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    select('TextPane1', "昨日から乾いた咳\n夜眠れない\n朝方に悪化\n\n \n")
    select_menu('テキスト>>サイズ>>大きく')
    select_menu('テキスト>>サイズ>>大きく')
    select_menu('テキスト>>サイズ>>小さく')
    select_menu('テキスト>>サイズ>>小さく')
    select_menu('テキスト>>サイズ>>小さく')
    select_menu('テキスト>>スタイル>>ボールド')
    select_menu('テキスト>>スタイル>>イタリック')
    select_menu('テキスト>>スタイル>>アンダーライン')
    select_menu('テキスト>>行揃え>>中央揃え')
    select_menu('テキスト>>行揃え>>右揃え')
    click('StampHolder')
    select_menu('テキスト>>行揃え>>左揃え')
    select_menu('テキスト>>カラー>>レッド')
    select_menu('テキスト>>カラー>>オレンジ')
    select_menu('テキスト>>カラー>>イェロー')
    select_menu('テキスト>>カラー>>グリーン')
    select_menu('テキスト>>カラー>>ブルー')
    select_menu('テキスト>>カラー>>パープル')
    select_menu('テキスト>>カラー>>グレイ')
    select_menu('テキスト>>カラー>>黒')
    rightclick('StampHolder')
    click('StampHolder')
    rightclick('StampHolder')
    rightclick('TextPane1')
    select_menu('コピー2')
    rightclick('TextPane1')
    click('Separator1', 19, 83)
    click('StampHolder')
    rightclick('StampHolder')
    rightclick('TextPane1')
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')

    with_window('未保存処理-OpenDolphin') {
      click('保存')
    }

    with_window('ドキュメント保存-OpenDolphin') {
      click('保存して送信')
    }
  }


  with_window('確認') {
    click('はい')
  }
