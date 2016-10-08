#処置スタンプボックスの皮膚科難航処置１（２）スタンプは期限切れスタンプ
#その内容を参照し、IDと背景色、数量が正しく表示されているか確認する


with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('StampTree5', '皮膚科軟膏処置１(2)')
    select('StampTree5', '[皮膚科軟膏処置１(2)]')
    select('tools_24', 'true')
    select('StampTree5', '[ç]')
    click('back_16')
    click('StampTree5', '皮膚科軟膏処置１(1)')
    click('StampTree5', '皮膚科軟膏処置１(2)')
    select('StampTree5', '[皮膚科軟膏処置１(2)]')
    click('back_16')
    click('setTable10', '{0, コード}')
    assert_p('setTable10', 'Text', '140011510', '{0, コード}')
    assert_p('setTable10', 'Background', '[r=255,g=255,b=255]', '{0, コード}')
    assert_p('setTable10', 'Background.Alpha', '255', '{0, コード}')
    assert_p('setTable10', 'Background.Blue', '255', '{0, コード}')
    assert_p('setTable10', 'Background.ColorSpace', '@2bb8ee96', '{0, コード}')
    assert_p('setTable10', 'Background.Green', '255', '{0, コード}')
    assert_p('setTable10', 'Background.RGB', '-1', '{0, コード}')
    assert_p('setTable10', 'Background.Red', '255', '{0, コード}')
    assert_p('setTable10', 'Background.Transparency', '1', '{0, コード}')
    assert_p('setTable10', 'Background', '[r=255,g=255,b=255]', '{2, コード}')
    assert_p('setTable10', 'Background.Alpha', '255', '{2, コード}')
    assert_p('setTable10', 'Background.Blue', '255', '{2, コード}')
    assert_p('setTable10', 'Background.ColorSpace', '@2bb8ee96', '{2, コード}')
    assert_p('setTable10', 'Background.Green', '255', '{2, コード}')
    assert_p('setTable10', 'Background.RGB', '-1', '{2, コード}')
    assert_p('setTable10', 'Background.Red', '255', '{2, コード}')
    assert_p('setTable10', 'Background.Transparency', '1', '{2, コード}')
    assert_p('setTable10', 'Text', '620009150', '{1, コード}')
    assert_p('setTable10', 'Text', '0', '{1, 数 量}')
    window_closed('スタンプ箱-OpenDolphin')
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
  }

  with_window('確認') {
    click('はい')
  }
