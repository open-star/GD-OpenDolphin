#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

# MEMO: リスナが動いていないので通らない

def test
  java_recorded_version = '1.6.0_20'

   on_main_window_as('admin', 'admin') do

    select_menu('ツール>>院内ユーザ登録...')

    with_window('ユーザ管理-OpenDolphin') {
      select('TabbedPane', 'ユーザ登録')

      select('TextField8', 'DolphinMarathon')
      assert_p('追加', 'Enabled', 'false')
      select('PasswordField', 'dolphin')
      assert_p('追加', 'Enabled', 'false')
      select('PasswordField1', 'dolphin')
      assert_p('追加', 'Enabled', 'false')
      select('TextField9', 'ドルフィン')
      assert_p('追加', 'Enabled', 'false')
      select('TextField10', 'マラソン')
      assert_p('追加', 'Enabled', 'false')
      select('TextField11', 'dolphin_marathon@example.com')
      assert_p('追加', 'Enabled', 'true')

      # Enable or Disable
      # Memo: TextField8, PasswordField, PasswordField1 はリスナなし。
      #       他のところから入力してもだめ
      select('TextField8', '')
      assert_p('追加', 'Enabled', 'false')
      select('TextField8', 'DolphinMarathon')
      assert_p('追加', 'Enabled', 'true')

      select('PasswordField', '')
      assert_p('追加', 'Enabled', 'false')
      select('PasswordField', 'dolphin')
      assert_p('追加', 'Enabled', 'true')

      select('PasswordField1', '')
      assert_p('追加', 'Enabled', 'false')
      select('PasswordField1', 'dolphin')
      assert_p('追加', 'Enabled', 'true')
      
      select('PasswordField', 'password')
      assert_p('追加', 'Enabled', 'false')
      select('PasswordField1', 'password')
      assert_p('追加', 'Enabled', 'true')

      select('TextField9', '')
      assert_p('追加', 'Enabled', 'false')
      select('TextField9', 'ドルフィン')
      assert_p('追加', 'Enabled', 'true')

      select('TextField10', '')
      assert_p('追加', 'Enabled', 'false')
      select('TextField10', 'マラソン')
      assert_p('追加', 'Enabled', 'true')

      select('TextField11', '')
      assert_p('追加', 'Enabled', 'false')
      select('TextField11', 'dolphin_marathon@example.com')
      assert_p('追加', 'Enabled', 'true')

      # MEMO: 同名のものは追加出来ない
      #       警告が欲しい
      # click('追加')
    }
  end
end
