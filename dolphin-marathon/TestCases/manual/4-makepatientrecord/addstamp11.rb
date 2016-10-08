#スタンプ箱の中でスタンプをドラッッグして順番をかえるテスト
#

with_window('ログイン-OpenDolphin-1.8') {
    select('PasswordField', 'doldol')
  }

  with_window('スタンプ箱-OpenDolphin') {
    assert_p('UserStampBox', 'Text', '注 射')
    assert_p('StampTree10', 'Text', 'エディタから発行...', 'エディタから発行...')
    assert_p('StampTree10', 'Text', 'ＩＭ', 'ＩＭ')
    assert_p('StampTree10', 'Text', 'ＩＶ（一般）', 'ＩＶ（一般）')
    assert_p('StampTree10', 'Text', 'ＩＶ（６歳未満）', 'ＩＶ（６歳未満）')
    assert_p('StampTree10', 'Text', '点滴注射（６歳以上）500ml以上', '点滴注射（６歳以上）500ml以上')
    assert_p('StampTree10', 'Text', '点滴注射（６歳未満）100ml以上', '点滴注射（６歳未満）100ml以上')
    assert_p('StampTree10', 'Text', '点滴注射（６歳未満）100ml未満', '点滴注射（６歳未満）100ml未満')
    assert_p('StampTree10', 'Text', 'じんましん', 'じんましん')
    assert_p('StampTree10', 'Text', '点滴注射（６歳以上）500ml未満', '点滴注射（６歳以上）500ml未満')
    assert_p('StampTree10', 'Text', 'ソルデム１（500ml）', 'ソルデム１（500ml）')
    assert_p('StampTree10', 'Text', 'ソルデム３（500ml）', 'ソルデム３（500ml）')
    assert_p('StampTree10', 'Text', 'アミカリック（500ml）', 'アミカリック（500ml）')
    assert_p('StampTree10', 'Text', 'ソルデム３（200ml）', 'ソルデム３（200ml）')
    assert_p('StampTree10', 'Text', '５％ブドウ糖（100ml）', '５％ブドウ糖（100ml）')
    assert_p('StampTree10', 'Text', '生食100ml＋ロセフィン', '生食100ml＋ロセフィン')
    assert_p('StampTree10', 'Text', '皮内、皮下及び筋肉内注射', '皮内、皮下及び筋肉内注射')
    assert_p('StampTree10', 'Text', '★★★', '★★★')
    assert_p('StampTree10', 'Text', '関節腔内注射', '関節腔内注射')
    assert_p('StampTree10', 'Text', '期限切れスタンプ', '期限切れスタンプ')
    assert_p('StampTree10', 'Text', 'じんましん', 'じんましん')
    select('StampTree10', '[]')
    rightclick('StampTree10', 'じんましん')
    click('StampTree10', 'じんましん')
    click('StampTree10', 'じんましん')
    select('StampTree10', '[じんましん]')
    drag_and_drop('StampTree10', 'じんましん', 'StampTree10', 'じんましん', 'move')
    click('StampTree10', 'エディタから発行...')
    select('StampTree10', '[エディタから発行...]')
    assert_p('StampTree10', 'Text', 'エディタから発行...', 'エディタから発行...')
    assert_p('StampTree10', 'Text', 'じんましん', 'じんましん')
    assert_p('StampTree10', 'Text', 'ＩＭ', 'ＩＭ')
    assert_p('StampTree10', 'Text', 'ＩＶ（一般）', 'ＩＶ（一般）')
    assert_p('StampTree10', 'Text', 'ＩＶ（６歳未満）', 'ＩＶ（６歳未満）')
    assert_p('StampTree10', 'Text', '点滴注射（６歳以上）500ml以上', '点滴注射（６歳以上）500ml以上')
    assert_p('StampTree10', 'Text', '点滴注射（６歳未満）100ml以上', '点滴注射（６歳未満）100ml以上')
    assert_p('StampTree10', 'Text', '点滴注射（６歳未満）100ml未満', '点滴注射（６歳未満）100ml未満')
    assert_p('StampTree10', 'Text', '点滴注射（６歳以上）500ml未満', '点滴注射（６歳以上）500ml未満')
    assert_p('StampTree10', 'Text', 'ソルデム１（500ml）', 'ソルデム１（500ml）')
    assert_p('StampTree10', 'Text', 'ソルデム３（500ml）', 'ソルデム３（500ml）')
    assert_p('StampTree10', 'Text', '５％ブドウ糖（100ml）', '５％ブドウ糖（100ml）')
    assert_p('StampTree10', 'Text', '生食100ml＋ロセフィン', '生食100ml＋ロセフィン')
    assert_p('StampTree10', 'Text', '皮内、皮下及び筋肉内注射', '皮内、皮下及び筋肉内注射')
    assert_p('StampTree10', 'Text', '★★★', '★★★')
    assert_p('StampTree10', 'Text', '関節腔内注射', '関節腔内注射')
    assert_p('StampTree10', 'Text', '期限切れスタンプ', '期限切れスタンプ')

    with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
      window_closed('メインウインドウ-OpenDolphin | DolphinMara | 医師')
    }

    select('StampTree10', '[エディタから発行...]')
  }

  with_window('確認') {
    click('はい')
  }
