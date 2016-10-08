#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

# MEMO: ファイルシステムに依存しているため保留
def test
  java_recorded_version = '1.6.0_20'

  with_window('ログイン-OpenDolphin-1.8') {
    select('TextField', 'DolphinMarathon')
  }

  with_window('ログイン-OpenDolphin-1.8(1)') {
    click('OK')
  }

  with_window('ログイン-OpenDolphin-1.8') {
    select('TextField', 'MarathonDolphin')
    select('TextField', 'DolphinMarathon')
  }

  with_window('スタンプ箱-OpenDolphin') {
    click('move_24')

    with_window('') {
      select('FileOpenDialog', '#H/Desktop/zのstmp.stamp')
    }
  }

  with_window('確認') {
    click('はい')
  }

  with_window('メッセージ') {
    click('OK')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('UserStampBox', 'テキスト')
    select('UserStampBox', 'パ ス')
    select('UserStampBox', 'ORCA')
    click('OrcaTree', '{/ORCA/紹介状, 紹介状}')
    select('OrcaTree', '[{/ORCA/紹介状, 紹介状}]')
    click('OrcaTree', '{/ORCA/Ｓ桂枝加朮附湯, Ｓ桂枝加朮附湯}')
    click('OrcaTree', '{/ORCA/紹介状, 紹介状}')
    select('OrcaTree', '[{/ORCA/紹介状, 紹介状}]')
    rightclick('OrcaTree', '{/ORCA/紹介状, 紹介状}')
    assert_p('OrcaTree', 'Text', '紹介状', '{/ORCA/紹介状, 紹介状}')
    click('OrcaTree', '{/ORCA/再診（労災・自賠責）, 再診（労災・自賠責）}')
    click('OrcaTree', '{/ORCA/abc, abc}')
    click('OrcaTree', '{/ORCA/在宅診療ー居宅入居者, 在宅診療ー居宅入居者}')
    click('OrcaTree', '{/ORCA/かぜ薬セット, かぜ薬セット}')
    click('OrcaTree', '{/ORCA/小児シロップ, 小児シロップ}')
    click('OrcaTree', '{/ORCA/薬２, 薬２}')
    click('OrcaTree', '{/ORCA/ネブライザー, ネブライザー}')
    click('OrcaTree', '{/ORCA/血液検査－高脂血症, 血液検査－高脂血症}')
    click('OrcaTree', '{/ORCA/血液検査－高血圧, 血液検査－高血圧}')
    click('OrcaTree', '{/ORCA/血液検査－糖尿病, 血液検査－糖尿病}')
    click('OrcaTree', '{/ORCA/アレルギーテスト３, アレルギーテスト３}')
    click('OrcaTree', '{/ORCA/非特異１３, 非特異１３}')
    click('OrcaTree', '{/ORCA/胸部レントゲン, 胸部レントゲン}')
    click('OrcaTree', '{/ORCA/胸部レントゲン, 胸部レントゲン}')
    click('OrcaTree', '{/ORCA/膝関節レントゲン, 膝関節レントゲン}')
    click('OrcaTree', '{/ORCA/腎盂造影, 腎盂造影}')
    click('OrcaTree', '{/ORCA/頭部レントゲン, 頭部レントゲン}')
    click('OrcaTree', '{/ORCA/ＣＴ, ＣＴ}')
    click('OrcaTree', '{/ORCA/透析セット, 透析セット}')
    click('OrcaTree', '{/ORCA/透析管理料, 透析管理料}')
    click('OrcaTree', '{/ORCA/ネオファーゲン注, ネオファーゲン注}')
    click('OrcaTree', '{/ORCA/腎盂腎炎検査, 腎盂腎炎検査}')
    click('OrcaTree', '{/ORCA/細菌塗沫培養同定(尿), 細菌塗沫培養同定(尿)}')
    click('OrcaTree', '{/ORCA/腎盂腎炎薬セット, 腎盂腎炎薬セット}')
    click('OrcaTree', '{/ORCA/腹部Ｘ－Ｐ, 腹部Ｘ－Ｐ}')
    click('OrcaTree', '{/ORCA/腎盂造影, 腎盂造影}')
    click('OrcaTree', '{/ORCA/腎盂造影点滴セット, 腎盂造影点滴セット}')
    click('OrcaTree', '{/ORCA/腎盂腎炎薬セット２, 腎盂腎炎薬セット２}')
    click('OrcaTree', '{/ORCA/薬剤感受性(１菌種), 薬剤感受性(１菌種)}')
    select('OrcaTree', '[{/ORCA/腎盂腎炎薬セット２, 腎盂腎炎薬セット２}]')
    drag_and_drop('OrcaTree', '{/ORCA/薬剤感受性(１菌種), 薬剤感受性(１菌種)}', 'OrcaTree', '{/ORCA/薬剤感受性(１菌種), 薬剤感受性(１菌種)}', 'move')
    click('OrcaTree', '{/ORCA/処方S, 処方S}')
    click('OrcaTree', '{/ORCA/小児シロップ, 小児シロップ}')
    click('OrcaTree', '{/ORCA/小児粉薬, 小児粉薬}')
    click('OrcaTree', '{/ORCA/Ｓ滋陰降火湯, Ｓ滋陰降火湯}')
    click('OrcaTree', '{/ORCA/Ｓ防己黄ぎ湯, Ｓ防己黄ぎ湯}')
    click('OrcaTree', '{/ORCA/Ｓ麻杏意甘湯, Ｓ麻杏意甘湯}')
    click('OrcaTree', '{/ORCA/Ｓ桂枝加朮附湯, Ｓ桂枝加朮附湯}')

    with_window('メインウインドウ-OpenDolphin | DolphinMarathon | 医師') {
      window_closed('メインウインドウ-OpenDolphin | DolphinMarathon | 医師')
    }

    select('OrcaTree', '[{/ORCA/Ｓ桂枝加朮附湯, Ｓ桂枝加朮附湯}]')
  }

  with_window('確認') {
    click('はい')
  }

end
