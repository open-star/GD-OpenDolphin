  with_window('ログイン-OpenDolphin-1.8') {
    select('TextField', 'admin')
    select('PasswordField', 'admin')
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('exp_24')
  }

  with_window('スタンプ公開-OpenDolphin') {
    select('TextField', 'StampCommon')
    select('院内', 'true')
    select('ComboBox', '院内シェア')
    select('傷病名', 'true')
    select('テキスト', 'true')
    select('パ ス', 'true')
    select('汎 用', 'true')
    select('その他', 'true')
    select('処 置', 'true')
    select('手 術', 'true')
    select('TextField1', 'グッデイ第三ビル内クリニック')
    select('TextField2', 'aaaa')
    select('TextField3', '院内にて共用してください')
    assert_p('TextField', 'Text', 'StampCommon')
    assert_p('院内', 'Text', 'true')
    assert_p('グローバル', 'Text', 'false')
    assert_p('ComboBox', 'Text', '院内シェア')
    assert_p('傷病名', 'Text', 'true')
    assert_p('テキスト', 'Text', 'true')
    assert_p('パ ス', 'Text', 'true')
    assert_p('ORCA', 'Text', 'false')
    assert_p('汎 用', 'Text', 'true')
    assert_p('その他', 'Text', 'true')
    assert_p('処 置', 'Text', 'true')
    assert_p('手 術', 'Text', 'true')
    assert_p('画像診断', 'Text', 'false')
    assert_p('検体検査', 'Text', 'false')
    assert_p('生体検査', 'Text', 'false')
    assert_p('注 射', 'Text', 'false')
    assert_p('処 方', 'Text', 'false')
    assert_p('初診・再診', 'Text', 'false')
    assert_p('指導', 'Text', 'false')
    assert_p('在宅', 'Text', 'false')
    assert_p('TextField1', 'Text', 'グッデイ第三ビル内クリニック')
    assert_p('TextField2', 'Text', 'aaaa')
    assert_p('TextField3', 'Text', '院内にて共用してください')
    assert_p('Label11', 'Text', '2010-05-27')
    click('公開する')

    with_window('スタンプ公開-OpenDolphin(1)') {
      click('OK')
    }
  }

  with_window('メインウインドウ-OpenDolphin | admin | 医師') {
    window_closed('メインウインドウ-OpenDolphin | admin | 医師')
  }

  with_window('確認') {
    click('はい')
  }
