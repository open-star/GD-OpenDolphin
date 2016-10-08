#スタンプをエディタから発行から作成する
#その際、スタンプメーカーの一覧にデータを表示し、いったん削除する
#再度、検索し、他のお薬のデータを一覧に表示させスタンプを作成する
#そのスタンプをカルテにはりつけ、保存できることを確認する


with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    click('RowTipsTable', '{0, 氏   名}')
    rightclick('RowTipsTable', '{0, 内容}')
    select_menu('カルテを開く')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      click('new_24')

      with_window('新規カルテ-OpenDolphin') {
        select('List', '[01270016  協会けんぽ]')
        select('List', '[01270016  協会けんぽ]')
        click('OK')
      }
    }
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('StampTree11', '{/処 方/エディタから発行..., エディタから発行...}')
    click('StampTree11', '{/処 方/エディタから発行..., エディタから発行...}')
    select('StampTree11', '[{/処 方/エディタから発行..., エディタから発行...}]')
  }

  with_window('処方エディタ-OpenDolphin') {
    select('TextField', 'ロキソ')
    select('部分一致', 'true')
    doubleclick('table', '{0, 名  称}')
    select('tabbedPane', '用法')
    select('ComboBox', '内服３回等(300)')
    doubleclick('table1', '{4, 名 称}')
    assert_p('medTable', 'Text', 'ロキソート錠　６０ｍｇ', '{0, 診療内容}')
    assert_p('medTable', 'Text', '[用法] １日３回毎食前に', '{1, 診療内容}')
    click('remov_16')
    assert_p('Viewport', 'Enabled', 'true')
    select('tabbedPane', '内用・外用薬')
    select('部分一致', 'false')
    select('部分一致', 'true')
    doubleclick('table', '{2, 名  称}')
    assert_p('medTable', 'Text', 'ロキソニン錠　６０ｍｇ', '{0, 診療内容}')
    select('tabbedPane', '用法')
    select('ComboBox', '選択してください')
    select('ComboBox', '内服３回等(300)')
    doubleclick('table1', '{0, 名 称}')
    assert_p('medTable', 'Text', '[用法] １日３回毎食後に', '{1, 診療内容}')
    click('lgicn_16')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree11', '[{/処 方/エディタから発行..., エディタから発行...}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    click('StampHolder')
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')
    select('TextPane1', "\n \nスタンプエディタから発行したスタンプが\nカルテに貼付けられて、保存できることを確認する\n")

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
    select('StampTree11', '[{/処 方/エディタから発行..., エディタから発行...}]')
  }

  with_window('確認') {
    click('はい')
  }
