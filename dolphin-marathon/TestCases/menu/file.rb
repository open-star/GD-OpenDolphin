#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_18'

  on_main_window_as('mmmmmm', 'mmmmmm') do

    assert_content('ファイル', [ ['Disabled,新規カルテ...', 'Disabled,新規文書...', 'Disabled,開く...', 'Disabled,閉じる', 'Disabled,保存...', 'Disabled,指示', 'Enabled,ページ設定...', 'Disabled,プリント...', 'Enabled,終了']])

    # ページ設定
    select_menu('ファイル>>ページ設定...')
    with_window('ページ設定') do
      click('了解')
    end
    select_menu('ファイル>>ページ設定...')
    with_window('ページ設定') do
      click('取消し')
    end

    # 終了
    select_menu('ファイル>>終了')
    with_window('確認') do
      assert_p('OptionPane.label', 'Text', 'OpenDolphin を終了して良いですか？')
      assert_p('はい#{Y#}', 'Text', 'はい(Y)')
      assert_p('いいえ#{N#}', 'Text', 'いいえ(N)')
    end
  end
end
