#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture
#

require 'common/on_login_window'
require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_20'

  on_login_window do
    select('TextField', '')
    select('PasswordField', '')
    assert_p('ログイン', 'Enabled', 'false')
    select('TextField', 'MarathonMarathon')
    assert_p('ログイン', 'Enabled', 'false')
    select('PasswordField', 'dolphin')
    assert_p('ログイン', 'Enabled', 'true')
    select('TextField', '')
    assert_p('ログイン', 'Enabled', 'false')
  end

  on_main_window_as('MarathonDolphin', 'dolphin') do

    select_menu('ツール>>プロフィール変更...')

    with_window('プロフィール変更-OpenDolphin') do
      assert_p('TextField', 'Text', 'MarathonDolphin')
      assert_p('PasswordField', 'Text', '')
      assert_p('PasswordField1', 'Text', '')
      assert_p('TextField1', 'Text', 'ドルフィン')
      assert_p('TextField2', 'Text', 'マラソン')
      assert_p('TextField3', 'Text', 'dolphin_marathon@example.com')
      assert_p('ComboBox', 'Text', '医師')
      assert_p('ComboBox1', 'Text', '内科')

      click('閉じる')
    end

    select_menu('ツール>>プロフィール変更...')

    with_window('プロフィール変更-OpenDolphin') do
      %w[
          医師 歯科医師 看護師 准看護師 臨床検査技師
          レントゲン技師 薬剤師 理学療法士 
      ].each do |name|
          select('ComboBox', name)
      end

      [
        '精神科', '神経科', '呼吸器科', '消化器科', '胃腸科', '循環器科',
        '内科', '小児科', '外科', '整形外科', '形成外科', '美容外科',
        '脳神経外科', '呼吸器外科', '心臓血管外科', '小児外科',
        '皮膚ひ尿器科', '皮膚科', 'ひ尿器科', ' 性病科', 'こう門科',
        '産婦人科', '産科', '婦人科', '眼科', '耳鼻いんこう科',
        '気管食道科', '理学診療科', '放射線科', '麻酔科', '人工透析科',
        ' 心療内科', 'アレルギー', 'リウマチ', 'リハビリ', '鍼灸',
      ].each do |name|
        select('ComboBox1', name)
      end
    end
  end
end

