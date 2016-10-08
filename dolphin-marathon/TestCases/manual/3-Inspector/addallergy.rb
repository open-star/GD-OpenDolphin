#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'manual/on_inspector_for'

# MEMO: データベースに依存
#アレルギー追加
def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    on_inspector_for('00038', name) do

      rightclick('table', '{0, 反応程度}')
      select_menu('追加')

      with_window('アレルギー登録-OpenDolphin') do
        select('factorFld', '卵')
        select('memoFld', '重度')
        assert_p('factorFld', 'Text', '卵')
        assert_p('factorFld', 'Text', '卵')
        assert_p('factorFld', 'Text', '卵')
        assert_p('reactionCombo', 'Text', 'severe')
        assert_p('memoFld', 'Text', '重度')
        click('クリア')
        assert_p('factorFld', 'Text', '')
        assert_p('reactionCombo', 'Text', 'severe')
        assert_p('memoFld', 'Text', '')
        assert_p('identifiedFld', 'Text', '')
        select('factorFld', '蕎麦')
        select('reactionCombo', 'mild')
        select('memoFld', '体調によって発祥')
        rightclick('identifiedFld')
        click('Table', '{4, 火}')
        assert_p('factorFld', 'Text', '蕎麦')
        assert_p('reactionCombo', 'Text', 'mild')
        assert_p('reactionCombo', 'Text', 'mild')
        assert_p('memoFld', 'Text', '体調によって発祥')
        assert_p('identifiedFld', 'Text', '2010-05-25')
        window_closed('アレルギー登録-OpenDolphin')
      end

      rightclick('table', '{0, 反応程度}')
      select_menu('追加')

      with_window('アレルギー登録-OpenDolphin') do
        select('factorFld', '蕎麦')
        select('reactionCombo', 'mild')
        select('memoFld', '体調によって発症')
        click('追加')
        window_closed('アレルギー登録-OpenDolphin')
      end

      assert_p('table', 'Text', '蕎麦', '{0, 要 因}')
    end
  end
end
